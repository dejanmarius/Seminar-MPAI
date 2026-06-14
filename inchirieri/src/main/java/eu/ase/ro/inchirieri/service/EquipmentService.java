package eu.ase.ro.inchirieri.service;

import eu.ase.ro.inchirieri.dto.request.EquipmentRequest;
import eu.ase.ro.inchirieri.dto.response.EquipmentResponse;
import eu.ase.ro.inchirieri.model.Equipment;
import eu.ase.ro.inchirieri.repository.EquipmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/** CRUD complet pentru catalogul de echipamente (gestionat de admin). */
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/init-equipments.txt");
        if (is == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                equipmentRepository.save(new Equipment(parts[0].trim(), parts[1].trim(),
                        Boolean.parseBoolean(parts[2].trim())));
            }
        } catch (Exception e) {
            System.err.println("Eroare init echipamente: " + e.getMessage());
        }
    }

    public List<EquipmentResponse> getAll() {
        return equipmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<EquipmentResponse> getAvailable() {
        return equipmentRepository.findByAvailableTrue().stream().map(this::toResponse).toList();
    }

    public EquipmentResponse findById(Long id) {
        return equipmentRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Echipamentul nu a fost gasit: " + id));
    }

    public void create(EquipmentRequest request) {
        equipmentRepository.save(new Equipment(request.getName(),
                request.getDescription(), request.isAvailable()));
    }

    public void update(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Echipamentul nu a fost gasit: " + id));
        equipment.setName(request.getName());
        equipment.setDescription(request.getDescription());
        equipment.setAvailable(request.isAvailable());
        equipmentRepository.save(equipment);
    }

    public void delete(Long id) {
        equipmentRepository.deleteById(id);
    }

    /** Schimba disponibilitatea unui echipament (apelat de RentalService la tranzitii). */
    public void setAvailability(Long equipmentId, boolean available) {
        if (equipmentId == null) {
            return;
        }
        equipmentRepository.findById(equipmentId).ifPresent(equipment -> {
            equipment.setAvailable(available);
            equipmentRepository.save(equipment);
        });
    }

    private EquipmentResponse toResponse(Equipment equipment) {
        return new EquipmentResponse(equipment.getId(), equipment.getName(),
                equipment.getDescription(), equipment.isAvailable());
    }
}

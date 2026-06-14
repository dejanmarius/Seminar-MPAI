package eu.ase.ro.inchirieri.service;

import eu.ase.ro.inchirieri.dto.request.RentalRequestDto;
import eu.ase.ro.inchirieri.dto.response.EquipmentResponse;
import eu.ase.ro.inchirieri.dto.response.RentalResponse;
import eu.ase.ro.inchirieri.model.RentalRequest;
import eu.ase.ro.inchirieri.model.RentalStatus;
import eu.ase.ro.inchirieri.repository.RentalRequestRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

/**
 * Logica cererilor de inchiriere: populare initiala, filtrare, creare,
 * schimbare de stare (cu validarea tranzitiilor) si anulare de catre utilizator.
 * La aprobare echipamentul devine indisponibil; la returnare/respingere revine disponibil.
 *
 * {@code @DependsOn} garanteaza ca echipamentele sunt populate inainte de cereri.
 */
@Service
@DependsOn("equipmentService")
public class RentalService {

    private final RentalRequestRepository rentalRepository;
    private final EquipmentService equipmentService;
    private final NotificationService notificationService;

    public RentalService(RentalRequestRepository rentalRepository,
                         EquipmentService equipmentService,
                         NotificationService notificationService) {
        this.rentalRepository = rentalRepository;
        this.equipmentService = equipmentService;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/init-data.txt");
        if (is == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                // format: userName,userEmail,equipmentId,equipmentName,zileStart,zileDurata,purpose
                String[] parts = line.split(",");
                LocalDate start = LocalDate.now().plusDays(Long.parseLong(parts[4].trim()));
                LocalDate end = start.plusDays(Long.parseLong(parts[5].trim()));
                RentalRequest request = new RentalRequest(parts[0].trim(), parts[1].trim(),
                        Long.parseLong(parts[2].trim()), parts[3].trim(), start, end, parts[6].trim());
                rentalRepository.save(request);
            }
        } catch (Exception e) {
            System.err.println("Eroare la initializarea datelor: " + e.getMessage());
        }
    }

    public List<RentalResponse> filter(String status, String userName,
                                       String equipmentName, String date) {
        RentalStatus st = (status != null && !status.isBlank())
                ? RentalStatus.valueOf(status) : null;
        String user = (userName != null && !userName.isBlank()) ? userName : null;
        String equipment = (equipmentName != null && !equipmentName.isBlank()) ? equipmentName : null;
        LocalDate d = (date != null && !date.isBlank()) ? LocalDate.parse(date) : null;
        return rentalRepository.findByFilters(st, user, equipment, d).stream()
                .map(this::toResponse)
                .toList();
    }

    public RentalResponse findById(Long id) {
        return rentalRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Cererea nu a fost gasita: " + id));
    }

    /** Lista echipamentelor disponibile pentru formularul de cerere noua. */
    public List<EquipmentResponse> getAvailableEquipments() {
        return equipmentService.getAvailable();
    }

    public void create(RentalRequestDto dto) {
        EquipmentResponse equipment = equipmentService.findById(dto.getEquipmentId());
        RentalRequest request = new RentalRequest(
                dto.getUserName(), dto.getUserEmail(),
                equipment.getId(), equipment.getName(),
                LocalDate.parse(dto.getStartDate()), LocalDate.parse(dto.getEndDate()),
                dto.getPurpose());
        rentalRepository.save(request);
        notificationService.send(request.getUserEmail(),
                "Cererea ta de inchiriere pentru '" + equipment.getName() + "' a fost inregistrata.");
    }

    /** Schimbare de stare de catre admin (aproba/respinge/preia/returneaza). */
    public void updateStatus(Long id, String newStatusStr) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cererea nu a fost gasita: " + id));

        RentalStatus newStatus = RentalStatus.valueOf(newStatusStr);
        if (!request.getStatus().nextStates().contains(newStatus)) {
            throw new RuntimeException("Tranzitie nepermisa: "
                    + request.getStatus() + " -> " + newStatus);
        }
        applyAvailabilityChange(request, newStatus);
        request.setStatus(newStatus);
        rentalRepository.save(request);
        notificationService.send(request.getUserEmail(),
                "Starea cererii tale a fost actualizata: " + newStatus.name());
    }

    /** Anulare de catre utilizator: doar inainte de aprobare (stare CERUTA). */
    public void cancelByUser(Long id) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cererea nu a fost gasita: " + id));

        if (!request.getStatus().canBeCancelledByUser()) {
            throw new RuntimeException("Cererea nu mai poate fi anulata (stare: "
                    + request.getStatus() + ").");
        }
        request.setStatus(RentalStatus.ANULATA);
        rentalRepository.save(request);
        notificationService.send(request.getUserEmail(), "Cererea ta a fost anulata.");
    }

    /**
     * Actualizeaza disponibilitatea echipamentului la tranzitii:
     *  - APROBATA  -> echipamentul devine indisponibil (rezervat)
     *  - RETURNATA / RESPINSA -> echipamentul revine disponibil
     */
    private void applyAvailabilityChange(RentalRequest request, RentalStatus newStatus) {
        switch (newStatus) {
            case APROBATA -> equipmentService.setAvailability(request.getEquipmentId(), false);
            case RETURNATA, RESPINSA -> equipmentService.setAvailability(request.getEquipmentId(), true);
            default -> { /* celelalte tranzitii nu schimba disponibilitatea */ }
        }
    }

    private RentalResponse toResponse(RentalRequest request) {
        return new RentalResponse(
                request.getId(),
                request.getUserName(),
                request.getUserEmail(),
                request.getEquipmentName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getPurpose(),
                request.getStatus().name(),
                request.getStatus().canBeCancelledByUser()
        );
    }
}

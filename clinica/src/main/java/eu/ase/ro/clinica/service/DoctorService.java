package eu.ase.ro.clinica.service;

import eu.ase.ro.clinica.dto.request.DoctorRequest;
import eu.ase.ro.clinica.dto.response.DoctorResponse;
import eu.ase.ro.clinica.model.Doctor;
import eu.ase.ro.clinica.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/** CRUD complet pentru catalogul de medici (gestionat de admin). */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/init-doctors.txt");
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
                doctorRepository.save(new Doctor(parts[0].trim(), parts[1].trim()));
            }
        } catch (Exception e) {
            System.err.println("Eroare init medici: " + e.getMessage());
        }
    }

    public List<DoctorResponse> getAll() {
        return doctorRepository.findAll().stream().map(this::toResponse).toList();
    }

    public DoctorResponse findById(Long id) {
        return doctorRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Medicul nu a fost gasit: " + id));
    }

    /** Entitatea Doctor (folosita intern de AppointmentService pentru relatie). */
    public Doctor getEntityById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicul nu a fost gasit: " + id));
    }

    /** Cauta medicul dupa nume (folosit la popularea initiala din fisier). */
    public Doctor getEntityByName(String name) {
        return doctorRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Medicul nu a fost gasit: " + name));
    }

    public void create(DoctorRequest request) {
        doctorRepository.save(new Doctor(request.getName(), request.getSpecialization()));
    }

    public void update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicul nu a fost gasit: " + id));
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctorRepository.save(doctor);
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(doctor.getId(), doctor.getName(), doctor.getSpecialization());
    }
}

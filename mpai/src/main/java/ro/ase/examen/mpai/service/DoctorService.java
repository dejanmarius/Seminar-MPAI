package ro.ase.examen.mpai.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ro.ase.examen.mpai.dto.request.DoctorRequest;
import ro.ase.examen.mpai.dto.response.DoctorResponse;
import ro.ase.examen.mpai.models.Doctor;
import ro.ase.examen.mpai.repository.DoctorRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void init(){
        InputStream inputStream = getClass().getResourceAsStream("/data/doctors.txt");
        if (inputStream == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
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


    public Doctor getEntityById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicul nu a fost gasit: " + id));
    }

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

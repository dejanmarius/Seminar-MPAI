package eu.ase.ro.clinica.service;

import eu.ase.ro.clinica.dto.request.AppointmentRequest;
import eu.ase.ro.clinica.dto.response.AppointmentResponse;
import eu.ase.ro.clinica.model.Appointment;
import eu.ase.ro.clinica.model.AppointmentStatus;
import eu.ase.ro.clinica.model.Doctor;
import eu.ase.ro.clinica.repository.AppointmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica programarilor: populare initiala, filtrare (stare/medic/pacient/data),
 * creare, schimbare de stare (cu validarea tranzitiilor) si anulare de catre pacient
 * doar daca mai sunt minim {@link #MIN_HOURS_BEFORE_CANCEL} ore pana la programare.
 */
@Service
@DependsOn("doctorService") // medicii trebuie populati inainte de programari
public class AppointmentService {

    // Constrangerea temporala: anulare permisa cu minim X ore inainte
    private static final int MIN_HOURS_BEFORE_CANCEL = 24;

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorService doctorService,
                              NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
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
                // format: patientName,patientEmail,doctorName,zilePanaLaProgramare,reason
                String[] parts = line.split(",");
                Doctor doctor = doctorService.getEntityByName(parts[2].trim());
                LocalDateTime when = LocalDateTime.now()
                        .plusDays(Long.parseLong(parts[3].trim()))
                        .withHour(10).withMinute(0).withSecond(0).withNano(0);
                Appointment appointment = new Appointment(parts[0].trim(), parts[1].trim(),
                        doctor, when, parts[4].trim());
                appointmentRepository.save(appointment);
            }
        } catch (Exception e) {
            System.err.println("Eroare la initializarea datelor: " + e.getMessage());
        }
    }

    public List<AppointmentResponse> filter(String status, String doctorName,
                                            String patientName, String date) {
        AppointmentStatus st = (status != null && !status.isBlank())
                ? AppointmentStatus.valueOf(status) : null;
        String doctor = (doctorName != null && !doctorName.isBlank()) ? doctorName : null;
        String patient = (patientName != null && !patientName.isBlank()) ? patientName : null;
        LocalDate d = (date != null && !date.isBlank()) ? LocalDate.parse(date) : null;
        return appointmentRepository.findByFilters(st, doctor, patient, d).stream()
                .map(this::toResponse)
                .toList();
    }

    public AppointmentResponse findById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita: " + id));
    }

    public void create(AppointmentRequest request) {
        Doctor doctor = doctorService.getEntityById(request.getDoctorId());
        LocalDateTime when = LocalDateTime.parse(request.getAppointmentDateTime());
        Appointment appointment = new Appointment(request.getPatientName(),
                request.getPatientEmail(), doctor, when, request.getReason());
        appointmentRepository.save(appointment);
        notificationService.send(appointment.getPatientEmail(),
                "Programarea ta a fost solicitata pentru " + when + ".");
    }

    /** Schimbare de stare de catre admin/receptie (confirma / efectueaza / anuleaza). */
    public void updateStatus(Long id, String newStatusStr) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita: " + id));

        AppointmentStatus newStatus = AppointmentStatus.valueOf(newStatusStr);
        if (!appointment.getStatus().nextStates().contains(newStatus)) {
            throw new RuntimeException("Tranzitie nepermisa: "
                    + appointment.getStatus() + " -> " + newStatus);
        }
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        notificationService.send(appointment.getPatientEmail(),
                "Starea programarii tale a fost actualizata: " + newStatus.name());
    }

    /** Anulare de catre pacient: doar din stare anulabila SI cu minim X ore inainte. */
    public void cancelByPatient(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita: " + id));

        if (!appointment.getStatus().canBeCancelledByPatient()) {
            throw new RuntimeException("Programarea nu mai poate fi anulata (stare: "
                    + appointment.getStatus() + ").");
        }
        if (!hasEnoughTimeToCancel(appointment.getAppointmentDateTime())) {
            throw new RuntimeException("Anularea e permisa cu minim "
                    + MIN_HOURS_BEFORE_CANCEL + " ore inainte de programare.");
        }
        appointment.setStatus(AppointmentStatus.ANULATA);
        appointmentRepository.save(appointment);
        notificationService.send(appointment.getPatientEmail(),
                "Programarea ta a fost anulata.");
    }

    private boolean hasEnoughTimeToCancel(LocalDateTime appointmentDateTime) {
        return LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_CANCEL)
                .isBefore(appointmentDateTime);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        boolean cancellable = appointment.getStatus().canBeCancelledByPatient()
                && hasEnoughTimeToCancel(appointment.getAppointmentDateTime());
        Doctor doctor = appointment.getDoctor();
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientName(),
                appointment.getPatientEmail(),
                doctor != null ? doctor.getName() : null,
                doctor != null ? doctor.getSpecialization() : null,
                appointment.getAppointmentDateTime(),
                appointment.getReason(),
                appointment.getStatus().name(),
                cancellable
        );
    }
}

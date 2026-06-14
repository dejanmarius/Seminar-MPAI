package ro.ase.examen.mpai.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ro.ase.examen.mpai.dto.request.AppointmentRequest;
import ro.ase.examen.mpai.dto.response.AppointmentResponse;
import ro.ase.examen.mpai.models.Appointment;
import ro.ase.examen.mpai.models.AppointmentStatus;
import ro.ase.examen.mpai.models.Doctor;
import ro.ase.examen.mpai.repository.AppointmentRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final NotificationService notificationService;
    private static final int MIN_HOURS_BEFORE_CANCEL = 24;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService, NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        InputStream is = getClass().getResourceAsStream("/data/data.txt");
        if (is == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                // format: patientName,patientEmail,doctorName,appointmentDateTime,reason
                String[] parts = line.split(",");
                Doctor doctor = doctorService.getEntityByName(parts[2].trim());
                LocalDateTime when = LocalDateTime.parse(parts[3].trim());
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


    public void create (AppointmentRequest appointmentRequest) {
        Doctor doctor = doctorService.getEntityById(appointmentRequest.getDoctorId());
        LocalDateTime when = LocalDateTime.parse(appointmentRequest.getAppointmentDate());
        Appointment appointment =  new Appointment(appointmentRequest.getPatientName(),
                appointmentRequest.getPatientEmail(),
                doctor,
                when,
                appointmentRequest.getReason());
        appointmentRepository.save(appointment);
        notificationService.sendNotification(appointment.getPatientEmail(),
                "Programarea ta a fost solicitata pentru " + when + ".");
    }

    public void updateStatus(Long id, String newStatusStr) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita: " + id));

        AppointmentStatus newStatus = AppointmentStatus.valueOf(newStatusStr);
        if (!appointment.getStatus().getNextStatuses().contains(newStatus)) {
            throw new RuntimeException("Tranzitie nepermisa: "
                    + appointment.getStatus() + " -> " + newStatus);
        }
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        notificationService.sendNotification(appointment.getPatientEmail(),
                "Starea programarii tale a fost actualizata: " + newStatus.name());
    }

    /** Anulare de catre pacient: doar din stare anulabila SI cu minim X ore inainte. */
    public void cancelByPatient(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita: " + id));

        if (!appointment.getStatus().canBeCanceled()) {
            throw new RuntimeException("Programarea nu mai poate fi anulata (stare: "
                    + appointment.getStatus() + ").");
        }
        if (!hasEnoughTimeToCancel(appointment.getAppointmentDate())) {
            throw new RuntimeException("Anularea e permisa cu minim "
                    + MIN_HOURS_BEFORE_CANCEL + " ore inainte de programare.");
        }
        appointment.setStatus(AppointmentStatus.ANULATA);
        appointmentRepository.save(appointment);
        notificationService.sendNotification(appointment.getPatientEmail(),
                "Programarea ta a fost anulata.");
    }

    private boolean hasEnoughTimeToCancel(LocalDateTime appointmentDateTime) {
        return LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_CANCEL)
                .isBefore(appointmentDateTime);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        boolean cancellable = appointment.getStatus().canBeCanceled()
                && hasEnoughTimeToCancel(appointment.getAppointmentDate());
        Doctor doctor = appointment.getDoctor();
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientName(),
                appointment.getPatientEmail(),
                doctor != null ? doctor.getName() : null,
                doctor != null ? doctor.getSpecialization() : null,
                appointment.getAppointmentDate(),
                appointment.getReason(),
                appointment.getStatus().name(),
                cancellable
        );
    }
}

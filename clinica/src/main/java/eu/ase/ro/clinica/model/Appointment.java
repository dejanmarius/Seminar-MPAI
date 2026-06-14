package eu.ase.ro.clinica.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Programare facuta de un pacient la un medic.
 * Are data+ora (pentru constrangerea de anulare cu X ore inainte) si un motiv obligatoriu.
 */
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private String patientEmail;
    private String doctorName;          // numele medicului ales din catalog
    private LocalDateTime appointmentDateTime;
    private String reason;             // motiv obligatoriu la creare

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    public Appointment() {}

    public Appointment(String patientName, String patientEmail, String doctorName,
                       LocalDateTime appointmentDateTime, String reason) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.doctorName = doctorName;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = AppointmentStatus.SOLICITATA;
    }

    public Long getId() { return id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}

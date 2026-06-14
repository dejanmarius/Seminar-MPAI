package eu.ase.ro.clinica.dto.response;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private String patientName;
    private String patientEmail;
    private String doctorName;
    private LocalDateTime appointmentDateTime;
    private String reason;
    private String status;            // String, nu enum (nu expune modelul in view)
    private boolean cancellable;      // pacientul poate anula? (stare + X ore inainte)

    public AppointmentResponse() {}

    public AppointmentResponse(Long id, String patientName, String patientEmail,
                               String doctorName, LocalDateTime appointmentDateTime,
                               String reason, String status, boolean cancellable) {
        this.id = id;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.doctorName = doctorName;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = status;
        this.cancellable = cancellable;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isCancellable() { return cancellable; }
    public void setCancellable(boolean cancellable) { this.cancellable = cancellable; }
}

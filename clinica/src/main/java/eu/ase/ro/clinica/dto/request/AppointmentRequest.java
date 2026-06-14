package eu.ase.ro.clinica.dto.request;

// Campurile corespund input-urilor din form (name="...")
public class AppointmentRequest {

    private String patientName;
    private String patientEmail;
    private Long doctorId;              // id-ul medicului ales din dropdown
    private String appointmentDateTime; // string ISO din <input type="datetime-local">
    private String reason;

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

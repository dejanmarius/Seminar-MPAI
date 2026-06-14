package eu.ase.ro.clinica.dto.request;

// Campurile corespund input-urilor din form (name="...")
public class AppointmentRequest {

    private String patientName;
    private String patientEmail;
    private String doctorName;
    private String appointmentDateTime; // string ISO din <input type="datetime-local">
    private String reason;

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

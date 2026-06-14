package eu.ase.ro.schelet.dto.request;

// TODO: rename clasa -> OrderRequest / AppointmentRequest / RentalRequest
// Campurile trebuie sa corespunda cu input-urile din form (name="...")
public class ItemRequest {

    // TODO: adapteaza campurile la subiect
    // S1: clientName, clientEmail, description
    // S2: patientName, patientEmail, doctorName, reason
    // S3: userName, userEmail, equipmentName, purpose
    private String ownerName;
    private String ownerEmail;
    private String description;

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

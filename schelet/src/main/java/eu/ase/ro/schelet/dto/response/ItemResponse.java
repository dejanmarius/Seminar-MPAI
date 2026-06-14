package eu.ase.ro.schelet.dto.response;

import java.time.LocalDate;

// TODO: rename clasa -> OrderResponse / AppointmentResponse / RentalResponse
// status e String (nu enum) - pentru a nu expune modelul in view
public class ItemResponse {

    private Long id;
    private String ownerName;    // TODO: rename
    private String ownerEmail;   // TODO: rename
    private String description;  // TODO: rename
    private LocalDate date;      // TODO: rename
    private String status;       // String intentionat (nu enum)

    public ItemResponse() {}

    public ItemResponse(Long id, String ownerName, String ownerEmail,
                        String description, LocalDate date, String status) {
        this.id = id;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

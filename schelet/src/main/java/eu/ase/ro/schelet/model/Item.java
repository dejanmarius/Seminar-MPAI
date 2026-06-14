package eu.ase.ro.schelet.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// TODO: rename clasa
// S1: Order      | S2: Appointment  | S3: RentalRequest
// TODO: rename @Table
// S1: "orders"   | S2: "appointments" | S3: "rental_requests"
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: rename si adapteaza campurile
    // S1: clientName, clientEmail, description, orderDate
    // S2: patientName, patientEmail, doctorName, appointmentDate, reason
    // S3: userName, userEmail, equipmentName, startDate, endDate, purpose
    private String ownerName;    // clientName / patientName / userName
    private String ownerEmail;   // clientEmail / patientEmail / userEmail
    private String description;  // description / reason / purpose
    private LocalDate date;      // orderDate / appointmentDate / startDate

    @Enumerated(EnumType.STRING) // Salveaza "PLASATA" in loc de 0,1,2...
    private ItemStatus status;   // TODO: rename tipul la ItemStatus -> OrderStatus etc.

    // Constructor fara argumente - OBLIGATORIU pentru JPA
    public Item() {}

    // Constructor pentru creare noua (status = STARE_INITIALA automat)
    public Item(String ownerName, String ownerEmail, String description, LocalDate date) {
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.description = description;
        this.date = date;
        this.status = ItemStatus.STARE_INITIALA; // TODO: replace cu prima stare
    }

    public Long getId() { return id; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
}

package eu.ase.ro.inchirieri.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Cerere de inchiriere facuta de un utilizator pentru un echipament,
 * pe o perioada (start - end), cu un scop. Are flux de stare.
 */
@Entity
@Table(name = "rental_requests")
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;
    private Long equipmentId;       // referinta la echipamentul cerut
    private String equipmentName;   // denormalizat pentru afisare usoara in liste
    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;         // scopul inchirierii

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    public RentalRequest() {}

    public RentalRequest(String userName, String userEmail, Long equipmentId,
                         String equipmentName, LocalDate startDate, LocalDate endDate,
                         String purpose) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.purpose = purpose;
        this.status = RentalStatus.CERUTA;
    }

    public Long getId() { return id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }
}

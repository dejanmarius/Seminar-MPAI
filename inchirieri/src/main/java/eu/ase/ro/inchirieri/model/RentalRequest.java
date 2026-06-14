package eu.ase.ro.inchirieri.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rental_requests")
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    public RentalRequest() {}

    public RentalRequest(String userName, String userEmail, Equipment equipment,
                         LocalDate startDate, LocalDate endDate, String purpose) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.equipment = equipment;
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

    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }
}

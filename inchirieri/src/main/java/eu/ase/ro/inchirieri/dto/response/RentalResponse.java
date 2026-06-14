package eu.ase.ro.inchirieri.dto.response;

import java.time.LocalDate;

public class RentalResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private String equipmentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;
    private String status;          // String, nu enum
    private boolean cancellable;    // utilizatorul poate anula? (doar din CERUTA)

    public RentalResponse() {}

    public RentalResponse(Long id, String userName, String userEmail, String equipmentName,
                          LocalDate startDate, LocalDate endDate, String purpose,
                          String status, boolean cancellable) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.equipmentName = equipmentName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.purpose = purpose;
        this.status = status;
        this.cancellable = cancellable;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isCancellable() { return cancellable; }
    public void setCancellable(boolean cancellable) { this.cancellable = cancellable; }
}

package eu.ase.ro.inchirieri.dto.request;

// Date din formular -> service. Campurile corespund input-urilor (name="...")
public class RentalRequestDto {

    private String userName;
    private String userEmail;
    private Long equipmentId;       // id-ul echipamentului ales din dropdown
    private String startDate;       // string ISO din <input type="date">
    private String endDate;
    private String purpose;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
}

package eu.ase.ro.inchirieri.dto.request;

public class EquipmentRequest {

    private String name;
    private String description;
    private boolean available;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

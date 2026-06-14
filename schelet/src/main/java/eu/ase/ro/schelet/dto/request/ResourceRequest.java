package eu.ase.ro.schelet.dto.request;

// TODO: rename -> DoctorRequest / EquipmentRequest / ProductRequest
// Campurile trebuie sa corespunda cu input-urile din form (name="...")
public class ResourceRequest {

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

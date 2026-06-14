package eu.ase.ro.schelet.dto.response;

// TODO: rename -> DoctorResponse / EquipmentResponse / ProductResponse
public class ResourceResponse {

    private Long id;
    private String name;
    private String description;
    private boolean available;

    public ResourceResponse() {}

    public ResourceResponse(Long id, String name, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

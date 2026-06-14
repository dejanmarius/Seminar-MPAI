package eu.ase.ro.inchirieri.model;

import jakarta.persistence.*;

/**
 * Echipament din catalogul gestionat de admin (CRUD complet).
 * Campul {@code available} se actualizeaza la aprobare (false) si la returnare/respingere (true).
 */
@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean available;

    public Equipment() {}

    public Equipment(String name, String description, boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

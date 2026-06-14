package eu.ase.ro.skeleton.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SubItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reprezinta Echipament / Medic / Client, in functie de varianta de examen
    private String name;
    private String details;

    public SubItem() {
    }

    public SubItem(String name, String details) {
        this.name = name;
        this.details = details;
    }
}

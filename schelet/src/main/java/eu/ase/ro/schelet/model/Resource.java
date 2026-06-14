package eu.ase.ro.schelet.model;

import jakarta.persistence.*;

/**
 * Entitate secundara gestionata DOAR de admin (CRUD complet: creare, editare,
 * stergere). Reprezinta catalogul de resurse alese de utilizator la creare.
 *
 * TODO: rename clasa in functie de subiect:
 *   S1 Magazin    -> Product   (nume, pret)               [optional la S1]
 *   S2 Clinica    -> Doctor    (nume = doctorName, specializare = description)
 *   S3 Inchirieri -> Equipment (nume = equipmentName, disponibil)  [OBLIGATORIU]
 *
 * TODO: rename @Table ("doctors" / "equipments" / "products")
 */
@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // doctorName / equipmentName / productName
    private String description;  // specializare / descriere echipament
    private boolean available;   // disponibilitate (folosit mai ales la S3 Equipment)

    public Resource() {}

    public Resource(String name, String description, boolean available) {
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

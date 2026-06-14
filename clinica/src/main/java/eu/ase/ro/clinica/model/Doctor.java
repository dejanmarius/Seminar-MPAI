package eu.ase.ro.clinica.model;

import jakarta.persistence.*;

/**
 * Medic din catalogul gestionat de admin (CRUD complet).
 * Pacientul alege un medic cand creeaza o programare.
 */
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // numele medicului
    private String specialization;  // specializarea

    public Doctor() {}

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}

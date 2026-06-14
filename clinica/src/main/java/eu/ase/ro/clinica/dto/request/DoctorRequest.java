package eu.ase.ro.clinica.dto.request;

public class DoctorRequest {

    private String name;
    private String specialization;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}

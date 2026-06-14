package eu.ase.ro.inchirieri.repository;

import eu.ase.ro.inchirieri.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // Echipamentele disponibile (pentru dropdown la cerere noua)
    List<Equipment> findByAvailableTrue();
}

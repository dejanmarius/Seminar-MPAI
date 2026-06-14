package eu.ase.ro.schelet.repository;

import eu.ase.ro.schelet.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO: rename -> DoctorRepository / EquipmentRepository / ProductRepository
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // Doar resursele disponibile (util la S3 pentru lista de echipamente libere)
    List<Resource> findByAvailableTrue();
}

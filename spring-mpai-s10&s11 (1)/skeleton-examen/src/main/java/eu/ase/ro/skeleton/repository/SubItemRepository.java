package eu.ase.ro.skeleton.repository;

import eu.ase.ro.skeleton.model.SubItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubItemRepository extends JpaRepository<SubItem, Long> {
}

package eu.ase.ro.schelet.repository;

import eu.ase.ro.schelet.model.Item;
import eu.ase.ro.schelet.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// TODO: rename interfata si tipurile generice -> OrderRepository<Order, Long> etc.
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Filtrare combinata - parametri optionali (null = ignora filtrul).
    // ATENTIE: numele campurilor din JPQL (o.status, o.ownerName, o.date)
    //          trebuie sa corespunda EXACT cu numele campurilor din entitate.
    @Query("SELECT o FROM Item o WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:ownerName IS NULL OR LOWER(o.ownerName) LIKE LOWER(CONCAT('%', :ownerName, '%'))) AND " +
           "(:date IS NULL OR o.date = :date)")
    List<Item> findByFilters(@Param("status") ItemStatus status,
                             @Param("ownerName") String ownerName,
                             @Param("date") LocalDate date);
}

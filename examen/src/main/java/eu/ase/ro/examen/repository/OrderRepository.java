package eu.ase.ro.examen.repository;

import eu.ase.ro.examen.model.Order;
import eu.ase.ro.examen.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:clientName IS NULL OR LOWER(o.clientName) LIKE LOWER(CONCAT('%', :clientName, '%'))) AND " +
            "(:orderDate IS NULL OR o.orderDate = :orderDate)")
    List<Order> findByFilters(@Param("status") OrderStatus status,
                              @Param("clientName") String clientName,
                              @Param("orderDate") LocalDate orderDate);
}

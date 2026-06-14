package eu.ase.ro.inchirieri.repository;

import eu.ase.ro.inchirieri.model.RentalRequest;
import eu.ase.ro.inchirieri.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    // Filtrare combinata: stare + utilizator + echipament + data (toate optionale).
    // Pentru data, cautam cererile active in ziua respectiva (start <= data <= end).
    @Query("SELECT o FROM RentalRequest o WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:userName IS NULL OR LOWER(o.userName) LIKE LOWER(CONCAT('%', :userName, '%'))) AND " +
           "(:equipmentName IS NULL OR LOWER(o.equipmentName) LIKE LOWER(CONCAT('%', :equipmentName, '%'))) AND " +
           "(:date IS NULL OR (o.startDate <= :date AND o.endDate >= :date))")
    List<RentalRequest> findByFilters(@Param("status") RentalStatus status,
                                      @Param("userName") String userName,
                                      @Param("equipmentName") String equipmentName,
                                      @Param("date") LocalDate date);
}

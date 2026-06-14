package eu.ase.ro.clinica.repository;

import eu.ase.ro.clinica.model.Appointment;
import eu.ase.ro.clinica.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Filtrare combinata: stare + medic + pacient + data (toate optionale).
    // CAST(o.appointmentDateTime AS date) compara doar partea de data, nu si ora.
    @Query("SELECT o FROM Appointment o WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:doctorName IS NULL OR LOWER(o.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))) AND " +
           "(:patientName IS NULL OR LOWER(o.patientName) LIKE LOWER(CONCAT('%', :patientName, '%'))) AND " +
           "(:date IS NULL OR CAST(o.appointmentDateTime AS date) = :date)")
    List<Appointment> findByFilters(@Param("status") AppointmentStatus status,
                                    @Param("doctorName") String doctorName,
                                    @Param("patientName") String patientName,
                                    @Param("date") LocalDate date);
}

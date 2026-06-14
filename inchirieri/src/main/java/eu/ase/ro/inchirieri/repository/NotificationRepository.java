package eu.ase.ro.inchirieri.repository;

import eu.ase.ro.inchirieri.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

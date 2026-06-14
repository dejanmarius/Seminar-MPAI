package eu.ase.ro.schelet.repository;

import eu.ase.ro.schelet.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Notification repository - identic pentru orice subiect
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

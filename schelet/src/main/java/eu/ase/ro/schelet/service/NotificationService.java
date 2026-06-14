package eu.ase.ro.schelet.service;

import eu.ase.ro.schelet.model.Notification;
import eu.ase.ro.schelet.repository.NotificationRepository;
import org.springframework.stereotype.Service;

/**
 * Trimite notificari catre utilizatori la fiecare schimbare de stare.
 * Notificarile sunt persistate in baza de date si afisate in consola.
 * Identic pentru orice subiect - nu trebuie modificat.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void send(String recipientEmail, String message) {
        Notification notification = new Notification(recipientEmail, message);
        notificationRepository.save(notification);
        System.out.println("NOTIFICARE -> " + recipientEmail + ": " + message);
    }
}

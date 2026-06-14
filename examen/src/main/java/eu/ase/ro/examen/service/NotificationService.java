package eu.ase.ro.examen.service;

import eu.ase.ro.examen.model.Notification;
import eu.ase.ro.examen.repository.NotificationRepository;
import org.springframework.stereotype.Service;

/**
 * Trimite notificari catre clienti la fiecare schimbare de stare a comenzii.
 * Notificarile sunt persistate in baza de date si afisate in consola.
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
        System.out.println("NOTIFICARE → " + recipientEmail + ": " + message);
    }
}

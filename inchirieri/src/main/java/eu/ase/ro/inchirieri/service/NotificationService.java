package eu.ase.ro.inchirieri.service;

import eu.ase.ro.inchirieri.model.Notification;
import eu.ase.ro.inchirieri.repository.NotificationRepository;
import org.springframework.stereotype.Service;

/** Trimite notificari utilizatorilor la fiecare schimbare de stare. */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void send(String recipientEmail, String message) {
        notificationRepository.save(new Notification(recipientEmail, message));
        System.out.println("NOTIFICARE -> " + recipientEmail + ": " + message);
    }
}

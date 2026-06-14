package eu.ase.ro.clinica.service;

import eu.ase.ro.clinica.model.Notification;
import eu.ase.ro.clinica.repository.NotificationRepository;
import org.springframework.stereotype.Service;

/** Trimite notificari pacientilor la fiecare schimbare de stare. */
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

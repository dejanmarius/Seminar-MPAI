package eu.ase.ro.inchirieri.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientEmail;
    private String message;
    private LocalDateTime sentAt;

    public Notification() {}

    public Notification(String recipientEmail, String message) {
        this.recipientEmail = recipientEmail;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}

package aor.proj3t.entities;

import java.time.Instant;

import aor.proj3t.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a notification sent to users in the system.
 * Notifications are system-generated messages that inform users about
 * various events, activities, or status changes within the application.
 * 
 * @author Jorge Osório
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "notifications")
public class Notification {

    /**
     * Unique identifier for the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The content/message of the notification.
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * The type/category of the notification.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    /**
     * Flag indicating whether the notification has been read.
     */
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    /**
     * Timestamp when the notification was sent.
     */
    @Column(name = "timestamp_sent", nullable = false, updatable = false)
    private Instant timestampSent;

    /**
     * Timestamp when the notification was read by the recipient.
     */
    @Column(name = "timestamp_read")
    private Instant timestampRead;

    /**
     * The user who triggered/sent the notification.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * The user who received the notification.
     */
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    /**
     * JPA lifecycle callback to set sent timestamp.
     */
    @PrePersist
    protected void onCreate() {
        if (this.timestampSent == null) {
            this.timestampSent = Instant.now();
        }
    }

    /**
     * Gets the unique identifier of this notification.
     * 
     * @return the notification ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this notification.
     * 
     * @param id the notification ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the content of this notification.
     * 
     * @return the notification content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of this notification.
     * 
     * @param content the notification content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the type of this notification.
     * 
     * @return the notification type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Sets the type of this notification.
     * 
     * @param type the notification type
     */
    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * Checks if this notification has been read.
     * 
     * @return true if the notification has been read, false otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the read status of this notification.
     * 
     * @param read the read status
     */
    public void setRead(boolean read) {
        isRead = read;
    }

    /**
     * Gets the timestamp when this notification was sent.
     * 
     * @return the sent timestamp
     */
    public Instant getTimestampSent() {
        return timestampSent;
    }

    /**
     * Sets the timestamp when this notification was sent.
     * 
     * @param timestampSent the sent timestamp
     */
    public void setTimestampSent(Instant timestampSent) {
        this.timestampSent = timestampSent;
    }

    /**
     * Gets the timestamp when this notification was read.
     * 
     * @return the read timestamp
     */
    public Instant getTimestampRead() {
        return timestampRead;
    }

    /**
     * Sets the timestamp when this notification was read.
     * 
     * @param timestampRead the read timestamp
     */
    public void setTimestampRead(Instant timestampRead) {
        this.timestampRead = timestampRead;
    }

    /**
     * Gets the user who sent this notification.
     * 
     * @return the sender user
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the user who sent this notification.
     * 
     * @param sender the sender user
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Gets the user who received this notification.
     * 
     * @return the recipient user
     */
    public User getRecipient() {
        return recipient;
    }

    /**
     * Sets the user who received this notification.
     * 
     * @param recipient the recipient user
     */
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}

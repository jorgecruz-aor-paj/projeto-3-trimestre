package aor.proj3t.dto;

import aor.proj3t.enums.NotificationType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code Notification} entity.
 * <p>
 * Represents system or user-triggered messages exchanged between users,
 * such as alerts, requests, or announcements. Designed for use in messaging
 * or notification APIs, dashboards, and audit tracking. JAXB annotations
 * support XML serialization for compatible endpoints.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the notification
 * - {@code content}: Message body displayed to recipient
 * - {@code type}: Type/category of notification (e.g. INFO, REQUEST, ALERT)
 * - {@code isRead}: Flag indicating whether the recipient has opened the notification
 * - {@code timestampSent}: Time the notification was created/sent
 * - {@code timestampRead}: Time the notification was marked as read
 * - {@code senderId}: ID of the user/system that sent the notification
 * - {@code recipientId}: ID of the user receiving the notification
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "notification")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationDTO {

    /** Unique identifier for the notification */
    @XmlElement(name = "id")
    private Long id;

    /** The textual content or message of the notification */
    @XmlElement(name = "content")
    private String content;

    /** Type of the notification (e.g., INFO, WARNING, ALERT) */
    @XmlElement(name = "type")
    private NotificationType type;

    /** Indicates if the notification has been read by the recipient */
    @XmlElement(name = "read")
    private boolean isRead;

    /** Timestamp when the notification was sent or created */
    @XmlElement(name = "timestampSent")
    private Instant timestampSent;

    /** Timestamp when the notification was opened or marked as read */
    @XmlElement(name = "timestampRead")
    private Instant timestampRead;

    /** ID of the user (or system) who sent the notification */
    @XmlElement(name = "senderId")
    private Long senderId;

    /** ID of the recipient who receives the notification */
    @XmlElement(name = "recipientId")
    private Long recipientId;

    public NotificationDTO() {}

    public NotificationDTO(Long id, String content, NotificationType type, boolean isRead,
                           Instant timestampSent, Instant timestampRead,
                           Long senderId, Long recipientId) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.timestampSent = timestampSent;
        this.timestampRead = timestampRead;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Instant getTimestampSent() {
        return timestampSent;
    }

    public void setTimestampSent(Instant timestampSent) {
        this.timestampSent = timestampSent;
    }

    public Instant getTimestampRead() {
        return timestampRead;
    }

    public void setTimestampRead(Instant timestampRead) {
        this.timestampRead = timestampRead;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}

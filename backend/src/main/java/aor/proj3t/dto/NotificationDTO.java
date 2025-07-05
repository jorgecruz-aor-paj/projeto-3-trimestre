package aor.proj3t.dto;

import aor.proj3t.enums.NotificationType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for Notification entity
 */
@XmlRootElement(name = "notification")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "content")
    private String content;
    @XmlElement(name = "type")
    private NotificationType type;
    @XmlElement(name = "read")
    private boolean isRead;
    @XmlElement(name = "timestampSent")
    private Instant timestampSent;
    @XmlElement(name = "timestampRead")
    private Instant timestampRead;
    @XmlElement(name = "senderId")
    private Long senderId;
    @XmlElement(name = "recipientId")
    private Long recipientId;

    public NotificationDTO() {
    }

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

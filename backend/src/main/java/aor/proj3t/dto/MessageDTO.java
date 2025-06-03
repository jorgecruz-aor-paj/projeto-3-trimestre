package aor.proj3t.dto;

import java.time.LocalDateTime;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Message entity
 */
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "content")
    private String content;
    @XmlElement(name = "timestampSent")
    private LocalDateTime timestampSent;
    @XmlElement(name = "timestampRead")
    private LocalDateTime timestampRead;
    @XmlElement(name = "read")
    private boolean isRead;
    @XmlElement(name = "senderId")
    private Long senderId;
    @XmlElement(name = "recipientId")
    private Long recipientId;

    public MessageDTO() {
    }

    public MessageDTO(Long id, String content, LocalDateTime timestampSent, LocalDateTime timestampRead,
            boolean isRead, Long senderId, Long recipientId) {
        this.id = id;
        this.content = content;
        this.timestampSent = timestampSent;
        this.timestampRead = timestampRead;
        this.isRead = isRead;
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

    public LocalDateTime getTimestampSent() {
        return timestampSent;
    }

    public void setTimestampSent(LocalDateTime timestampSent) {
        this.timestampSent = timestampSent;
    }

    public LocalDateTime getTimestampRead() {
        return timestampRead;
    }

    public void setTimestampRead(LocalDateTime timestampRead) {
        this.timestampRead = timestampRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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

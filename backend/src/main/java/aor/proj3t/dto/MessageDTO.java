package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;
/**
 * Data Transfer Object for {@code Message} entity.
 * <p>
 * Encapsulates core messaging attributes for transport between services and APIs.
 * This DTO supports read tracking, sender/recipient metadata, and timestamps
 * to help manage in-app communication flows. JAXB annotations enable XML-based
 * serialization for compatible REST endpoints or external integrations.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the message
 * - {@code content}: Actual message text body
 * - {@code timestampSent}: When the message was sent by the sender
 * - {@code timestampRead}: When the recipient read the message, if applicable
 * - {@code isRead}: Flag indicating whether the message has been marked as read
 * - {@code senderId}: ID of the user who sent the message
 * - {@code recipientId}: ID of the user to whom the message was sent
 *
 * Used primarily in messaging services, user inbox views, and notification workflows.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageDTO {

    /** Unique identifier for the message */
    @XmlElement(name = "id")
    private Long id;

    /** Body content of the message */
    @XmlElement(name = "content")
    private String content;

    /** Timestamp marking when the message was sent */
    @XmlElement(name = "timestampSent")
    private Instant timestampSent;

    /** Timestamp marking when the message was read (if available) */
    @XmlElement(name = "timestampRead")
    private Instant timestampRead;

    /** Indicates whether the message has been read */
    @XmlElement(name = "read")
    private boolean isRead;

    /** ID of the user who sent the message */
    @XmlElement(name = "senderId")
    private Long senderId;

    /** ID of the user who received the message */
    @XmlElement(name = "recipientId")
    private Long recipientId;

    public MessageDTO() {}

    public MessageDTO(Long id, String content, Instant timestampSent, Instant timestampRead,
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

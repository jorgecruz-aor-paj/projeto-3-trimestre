package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing a message sent between users in the system.
 * Messages support direct communication between users with read status tracking
 * and timestamp information for both sending and reading.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "messages")
public class Message {

    /**
     * Unique identifier for the message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The content/text of the message.
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * Timestamp when the message was sent.
     */
    @Column(name = "timestamp_sent", nullable = false, updatable = false)
    private Instant timestampSent;

    /**
     * Timestamp when the message was read by the recipient.
     */
    @Column(name = "timestamp_read")
    private Instant timestampRead;

    /**
     * Flag indicating whether the message has been read.
     */
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    /**
     * The user who sent the message.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * The user who received the message.
     */
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    /**
     * Gets the unique identifier of this message.
     * 
     * @return the message ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this message.
     * 
     * @param id the message ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the content of this message.
     * 
     * @return the message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of this message.
     * 
     * @param content the message content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the timestamp when this message was sent.
     * 
     * @return the sent timestamp
     */
    public Instant getTimestampSent() {
        return timestampSent;
    }

    /**
     * Sets the timestamp when this message was sent.
     * 
     * @param timestampSent the sent timestamp
     */
    public void setTimestampSent(Instant timestampSent) {
        this.timestampSent = timestampSent;
    }

    /**
     * Gets the timestamp when this message was read.
     * 
     * @return the read timestamp
     */
    public Instant getTimestampRead() {
        return timestampRead;
    }

    /**
     * Sets the timestamp when this message was read.
     * 
     * @param timestampRead the read timestamp
     */
    public void setTimestampRead(Instant timestampRead) {
        this.timestampRead = timestampRead;
    }

    /**
     * Checks if this message has been read.
     * 
     * @return true if the message has been read, false otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the read status of this message.
     * 
     * @param read the read status
     */
    public void setRead(boolean read) {
        isRead = read;
    }

    /**
     * Gets the user who sent this message.
     * 
     * @return the sender user
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the user who sent this message.
     * 
     * @param sender the sender user
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Gets the user who received this message.
     * 
     * @return the recipient user
     */
    public User getRecipient() {
        return recipient;
    }

    /**
     * Sets the user who received this message.
     * 
     * @param recipient the recipient user
     */
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}

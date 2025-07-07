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

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "timestamp_sent", nullable = false, updatable = false)
    private Instant timestampSent;

    @Column(name = "timestamp_read")
    private Instant timestampRead;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @PrePersist
    protected void onCreate() {
        if (this.timestampSent == null) {
            this.timestampSent = Instant.now();
        }
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}

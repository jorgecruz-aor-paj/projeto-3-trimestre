package aor.proj3t.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp_sent", nullable = false, updatable = false)
    private LocalDateTime timestampSent;

    @Column(name = "timestamp_read")
    private LocalDateTime timestampRead;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserEntity recipient;

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

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(UserEntity recipient) {
        this.recipient = recipient;
    }
}


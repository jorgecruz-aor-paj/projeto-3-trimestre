package aor.proj3t.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "settings")

@NamedQueries({
        @NamedQuery(name = "Settings.getLatestSettings",
                query = "SELECT s FROM Settings s ORDER BY s.createdAt DESC")
})

public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "confirmation_expiration_time", nullable = false)
    private Integer confirmationExpirationTime;

    @Column(name = "session_expiration_time", nullable = false)
    private Integer sessionExpirationTime;

    @Column(name = "reset_password_expiration_time", nullable = false)
    private Integer resetPasswordExpirationTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public Settings() {}

    public Settings(Integer confirmationExpirationTime, Integer sessionExpirationTime, Integer resetPasswordExpirationTime) {
        this.confirmationExpirationTime = confirmationExpirationTime;
        this.sessionExpirationTime = sessionExpirationTime;
        this.resetPasswordExpirationTime = resetPasswordExpirationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getConfirmationExpirationTime() {
        return confirmationExpirationTime;
    }

    public void setConfirmationExpirationTime(Integer confirmationWindow) {
        this.confirmationExpirationTime = confirmationWindow;
    }

    public Integer getSessionExpirationTime() {
        return sessionExpirationTime;
    }

    public void setSessionExpirationTime(Integer sessionWindow) {
        this.sessionExpirationTime = sessionWindow;
    }

    public Integer getResetPasswordExpirationTime() {
        return resetPasswordExpirationTime;
    }

    public void setResetPasswordExpirationTime(Integer resetPasswordWindow) {
        this.resetPasswordExpirationTime = resetPasswordWindow;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}


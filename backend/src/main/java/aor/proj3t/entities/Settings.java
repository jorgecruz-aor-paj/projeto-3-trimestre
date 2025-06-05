package aor.proj3t.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "confirmation_window", nullable = false)
    private Integer confirmationWindow;

    @Column(name = "session_window", nullable = false)
    private Integer sessionWindow;

    @Column(name = "reset_password_window", nullable = false)
    private Integer resetPasswordWindow;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getConfirmationWindow() {
        return confirmationWindow;
    }

    public void setConfirmationWindow(Integer confirmationWindow) {
        this.confirmationWindow = confirmationWindow;
    }

    public Integer getSessionWindow() {
        return sessionWindow;
    }

    public void setSessionWindow(Integer sessionWindow) {
        this.sessionWindow = sessionWindow;
    }

    public Integer getResetPasswordWindow() {
        return resetPasswordWindow;
    }

    public void setResetPasswordWindow(Integer resetPasswordWindow) {
        this.resetPasswordWindow = resetPasswordWindow;
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


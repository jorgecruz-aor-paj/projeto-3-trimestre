package aor.proj3t.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}


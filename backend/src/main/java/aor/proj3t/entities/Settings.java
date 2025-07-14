package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing system-wide configuration settings.
 * This entity stores various timeout and expiration values that control
 * the behavior of authentication, sessions, and other system operations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "settings", indexes = {
        @Index(name = "idx_settings_created_at", columnList = "created_at")
})

@NamedQueries({
        @NamedQuery(name = "Settings.getLatestSettings", query = "SELECT s FROM Settings s ORDER BY s.createdAt DESC")
})

public class Settings {

    /**
     * Unique identifier for the settings record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Confirmation token expiration time in minutes.
     */
    @Column(name = "confirmation_expiration_time", nullable = false)
    private Integer confirmationExpirationTime;

    /**
     * Session expiration time in minutes.
     */
    @Column(name = "session_expiration_time", nullable = false)
    private Integer sessionExpirationTime;

    /**
     * Password reset token expiration time in minutes.
     */
    @Column(name = "reset_password_expiration_time", nullable = false)
    private Integer resetPasswordExpirationTime;

    /**
     * Timestamp when these settings were created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * JPA lifecycle callback to set creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    /**
     * The user who created these settings.
     */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * Default constructor for JPA.
     */
    public Settings() {
    }

    /**
     * Constructor to create settings with expiration times.
     * 
     * @param confirmationExpirationTime  confirmation token expiration time in
     *                                    minutes
     * @param sessionExpirationTime       session expiration time in minutes
     * @param resetPasswordExpirationTime password reset token expiration time in
     *                                    minutes
     */
    public Settings(Integer confirmationExpirationTime, Integer sessionExpirationTime,
            Integer resetPasswordExpirationTime) {
        this.confirmationExpirationTime = confirmationExpirationTime;
        this.sessionExpirationTime = sessionExpirationTime;
        this.resetPasswordExpirationTime = resetPasswordExpirationTime;
    }

    /**
     * Gets the unique identifier of these settings.
     * 
     * @return the settings ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of these settings.
     * 
     * @param id the settings ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the confirmation token expiration time.
     * 
     * @return the confirmation expiration time in minutes
     */
    public Integer getConfirmationExpirationTime() {
        return confirmationExpirationTime;
    }

    /**
     * Sets the confirmation token expiration time.
     * 
     * @param confirmationWindow the confirmation expiration time in minutes
     */
    public void setConfirmationExpirationTime(Integer confirmationWindow) {
        this.confirmationExpirationTime = confirmationWindow;
    }

    /**
     * Gets the session expiration time.
     * 
     * @return the session expiration time in minutes
     */
    public Integer getSessionExpirationTime() {
        return sessionExpirationTime;
    }

    /**
     * Sets the session expiration time.
     * 
     * @param sessionWindow the session expiration time in minutes
     */
    public void setSessionExpirationTime(Integer sessionWindow) {
        this.sessionExpirationTime = sessionWindow;
    }

    /**
     * Gets the password reset token expiration time.
     * 
     * @return the password reset expiration time in minutes
     */
    public Integer getResetPasswordExpirationTime() {
        return resetPasswordExpirationTime;
    }

    /**
     * Sets the password reset token expiration time.
     * 
     * @param resetPasswordWindow the password reset expiration time in minutes
     */
    public void setResetPasswordExpirationTime(Integer resetPasswordWindow) {
        this.resetPasswordExpirationTime = resetPasswordWindow;
    }

    /**
     * Gets the creation timestamp of these settings.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of these settings.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the user who created these settings.
     * 
     * @return the creator user
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created these settings.
     * 
     * @param createdBy the creator user
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}

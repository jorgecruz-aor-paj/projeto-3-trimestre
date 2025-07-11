package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a password reset request for a user.
 * This entity stores temporary password reset tokens with expiration dates
 * to securely handle password reset operations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "reset_password")
public class ResetPassword {

    /**
     * Unique identifier for the password reset request.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when the password reset request was created.
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
     * Expiration date for the password reset token.
     */
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    /**
     * Unique token for password reset verification.
     */
    @Column(name = "reset_password_token", nullable = false, unique = true)
    private String resetPasswordToken;

    /**
     * The user requesting the password reset.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Gets the unique identifier of this password reset request.
     * 
     * @return the reset request ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this password reset request.
     * 
     * @param id the reset request ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the creation timestamp of this password reset request.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this password reset request.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the expiration date of the reset token.
     * 
     * @return the expiration date
     */
    public Instant getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the reset token.
     * 
     * @param expirationDate the expiration date
     */
    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the password reset token.
     * 
     * @return the reset token
     */
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    /**
     * Sets the password reset token.
     * 
     * @param resetPasswordToken the reset token
     */
    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    /**
     * Gets the user requesting the password reset.
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user requesting the password reset.
     * 
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }
}

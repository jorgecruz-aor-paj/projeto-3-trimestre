package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a pending user registration that awaits confirmation.
 * This entity stores temporary registration data including confirmation tokens
 * and expiration dates for email verification during the user registration
 * process.
 * 
 * @author Jorge Osório
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "pending_registration")

public class PendingRegistration {

    /**
     * Unique identifier for the pending registration.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    /**
     * Email address of the user attempting to register.
     */
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    /**
     * Hashed password for the pending user account.
     */
    @Column(name = "password", nullable = false, length = 800)
    private String password;

    /**
     * Timestamp when the registration request was created.
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
     * Confirmation token sent to the user's email for verification.
     */
    @Column(name = "confirmation_token", nullable = false, length = 800)
    private String confirmationToken;

    /**
     * Expiration date for the confirmation token.
     */
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    /**
     * Default constructor for JPA.
     */
    public PendingRegistration() {
    }

    /**
     * Constructor to create a pending registration with email and password.
     * 
     * @param email    the user's email address
     * @param password the user's hashed password
     */
    public PendingRegistration(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the unique identifier of this pending registration.
     * 
     * @return the registration ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this pending registration.
     * 
     * @param id the registration ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the email address of the pending registration.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the pending registration.
     * 
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the hashed password of the pending registration.
     * 
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the hashed password of the pending registration.
     * 
     * @param password the hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the creation timestamp of this pending registration.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this pending registration.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the confirmation token for email verification.
     * 
     * @return the confirmation token
     */
    public String getConfirmationToken() {
        return confirmationToken;
    }

    /**
     * Sets the confirmation token for email verification.
     * 
     * @param confirmationToken the confirmation token
     */
    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    /**
     * Gets the expiration date of the confirmation token.
     * 
     * @return the expiration date
     */
    public Instant getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the confirmation token.
     * 
     * @param expirationDate the expiration date
     */
    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}

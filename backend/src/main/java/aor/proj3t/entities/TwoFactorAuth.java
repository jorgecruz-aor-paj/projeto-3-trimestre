package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Entity representing Two-Factor Authentication settings for a user.
 * This entity stores TOTP secret keys and configuration for 2FA,
 * maintaining a one-to-one relationship with the User entity.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "two_factor_auth")
public class TwoFactorAuth {

    /**
     * Unique identifier for the 2FA record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user this 2FA configuration belongs to.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Base32-encoded secret key for TOTP generation.
     * This is stored encrypted in the database.
     */
    @Column(name = "secret_key", nullable = false, length = 64)
    private String secretKey;

    /**
     * Whether 2FA is enabled for this user.
     */
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false;

    /**
     * Whether the 2FA setup has been verified by the user.
     * User must scan QR code and enter a valid code to verify.
     */
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    /**
     * Backup codes for account recovery (comma-separated, encrypted).
     * These are one-time use codes that can be used if TOTP is unavailable.
     */
    @Column(name = "backup_codes", length = 500)
    private String backupCodes;

    /**
     * Number of backup codes that have been used.
     */
    @Column(name = "backup_codes_used", nullable = false)
    private Integer backupCodesUsed = 0;

    /**
     * Last time a TOTP code was successfully used.
     * Used to prevent replay attacks.
     */
    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    /**
     * Timestamp when the 2FA was first created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Timestamp when the 2FA configuration was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * JPA lifecycle callback to set creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * JPA lifecycle callback to update modification timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Default constructor for JPA.
     */
    public TwoFactorAuth() {
    }

    /**
     * Constructor to create a new 2FA configuration for a user.
     * 
     * @param user      the user this 2FA belongs to
     * @param secretKey the TOTP secret key
     */
    public TwoFactorAuth(User user, String secretKey) {
        this.user = user;
        this.secretKey = secretKey;
        this.isEnabled = false;
        this.isVerified = false;
        this.backupCodesUsed = 0;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getBackupCodes() {
        return backupCodes;
    }

    public void setBackupCodes(String backupCodes) {
        this.backupCodes = backupCodes;
    }

    public Integer getBackupCodesUsed() {
        return backupCodesUsed;
    }

    public void setBackupCodesUsed(Integer backupCodesUsed) {
        this.backupCodesUsed = backupCodesUsed;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Checks if 2FA is both enabled and verified for this user.
     * 
     * @return true if 2FA is active and verified
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(isEnabled) && Boolean.TRUE.equals(isVerified);
    }

    /**
     * Increments the count of used backup codes.
     */
    public void incrementBackupCodesUsed() {
        this.backupCodesUsed = (this.backupCodesUsed == null ? 0 : this.backupCodesUsed) + 1;
    }
}

package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code PendingRegistration} entity.
 * <p>
 * Encapsulates the essential data related to user registrations that are initiated
 * but not yet confirmed. This DTO is used in flows that involve email verification,
 * token-based confirmation, and cleanup of expired registration requests. JAXB
 * annotations support XML serialization for compatible endpoints or external systems.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the pending registration entry
 * - {@code email}: Email address used for the registration attempt
 * - {@code createdAt}: Timestamp when the registration was initiated
 * - {@code confirmationToken}: Token issued to the user for account confirmation
 * - {@code expirationDate}: Date when the token/registration expires if unused
 *
 * Use cases include:
 * - Sending confirmation emails upon registration
 * - Validating token-based access before account creation
 * - Cleaning up stale or expired registrations
 *
 * Ensure {@code email} and {@code confirmationToken} are populated before sending confirmation.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "pendingRegistration")
@XmlAccessorType(XmlAccessType.FIELD)
public class PendingRegistrationDTO {

    /** Unique identifier for the registration attempt */
    @XmlElement(name = "id")
    private long id;

    /** Email address provided by the user during registration */
    @XmlElement(name = "email")
    private String email;

    /** Timestamp of when the registration was created */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Token used to confirm the user's account */
    @XmlElement(name = "confirmationToken")
    private String confirmationToken;

    /** Expiration date for the confirmation token */
    @XmlElement(name = "expirationDate")
    private Instant expirationDate;

    public PendingRegistrationDTO() {}

    public PendingRegistrationDTO(long id, String email, Instant createdAt,
                                  String confirmationToken, Instant expirationDate) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.confirmationToken = confirmationToken;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}

package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code ResetPassword} entity.
 * <p>
 * Encapsulates metadata related to password reset requests, including the creation timestamp,
 * token, associated user, and expiration information. Typically used in flows that validate
 * token-based password recovery via email or external identity providers. JAXB annotations
 * allow this object to be serialized for XML-based APIs and clients.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the password reset entry
 * - {@code createdAt}: Timestamp when the reset request was initiated
 * - {@code expirationDate}: Time when the token becomes invalid
 * - {@code resetPasswordToken}: Randomized token to authenticate password reset
 * - {@code userId}: ID of the user requesting the password reset
 *
 * Use cases:
 * - Backend validation of password reset flows
 * - Auditing token expiration and request timestamps
 * - XML-based transport for password recovery services
 *
 * Security tip: Never expose the token or DTO publicly without proper safeguards.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "resetPassword")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResetPasswordDTO {

    /** Unique identifier of the password reset request */
    @XmlElement(name = "id")
    private Long id;

    /** Timestamp when the reset password request was created */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Expiration timestamp after which the reset token becomes invalid */
    @XmlElement(name = "expirationDate")
    private Instant expirationDate;

    /** Token used to authenticate password reset requests */
    @XmlElement(name = "resetPasswordToken")
    private String resetPasswordToken;

    /** ID of the user who requested the password reset */
    @XmlElement(name = "userId")
    private Long userId;

    public ResetPasswordDTO() {
    }

    public ResetPasswordDTO(Long id, Instant createdAt, Instant expirationDate,
            String resetPasswordToken, Long userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
        this.resetPasswordToken = resetPasswordToken;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

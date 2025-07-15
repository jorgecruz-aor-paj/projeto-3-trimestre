package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code Settings} entity.
 * <p>
 * Encapsulates system-level configuration options related to token lifecycles,
 * session durations, and password reset timeframes. Typically used in platform
 * administration modules to enforce application-wide behavioral constraints.
 * JAXB annotations support XML-based serialization for interoperability and
 * configuration syncing across services.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the settings configuration
 * - {@code confirmationWindow}: Time window in minutes for account confirmation tokens to remain valid
 * - {@code sessionWindow}: Duration in minutes for a user session to remain active
 * - {@code resetPasswordWindow}: Validity period in minutes for password reset tokens
 * - {@code createdAt}: Timestamp of when this settings record was created
 * - {@code createdById}: ID of the administrator who created this configuration
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsDTO {

    /** Unique identifier of the settings configuration */
    @XmlElement(name = "id")
    private Long id;

    /** Time window (in minutes) for confirmation token validity */
    @XmlElement(name = "confirmationWindow")
    private Integer confirmationWindow;

    /** Duration (in minutes) for an active user session */
    @XmlElement(name = "sessionWindow")
    private Integer sessionWindow;

    /** Time limit (in minutes) for reset password token usage */
    @XmlElement(name = "resetPasswordWindow")
    private Integer resetPasswordWindow;

    /** Timestamp of when these settings were created */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** ID of the admin who configured the settings */
    @XmlElement(name = "createdById")
    private Long createdById;

    public SettingsDTO() {
    }

    public SettingsDTO(Long id, Integer confirmationWindow, Integer sessionWindow,
            Integer resetPasswordWindow, Instant createdAt,
            Long createdById) {
        this.id = id;
        this.confirmationWindow = confirmationWindow;
        this.sessionWindow = sessionWindow;
        this.resetPasswordWindow = resetPasswordWindow;
        this.createdAt = createdAt;
        this.createdById = createdById;
    }

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

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }
}

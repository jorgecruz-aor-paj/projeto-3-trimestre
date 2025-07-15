package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code Session} entity.
 * <p>
 * Represents session metadata for a user's active or expired login token,
 * including lifecycle timestamps, status, and client-specific identifiers.
 * Primarily used in authentication systems, session tracking dashboards,
 * or security auditing workflows. JAXB annotations support XML serialization
 * for REST endpoints and interoperability with external systems.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the session record
 * - {@code createdAt}: Timestamp when the session was created
 * - {@code expirationDate}: When the session token is set to expire
 * - {@code sessionToken}: Token string used to maintain session authentication
 * - {@code isActive}: Flag indicating if the session is still valid
 * - {@code ipAddress}: IP address from which the session originated
 * - {@code userAgent}: Client-specific identifier (e.g., browser info, device)
 * - {@code userId}: ID of the user associated with this session
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "session")
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionDTO {

    /** Unique identifier for the session */
    @XmlElement(name = "id")
    private Long id;

    /** Timestamp marking when the session was initiated */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Expiration time for the session token */
    @XmlElement(name = "expirationDate")
    private Instant expirationDate;

    /** Token used to authenticate session requests */
    @XmlElement(name = "sessionToken")
    private String sessionToken;

    /** Indicates whether the session is currently active */
    @XmlElement(name = "active")
    private Boolean isActive;

    /** IP address from which the session originated */
    @XmlElement(name = "ipAddress")
    private String ipAddress;

    /** Client identifier string (e.g. browser or device info) */
    @XmlElement(name = "userAgent")
    private String userAgent;

    /** ID of the user linked to this session */
    @XmlElement(name = "userId")
    private Long userId;

    public SessionDTO() {
    }

    public SessionDTO(Long id, Instant createdAt, Instant expirationDate,
            String sessionToken, Boolean isActive, String ipAddress,
            String userAgent, Long userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
        this.sessionToken = sessionToken;
        this.isActive = isActive;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
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

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

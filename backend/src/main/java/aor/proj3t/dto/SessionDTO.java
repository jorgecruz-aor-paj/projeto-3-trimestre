package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for Session entity
 */
@XmlRootElement(name = "session")
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "expirationDate")
    private Instant expirationDate;
    @XmlElement(name = "sessionToken")
    private String sessionToken;
    @XmlElement(name = "active")
    private Boolean isActive;
    @XmlElement(name = "ipAddress")
    private String ipAddress;
    @XmlElement(name = "userAgent")
    private String userAgent;
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

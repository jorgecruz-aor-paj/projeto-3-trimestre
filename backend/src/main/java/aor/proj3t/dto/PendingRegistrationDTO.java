package aor.proj3t.dto;

import java.time.Instant;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for PendingRegistration entity
 */
@XmlRootElement(name = "pendingRegistration")
@XmlAccessorType(XmlAccessType.FIELD)
public class PendingRegistrationDTO {
    @XmlElement(name = "id")
    private long id;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "confirmationToken")
    private String confirmationToken;
    @XmlElement(name = "expirationDate")
    private Instant expirationDate;

    public PendingRegistrationDTO() {
    }

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

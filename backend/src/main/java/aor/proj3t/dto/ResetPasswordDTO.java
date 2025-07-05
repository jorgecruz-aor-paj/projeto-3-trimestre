package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for ResetPassword entity
 */
@XmlRootElement(name = "resetPassword")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResetPasswordDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "expirationDate")
    private Instant expirationDate;
    @XmlElement(name = "resetPasswordToken")
    private String resetPasswordToken;
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

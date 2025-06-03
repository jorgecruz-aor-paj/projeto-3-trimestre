package aor.proj3t.dto;

import java.time.LocalDateTime;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for ResetPassword entity
 */
@XmlRootElement(name = "resetPassword")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResetPasswordDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "createdAt")
    private LocalDateTime createdAt;
    @XmlElement(name = "expirationDate")
    private LocalDateTime expirationDate;
    @XmlElement(name = "resetPasswordToken")
    private String resetPasswordToken;
    @XmlElement(name = "userId")
    private Long userId;

    public ResetPasswordDTO() {
    }

    public ResetPasswordDTO(Long id, LocalDateTime createdAt, LocalDateTime expirationDate,
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
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

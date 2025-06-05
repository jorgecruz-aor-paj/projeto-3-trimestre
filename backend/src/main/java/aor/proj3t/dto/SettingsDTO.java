package aor.proj3t.dto;

import java.time.Instant;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Settings entity
 */
@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "confirmationWindow")
    private Integer confirmationWindow;
    @XmlElement(name = "sessionWindow")
    private Integer sessionWindow;
    @XmlElement(name = "resetPasswordWindow")
    private Integer resetPasswordWindow;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
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

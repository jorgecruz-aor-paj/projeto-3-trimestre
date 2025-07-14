package aor.proj3t.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for login response that indicates 2FA is required.
 * Contains a temporary token that can only be used for 2FA verification.
 * 
 * @author Jorge Cruz
 */
@XmlRootElement(name = "loginResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginResponseDTO {

    @XmlElement(name = "requiresTwoFactor")
    private boolean requiresTwoFactor;

    @XmlElement(name = "temporaryToken")
    private String temporaryToken;

    @XmlElement(name = "sessionToken")
    private String sessionToken;

    public LoginResponseDTO() {
    }

    /**
     * Constructor for successful login without 2FA
     */
    public LoginResponseDTO(String sessionToken) {
        this.requiresTwoFactor = false;
        this.sessionToken = sessionToken;
    }

    /**
     * Constructor for login that requires 2FA
     */
    public LoginResponseDTO(boolean requiresTwoFactor, String temporaryToken) {
        this.requiresTwoFactor = requiresTwoFactor;
        this.temporaryToken = temporaryToken;
    }

    public boolean isRequiresTwoFactor() {
        return requiresTwoFactor;
    }

    public void setRequiresTwoFactor(boolean requiresTwoFactor) {
        this.requiresTwoFactor = requiresTwoFactor;
    }

    public String getTemporaryToken() {
        return temporaryToken;
    }

    public void setTemporaryToken(String temporaryToken) {
        this.temporaryToken = temporaryToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

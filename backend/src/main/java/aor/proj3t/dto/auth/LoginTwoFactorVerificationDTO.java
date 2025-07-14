package aor.proj3t.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for 2FA verification during login process.
 * Contains the temporary token and the 2FA code.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 */
@XmlRootElement(name = "loginTwoFactorVerification")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginTwoFactorVerificationDTO {

    @XmlElement(name = "temporaryToken")
    private String temporaryToken;

    @XmlElement(name = "code")
    private String code;

    public LoginTwoFactorVerificationDTO() {
    }

    public LoginTwoFactorVerificationDTO(String temporaryToken, String code) {
        this.temporaryToken = temporaryToken;
        this.code = code;
    }

    public String getTemporaryToken() {
        return temporaryToken;
    }

    public void setTemporaryToken(String temporaryToken) {
        this.temporaryToken = temporaryToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

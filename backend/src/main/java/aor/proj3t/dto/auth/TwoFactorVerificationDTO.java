package aor.proj3t.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Two-Factor Authentication verification request.
 * Contains the TOTP code or backup code for verification.
 * 
 * @author Jorge Cruz
 */
@XmlRootElement(name = "twoFactorVerification")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwoFactorVerificationDTO {

    @XmlElement(name = "code")
    private String code;

    public TwoFactorVerificationDTO() {
    }

    public TwoFactorVerificationDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

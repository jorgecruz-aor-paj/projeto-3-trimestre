package aor.proj3t.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for disabling two-factor authentication request.
 * 
 * @author Jorge Cruz
 */
@XmlRootElement(name = "disableTwoFactor")
@XmlAccessorType(XmlAccessType.FIELD)
public class DisableTwoFactorDTO {

    @XmlElement(name = "password")
    private String password;

    public DisableTwoFactorDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

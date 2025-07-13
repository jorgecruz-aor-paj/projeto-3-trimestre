package aor.proj3t.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for two-factor authentication status response.
 * 
 * @author Jorge Cruz
 */
@XmlRootElement(name = "twoFactorStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwoFactorStatusResponseDTO {

    @XmlElement(name = "enabled")
    private boolean enabled;

    public TwoFactorStatusResponseDTO() {
    }

    public TwoFactorStatusResponseDTO(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

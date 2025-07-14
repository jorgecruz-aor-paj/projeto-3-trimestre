package aor.proj3t.dto.auth;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Two-Factor Authentication setup response.
 * Contains the secret key, QR code URL, and backup codes for 2FA setup.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 */
@XmlRootElement(name = "twoFactorSetup")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwoFactorSetupDTO {

    @XmlElement(name = "secretKey")
    private String secretKey;

    @XmlElement(name = "qrCodeUrl")
    private String qrCodeUrl;

    @XmlElement(name = "backupCodes")
    private List<String> backupCodes;

    public TwoFactorSetupDTO() {
    }

    public TwoFactorSetupDTO(String secretKey, String qrCodeUrl, List<String> backupCodes) {
        this.secretKey = secretKey;
        this.qrCodeUrl = qrCodeUrl;
        this.backupCodes = backupCodes;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public List<String> getBackupCodes() {
        return backupCodes;
    }

    public void setBackupCodes(List<String> backupCodes) {
        this.backupCodes = backupCodes;
    }
}

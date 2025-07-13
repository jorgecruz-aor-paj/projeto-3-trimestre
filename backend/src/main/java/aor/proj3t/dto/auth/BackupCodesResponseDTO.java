package aor.proj3t.dto.auth;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for backup codes response.
 * 
 * @author Jorge Cruz
 */
@XmlRootElement(name = "backupCodes")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackupCodesResponseDTO {

    @XmlElement(name = "backupCodes")
    private List<String> backupCodes;

    public BackupCodesResponseDTO() {
    }

    public BackupCodesResponseDTO(List<String> backupCodes) {
        this.backupCodes = backupCodes;
    }

    public List<String> getBackupCodes() {
        return backupCodes;
    }

    public void setBackupCodes(List<String> backupCodes) {
        this.backupCodes = backupCodes;
    }
}

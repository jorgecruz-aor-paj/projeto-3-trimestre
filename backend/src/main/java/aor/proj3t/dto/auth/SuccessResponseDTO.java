package aor.proj3t.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for success response messages.
 * 
 * @author Jorge Cruz
 */
@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuccessResponseDTO {

    @XmlElement(name = "message")
    private String message;

    public SuccessResponseDTO() {
    }

    public SuccessResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package aor.proj3t.dto.ai;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Chat Request
 */
@XmlRootElement(name = "chat_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "message")
    private String message;

    public ChatRequestDTO() {
    }

    public ChatRequestDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

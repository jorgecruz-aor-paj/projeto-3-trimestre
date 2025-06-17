package aor.proj3t.dto.ai;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Chat Response
 */
@XmlRootElement(name = "chat_response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "reply")
    private String reply;

    public ChatResponseDTO() {
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}

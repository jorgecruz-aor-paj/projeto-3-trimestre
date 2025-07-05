package aor.proj3t.dto.ai;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * Data Transfer Object for Feedback text
 */
@XmlRootElement(name = "feedback")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeedbackDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "text")
    private String text;

    public FeedbackDTO() {
    }

    public FeedbackDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

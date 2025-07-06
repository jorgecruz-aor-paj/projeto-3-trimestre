package aor.proj3t.dto.ai;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * Data Transfer Object for Sentiment Analysis Response
 */
@XmlRootElement(name = "sentiment_response")
@XmlAccessorType(XmlAccessType.FIELD)
public class SentimentResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "polarity")
    private float polarity;

    @XmlElement(name = "subjectivity")
    private float subjectivity;

    @XmlElement(name = "sentiment")
    private String sentiment;

    public SentimentResponseDTO() {
    }

    public float getPolarity() {
        return polarity;
    }

    public void setPolarity(float polarity) {
        this.polarity = polarity;
    }

    public float getSubjectivity() {
        return subjectivity;
    }

    public void setSubjectivity(float subjectivity) {
        this.subjectivity = subjectivity;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}

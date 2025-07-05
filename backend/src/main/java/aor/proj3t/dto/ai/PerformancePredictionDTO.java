package aor.proj3t.dto.ai;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * Data Transfer Object for Performance Prediction
 */
@XmlRootElement(name = "performance_prediction")
@XmlAccessorType(XmlAccessType.FIELD)
public class PerformancePredictionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "predicted_score")
    private float predictedScore;

    @XmlElement(name = "confidence")
    private float confidence;

    public PerformancePredictionDTO() {
    }

    public float getPredictedScore() {
        return predictedScore;
    }

    public void setPredictedScore(float predictedScore) {
        this.predictedScore = predictedScore;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }
}

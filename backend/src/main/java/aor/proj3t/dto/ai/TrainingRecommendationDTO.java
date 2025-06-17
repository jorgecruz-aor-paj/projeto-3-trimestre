package aor.proj3t.dto.ai;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Training Recommendation Response
 */
@XmlRootElement(name = "training_recommendation")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingRecommendationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "recommended_training")
    private String recommendedTraining;

    @XmlElement(name = "reason")
    private String reason;

    public TrainingRecommendationDTO() {
    }

    public String getRecommendedTraining() {
        return recommendedTraining;
    }

    public void setRecommendedTraining(String recommendedTraining) {
        this.recommendedTraining = recommendedTraining;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

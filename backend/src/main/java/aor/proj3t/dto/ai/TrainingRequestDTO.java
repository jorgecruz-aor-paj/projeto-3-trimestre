package aor.proj3t.dto.ai;

import java.io.Serializable;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Training Recommendation Request
 */
@XmlRootElement(name = "training_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "evaluations")
    private List<String> evaluations;

    @XmlElement(name = "skills")
    private List<String> skills;

    @XmlElement(name = "trainings_history")
    private List<String> trainingsHistory;

    public TrainingRequestDTO() {
    }

    public TrainingRequestDTO(List<String> evaluations, List<String> skills, List<String> trainingsHistory) {
        this.evaluations = evaluations;
        this.skills = skills;
        this.trainingsHistory = trainingsHistory;
    }

    public List<String> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<String> evaluations) {
        this.evaluations = evaluations;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getTrainingsHistory() {
        return trainingsHistory;
    }

    public void setTrainingsHistory(List<String> trainingsHistory) {
        this.trainingsHistory = trainingsHistory;
    }
}

package aor.proj3t.dto;

import java.time.LocalDateTime;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Evaluation entity
 */
@XmlRootElement(name = "evaluation")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "scale")
    private EvaluationScale scale;
    @XmlElement(name = "state")
    private EvaluationStatus state;
    @XmlElement(name = "feedback")
    private String feedback;
    @XmlElement(name = "createdAt")
    private LocalDateTime createdAt;
    @XmlElement(name = "editedAt")
    private LocalDateTime editedAt;
    @XmlElement(name = "evaluationCycleId")
    private Long evaluationCycleId;
    @XmlElement(name = "evaluateeId")
    private Long evaluateeId;
    @XmlElement(name = "evaluatorId")
    private Long evaluatorId;

    public EvaluationDTO() {
    }

    public EvaluationDTO(Long id, EvaluationScale scale, EvaluationStatus state, String feedback,
            LocalDateTime createdAt, LocalDateTime editedAt, Long evaluationCycleId,
            Long evaluateeId, Long evaluatorId) {
        this.id = id;
        this.scale = scale;
        this.state = state;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.evaluationCycleId = evaluationCycleId;
        this.evaluateeId = evaluateeId;
        this.evaluatorId = evaluatorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EvaluationScale getScale() {
        return scale;
    }

    public void setScale(EvaluationScale scale) {
        this.scale = scale;
    }

    public EvaluationStatus getState() {
        return state;
    }

    public void setState(EvaluationStatus state) {
        this.state = state;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public Long getEvaluationCycleId() {
        return evaluationCycleId;
    }

    public void setEvaluationCycleId(Long evaluationCycleId) {
        this.evaluationCycleId = evaluationCycleId;
    }

    public Long getEvaluateeId() {
        return evaluateeId;
    }

    public void setEvaluateeId(Long evaluateeId) {
        this.evaluateeId = evaluateeId;
    }

    public Long getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
    }
}

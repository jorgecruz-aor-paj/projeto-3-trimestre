package aor.proj3t.dto;

import java.time.Instant;

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
    private EvaluationStatus status;
    @XmlElement(name = "feedback")
    private String feedback;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "editedAt")
    private Instant editedAt;
    @XmlElement(name = "evaluationCycleId")
    private Long evaluationCycleId;
    @XmlElement(name = "evaluateeId")
    private MiniUserDTO evaluatee;
    @XmlElement(name = "evaluatorId")
    private MiniUserDTO evaluator;

    public EvaluationDTO() {
    }

    public EvaluationDTO(Long id, EvaluationScale scale, EvaluationStatus state, String feedback,
                         Instant createdAt, Instant editedAt, Long evaluationCycleId,
                         MiniUserDTO evaluatee, MiniUserDTO evaluator) {
        this.id = id;
        this.scale = scale;
        this.status = state;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.evaluationCycleId = evaluationCycleId;
        this.evaluatee = evaluatee;
        this.evaluator = evaluator;
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

    public EvaluationStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationStatus status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    public Long getEvaluationCycleId() {
        return evaluationCycleId;
    }

    public void setEvaluationCycleId(Long evaluationCycleId) {
        this.evaluationCycleId = evaluationCycleId;
    }

    public MiniUserDTO getEvaluatee() {
        return evaluatee;
    }

    public void setEvaluatee(MiniUserDTO evaluatee) {
        this.evaluatee = evaluatee;
    }

    public MiniUserDTO getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(MiniUserDTO evaluator) {
        this.evaluator = evaluator;
    }
}

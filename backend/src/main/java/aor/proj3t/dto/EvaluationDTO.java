package aor.proj3t.dto;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

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
    @XmlElement(name = "evaluationCycle")
    private MiniEvaluationCycleDTO evaluationCycle;
    @XmlElement(name = "evaluateeId")
    private MiniUserDTO evaluatee;
    @XmlElement(name = "evaluatorId")
    private MiniUserDTO evaluator;

    public EvaluationDTO() {
    }

    public EvaluationDTO(Long id, EvaluationScale scale, EvaluationStatus state, String feedback,
                         Instant createdAt, Instant editedAt, MiniEvaluationCycleDTO evaluationCycle,
                         MiniUserDTO evaluatee, MiniUserDTO evaluator) {
        this.id = id;
        this.scale = scale;
        this.status = state;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.evaluationCycle = evaluationCycle;
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

    public MiniEvaluationCycleDTO getEvaluationCycle() {
        return evaluationCycle;
    }

    public void setEvaluationCycle(MiniEvaluationCycleDTO evaluationCycle) {
        this.evaluationCycle = evaluationCycle;
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

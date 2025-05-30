package aor.proj3t.Entities;

import aor.proj3t.Enums.EvaluationScale;
import aor.proj3t.Enums.EvaluationState;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "scale", nullable = false)
    private EvaluationScale scale;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EvaluationState state;

    @Column(name = "feedback")
    private String feedback;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name = "evaluation_cycle_id", nullable = false)
    private EvaluationCycle evaluationCycle;

    @ManyToOne
    @JoinColumn(name = "evaluatee_id", nullable = false)
    private User evaluatee;

    @ManyToOne
    @JoinColumn(name = "evaluator_id", nullable = false)
    private User evaluator;

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

    public EvaluationState getState() {
        return state;
    }

    public void setState(EvaluationState state) {
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

    public EvaluationCycle getEvaluationCycle() {
        return evaluationCycle;
    }

    public void setEvaluationCycle(EvaluationCycle evaluationCycle) {
        this.evaluationCycle = evaluationCycle;
    }

    public User getEvaluatee() {
        return evaluatee;
    }

    public void setEvaluatee(User evaluatee) {
        this.evaluatee = evaluatee;
    }

    public User getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }
}


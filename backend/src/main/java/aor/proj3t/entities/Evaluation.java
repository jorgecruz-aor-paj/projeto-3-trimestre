package aor.proj3t.entities;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@NamedQueries({
        @NamedQuery(name = "Evaluation.findCompleted", query = "SELECT e FROM Evaluation e WHERE e.status = 'COMPLETED'"),
})
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "scale", nullable = false)
    private EvaluationScale scale;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationStatus status;

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
    @JoinColumn(name = "evaluated_id", nullable = false)
    private User evaluated;

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

    public User getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(User evaluatee) {
        this.evaluated = evaluatee;
    }

    public User getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }
}


package aor.proj3t.entities;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

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
    @Column(name = "scale")
    private EvaluationScale scale;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationStatus status = EvaluationStatus.ONGOING;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @Column(name = "edited_at")
    private Instant editedAt;

    @PreUpdate
    protected void onUpdate() {
        this.editedAt = Instant.now();
    }

    @ManyToOne
    @JoinColumn(name = "evaluation_cycle_id", nullable = false)
    private EvaluationCycle evaluationCycle;

    @ManyToOne
    @JoinColumn(name = "evaluated_id", nullable = false)
    private User evaluated;

    @ManyToOne
    @JoinColumn(name = "evaluator_id", nullable = false)
    private User evaluator;

    public Evaluation() {}

    public Evaluation(EvaluationCycle evaluationCycle, User evaluated, User evaluator) {
        this.evaluationCycle = evaluationCycle;
        this.evaluated = evaluated;
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


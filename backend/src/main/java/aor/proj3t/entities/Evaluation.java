package aor.proj3t.entities;

import java.time.Instant;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Entity representing an evaluation of a user within an evaluation cycle.
 * An evaluation captures the performance assessment of an employee by their
 * evaluator,
 * including a scale rating, feedback, and status tracking.
 * 
 * @author Jorge Osório
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "evaluations")
@NamedQueries({
        @NamedQuery(name = "Evaluation.findCompleted", query = "SELECT e FROM Evaluation e WHERE e.status = 'COMPLETED'"),
})
public class Evaluation {

    /**
     * Unique identifier for the evaluation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The scale/rating given in this evaluation.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "scale")
    private EvaluationScale scale;

    /**
     * The current status of the evaluation (ONGOING, COMPLETED, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationStatus status = EvaluationStatus.ONGOING;

    /**
     * Written feedback provided by the evaluator.
     */
    @Column(name = "feedback")
    private String feedback;

    /**
     * Timestamp when the evaluation was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * JPA lifecycle callback to set creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    /**
     * Timestamp when the evaluation was last modified.
     */
    @Column(name = "edited_at")
    private Instant editedAt;

    /**
     * JPA lifecycle callback to set modification timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.editedAt = Instant.now();
    }

    /**
     * The evaluation cycle this evaluation belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "evaluation_cycle_id", nullable = false)
    private EvaluationCycle evaluationCycle;

    /**
     * The user being evaluated.
     */
    @ManyToOne
    @JoinColumn(name = "evaluated_id", nullable = false)
    private User evaluated;

    /**
     * The user conducting the evaluation.
     */
    @ManyToOne
    @JoinColumn(name = "evaluator_id", nullable = false)
    private User evaluator;

    /**
     * Default constructor for JPA.
     */
    public Evaluation() {
    }

    /**
     * Constructor to create an evaluation with required relationships.
     * 
     * @param evaluationCycle the evaluation cycle this evaluation belongs to
     * @param evaluated       the user being evaluated
     * @param evaluator       the user conducting the evaluation
     */
    public Evaluation(EvaluationCycle evaluationCycle, User evaluated, User evaluator) {
        this.evaluationCycle = evaluationCycle;
        this.evaluated = evaluated;
        this.evaluator = evaluator;
    }

    /**
     * Gets the unique identifier of this evaluation.
     * 
     * @return the evaluation ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this evaluation.
     * 
     * @param id the evaluation ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the scale/rating of this evaluation.
     * 
     * @return the evaluation scale
     */
    public EvaluationScale getScale() {
        return scale;
    }

    /**
     * Sets the scale/rating of this evaluation.
     * 
     * @param scale the evaluation scale
     */
    public void setScale(EvaluationScale scale) {
        this.scale = scale;
    }

    /**
     * Gets the current status of this evaluation.
     * 
     * @return the evaluation status
     */
    public EvaluationStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of this evaluation.
     * 
     * @param status the evaluation status
     */
    public void setStatus(EvaluationStatus status) {
        this.status = status;
    }

    /**
     * Gets the feedback text for this evaluation.
     * 
     * @return the feedback text
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * Sets the feedback text for this evaluation.
     * 
     * @param feedback the feedback text
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * Gets the creation timestamp of this evaluation.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this evaluation.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last modification timestamp of this evaluation.
     * 
     * @return the last modification timestamp
     */
    public Instant getEditedAt() {
        return editedAt;
    }

    /**
     * Sets the last modification timestamp of this evaluation.
     * 
     * @param editedAt the last modification timestamp
     */
    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    /**
     * Gets the evaluation cycle this evaluation belongs to.
     * 
     * @return the evaluation cycle
     */
    public EvaluationCycle getEvaluationCycle() {
        return evaluationCycle;
    }

    /**
     * Sets the evaluation cycle this evaluation belongs to.
     * 
     * @param evaluationCycle the evaluation cycle
     */
    public void setEvaluationCycle(EvaluationCycle evaluationCycle) {
        this.evaluationCycle = evaluationCycle;
    }

    /**
     * Gets the user being evaluated.
     * 
     * @return the evaluated user
     */
    public User getEvaluated() {
        return evaluated;
    }

    /**
     * Sets the user being evaluated.
     * 
     * @param evaluatee the evaluated user
     */
    public void setEvaluated(User evaluatee) {
        this.evaluated = evaluatee;
    }

    /**
     * Gets the user conducting the evaluation.
     * 
     * @return the evaluator user
     */
    public User getEvaluator() {
        return evaluator;
    }

    /**
     * Sets the user conducting the evaluation.
     * 
     * @param evaluator the evaluator user
     */
    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }
}

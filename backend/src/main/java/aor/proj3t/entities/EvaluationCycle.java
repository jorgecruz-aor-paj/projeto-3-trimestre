package aor.proj3t.entities;

import java.time.Instant;
import java.util.List;

import aor.proj3t.enums.EvaluationCycleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing an evaluation cycle within the organization.
 * An evaluation cycle defines a period during which employee evaluations are
 * conducted,
 * with specific start and end dates, and can contain multiple individual
 * evaluations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "evaluation_cycles")

@NamedQueries({
        @NamedQuery(name = "EvaluationCycle.findActive", query = "SELECT ec FROM EvaluationCycle ec WHERE ec.status = 'ACTIVE' OR ec.status = 'COMPLETED'"),
        @NamedQuery(name = "EvaluationCycle.findAll", query = "SELECT ec FROM EvaluationCycle ec WHERE ec.status = 'CLOSED' ORDER BY ec.closureDate DESC"),
        @NamedQuery(name = "EvaluationCycle.countActive", query = "SELECT COUNT(ec) FROM EvaluationCycle ec WHERE ec.status = 'ACTIVE'")
})

public class EvaluationCycle {

    /**
     * Unique identifier for the evaluation cycle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when the evaluation cycle was created.
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
     * The start date of the evaluation cycle.
     */
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    /**
     * The end date of the evaluation cycle.
     */
    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    /**
     * The date when the evaluation cycle was closed.
     */
    @Column(name = "closure_date")
    private Instant closureDate;

    /**
     * The current status of the evaluation cycle.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationCycleStatus status = EvaluationCycleStatus.ACTIVE;

    /**
     * The user who created this evaluation cycle.
     */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * List of evaluations associated with this cycle.
     */
    @OneToMany(mappedBy = "evaluationCycle", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations;

    /**
     * Default constructor for JPA.
     */
    public EvaluationCycle() {
    }

    /**
     * Constructor to create an evaluation cycle with required parameters.
     * 
     * @param startDate the start date of the cycle
     * @param endDate   the end date of the cycle
     * @param createdBy the user creating the cycle
     */
    public EvaluationCycle(Instant startDate, Instant endDate, User createdBy) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
    }

    /**
     * Gets the unique identifier of this evaluation cycle.
     * 
     * @return the cycle ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this evaluation cycle.
     * 
     * @param id the cycle ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the creation timestamp of this evaluation cycle.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this evaluation cycle.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the start date of this evaluation cycle.
     * 
     * @return the start date
     */
    public Instant getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of this evaluation cycle.
     * 
     * @param startDate the start date
     */
    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of this evaluation cycle.
     * 
     * @return the end date
     */
    public Instant getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of this evaluation cycle.
     * 
     * @param endDate the end date
     */
    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the closure date of this evaluation cycle.
     * 
     * @return the closure date
     */
    public Instant getClosureDate() {
        return closureDate;
    }

    /**
     * Sets the closure date of this evaluation cycle.
     * 
     * @param closureDate the closure date
     */
    public void setClosureDate(Instant closureDate) {
        this.closureDate = closureDate;
    }

    /**
     * Gets the user who created this evaluation cycle.
     * 
     * @return the creator user
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created this evaluation cycle.
     * 
     * @param createdBy the creator user
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the status of this evaluation cycle.
     * 
     * @return the cycle status
     */
    public EvaluationCycleStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this evaluation cycle.
     * 
     * @param status the cycle status
     */
    public void setStatus(EvaluationCycleStatus status) {
        this.status = status;
    }

    /**
     * Gets the list of evaluations associated with this cycle.
     * 
     * @return the list of evaluations
     */
    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    /**
     * Sets the list of evaluations associated with this cycle.
     * 
     * @param evaluations the list of evaluations
     */
    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}

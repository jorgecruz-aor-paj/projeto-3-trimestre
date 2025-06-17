package aor.proj3t.entities;

import aor.proj3t.enums.EvaluationCycleStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.xml.namespace.QName;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "evaluation_cycles")

@NamedQueries({
        @NamedQuery(name = "EvaluationCycle.findActive", query = "SELECT ec FROM EvaluationCycle ec WHERE ec.status = 'ACTIVE'"),
        @NamedQuery(name = "EvaluationCycle.findAll", query = "SELECT ec FROM EvaluationCycle ec"),
        @NamedQuery(
                name = "EvaluationCycle.countActive",
                query = "SELECT COUNT(ec) FROM EvaluationCycle ec WHERE ec.status = 'ACTIVE'"
        )

})

public class EvaluationCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "closure_date")
    private Instant closureDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationCycleStatus status = EvaluationCycleStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "evaluationCycle", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations;

    public EvaluationCycle() {}

    public EvaluationCycle(Instant startDate, Instant endDate, User createdBy) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(Instant closureDate) {
        this.closureDate = closureDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public EvaluationCycleStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationCycleStatus status) {
        this.status = status;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}


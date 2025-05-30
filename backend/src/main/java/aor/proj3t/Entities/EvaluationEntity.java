package aor.proj3t.Entities;

import aor.proj3t.Enums.EvaluationScale;
import aor.proj3t.Enums.EvaluationState;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class EvaluationEntity {

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
    private EvaluationCycleEntity evaluationCycle;

    @ManyToOne
    @JoinColumn(name = "evaluatee_id", nullable = false)
    private UserEntity evaluatee;

    @ManyToOne
    @JoinColumn(name = "evaluator_id", nullable = false)
    private UserEntity evaluator;

}


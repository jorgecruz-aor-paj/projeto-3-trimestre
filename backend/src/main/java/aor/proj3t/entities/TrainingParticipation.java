package aor.proj3t.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "training_participation")
public class TrainingParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "participated_at")
    private LocalDateTime participatedAt;

    // Constructors, Getters, Setters
}


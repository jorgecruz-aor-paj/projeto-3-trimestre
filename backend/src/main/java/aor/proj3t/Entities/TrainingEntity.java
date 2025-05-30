package aor.proj3t.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trainings")
public class TrainingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "duration", nullable = false)
    private Integer duration; // Duration in minutes

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name = "training_area_id", nullable = false)
    private TrainingAreaEntity trainingArea;

    // Constructors, Getters, Setters
}


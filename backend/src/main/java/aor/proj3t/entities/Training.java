package aor.proj3t.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "duration", nullable = false)
    private Integer duration; // Duration in minutes

    @Column(name = "description",length = 1000)
    private String description;

    @Column(name = "link",length = 500)
    private String link;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

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
    @JoinColumn(name = "training_area_id", nullable = false)
    private TrainingArea trainingArea;

    public Training(){}

    public Training (String title, Integer duration, String description, String link, String language, TrainingArea trainingArea) {
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.link = link;
        this.language = language;
        this.trainingArea = trainingArea;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public TrainingArea getTrainingArea() {
        return trainingArea;
    }

    public void setTrainingArea(TrainingArea trainingArea) {
        this.trainingArea = trainingArea;
    }
}


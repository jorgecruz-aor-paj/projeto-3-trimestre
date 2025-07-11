package aor.proj3t.entities;

import java.time.Instant;

import aor.proj3t.converters.TrainingLanguageConverter;
import aor.proj3t.enums.TrainingLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Entity representing a training course or module available in the system.
 * Training entities define educational content with metadata such as duration,
 * language, and associated training areas for employee development.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "trainings")
public class Training {

    /**
     * Unique identifier for the training.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title/name of the training course.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Duration of the training in minutes.
     */
    @Column(name = "duration", nullable = false)
    private Integer duration;

    /**
     * Detailed description of the training content.
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * External link or URL to access the training content.
     */
    @Column(name = "link", length = 500)
    private String link;

    /**
     * Language in which the training is available.
     */
    @Column(name = "language", nullable = false)
    @Enumerated(EnumType.STRING)
    @Convert(converter = TrainingLanguageConverter.class)
    private TrainingLanguage language;

    /**
     * Flag indicating whether the training is currently active/available.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * Timestamp when the training was created.
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
     * Timestamp when the training was last modified.
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
     * The training area/category this training belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "training_area_id", nullable = false)
    private TrainingArea trainingArea;

    /**
     * Default constructor for JPA.
     */
    public Training() {
    }

    /**
     * Constructor to create a training with all required parameters.
     * 
     * @param title        the training title
     * @param duration     the duration in minutes
     * @param description  the training description
     * @param link         the external link to training content
     * @param language     the training language
     * @param trainingArea the training area/category
     */
    public Training(String title, Integer duration, String description, String link, TrainingLanguage language,
            TrainingArea trainingArea) {
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.link = link;
        this.language = language;
        this.trainingArea = trainingArea;
    }

    /**
     * Gets the unique identifier of this training.
     * 
     * @return the training ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this training.
     * 
     * @param id the training ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the title of this training.
     * 
     * @return the training title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this training.
     * 
     * @param title the training title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the duration of this training in minutes.
     * 
     * @return the duration in minutes
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the duration of this training in minutes.
     * 
     * @param duration the duration in minutes
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets the description of this training.
     * 
     * @return the training description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this training.
     * 
     * @param description the training description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the external link to the training content.
     * 
     * @return the training link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the external link to the training content.
     * 
     * @param link the training link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the language of this training.
     * 
     * @return the training language
     */
    public TrainingLanguage getLanguage() {
        return language;
    }

    /**
     * Sets the language of this training.
     * 
     * @param language the training language
     */
    public void setLanguage(TrainingLanguage language) {
        this.language = language;
    }

    /**
     * Checks if this training is currently active.
     * 
     * @return true if the training is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of this training.
     * 
     * @param active the active status
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the creation timestamp of this training.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this training.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last modification timestamp of this training.
     * 
     * @return the last modification timestamp
     */
    public Instant getEditedAt() {
        return editedAt;
    }

    /**
     * Sets the last modification timestamp of this training.
     * 
     * @param editedAt the last modification timestamp
     */
    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    /**
     * Gets the training area this training belongs to.
     * 
     * @return the training area
     */
    public TrainingArea getTrainingArea() {
        return trainingArea;
    }

    /**
     * Sets the training area this training belongs to.
     * 
     * @param trainingArea the training area
     */
    public void setTrainingArea(TrainingArea trainingArea) {
        this.trainingArea = trainingArea;
    }
}

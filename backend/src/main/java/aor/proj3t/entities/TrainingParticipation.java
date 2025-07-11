package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a user's participation in a training course.
 * This entity tracks when employees participate in training sessions,
 * including the manager who approved/assigned the training and participation
 * timestamps.
 * 
 * @author Jorge Osório
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "training_participation")
public class TrainingParticipation {

    /**
     * Unique identifier for the training participation record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who participated in the training.
     */
    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    /**
     * The manager who approved/assigned the training.
     */
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    /**
     * The training course that was participated in.
     */
    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    /**
     * Timestamp when the participation record was created.
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
     * Timestamp when the user actually participated in the training.
     */
    @Column(name = "participated_at")
    private Instant participatedAt;

    /**
     * Default constructor for JPA.
     */
    public TrainingParticipation() {
    }

    /**
     * Constructor to create a training participation record.
     * 
     * @param participant    the user participating in the training
     * @param manager        the manager who approved the training
     * @param training       the training course
     * @param participatedAt the date when participation occurred
     */
    public TrainingParticipation(User participant, User manager, Training training, Instant participatedAt) {
        this.participant = participant;
        this.manager = manager;
        this.training = training;
        this.participatedAt = participatedAt;
    }

    /**
     * Gets the unique identifier of this participation record.
     * 
     * @return the participation ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this participation record.
     * 
     * @param id the participation ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user who participated in the training.
     * 
     * @return the participant user
     */
    public User getParticipant() {
        return participant;
    }

    /**
     * Sets the user who participated in the training.
     * 
     * @param participant the participant user
     */
    public void setParticipant(User participant) {
        this.participant = participant;
    }

    /**
     * Gets the manager who approved/assigned the training.
     * 
     * @return the manager user
     */
    public User getManager() {
        return manager;
    }

    /**
     * Sets the manager who approved/assigned the training.
     * 
     * @param manager the manager user
     */
    public void setManager(User manager) {
        this.manager = manager;
    }

    /**
     * Gets the training course that was participated in.
     * 
     * @return the training
     */
    public Training getTraining() {
        return training;
    }

    /**
     * Sets the training course that was participated in.
     * 
     * @param training the training
     */
    public void setTraining(Training training) {
        this.training = training;
    }

    /**
     * Gets the creation timestamp of this participation record.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this participation record.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the timestamp when the user participated in the training.
     * 
     * @return the participation timestamp
     */
    public Instant getParticipatedAt() {
        return participatedAt;
    }

    /**
     * Sets the timestamp when the user participated in the training.
     * 
     * @param participatedAt the participation timestamp
     */
    public void setParticipatedAt(Instant participatedAt) {
        this.participatedAt = participatedAt;
    }
}

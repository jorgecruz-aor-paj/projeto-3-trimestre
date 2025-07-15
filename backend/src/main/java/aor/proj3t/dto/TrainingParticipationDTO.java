package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code TrainingParticipation} entity.
 * <p>
 * Captures the metadata linking a user to a specific training they’ve attended or completed,
 * including assignment origin, participation timestamp, and associated training details.
 * Commonly used in reporting dashboards, certification trackers, and audit logs.
 * JAXB annotations enable XML serialization for API exchanges.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the participation record
 * - {@code participantId}: ID of the user who took part in the training
 * - {@code managerId}: ID of the manager who assigned or approved the training
 * - {@code training}: Reference to the training content that was attended
 * - {@code createdAt}: Timestamp when the participation record was created
 * - {@code participatedAt}: Timestamp when the training participation occurred
 *
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "trainingParticipation")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingParticipationDTO {

    /** Unique identifier for the training participation entry */
    @XmlElement(name = "id")
    private Long id;

    /** ID of the user who participated in the training */
    @XmlElement(name = "participantId")
    private Long participantId;

    /** ID of the manager who assigned or authorized the training */
    @XmlElement(name = "managerId")
    private Long managerId;

    /** Reference to the training resource associated with participation */
    @XmlElement(name = "training")
    private TrainingDTO training;

    /** Timestamp when the participation record was created */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Timestamp when the training session was attended */
    @XmlElement(name = "participatedAt")
    private Instant participatedAt;

    public TrainingParticipationDTO() {
    }

    public TrainingParticipationDTO(Long id, Long participantId, Long managerId, TrainingDTO training,
            Instant createdAt, Instant participatedAt) {
        this.id = id;
        this.participantId = participantId;
        this.managerId = managerId;
        this.training = training;
        this.createdAt = createdAt;
        this.participatedAt = participatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public TrainingDTO getTraining() {
        return training;
    }

    public void setTraining(TrainingDTO training) {
        this.training = training;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(Instant participatedAt) {
        this.participatedAt = participatedAt;
    }
}

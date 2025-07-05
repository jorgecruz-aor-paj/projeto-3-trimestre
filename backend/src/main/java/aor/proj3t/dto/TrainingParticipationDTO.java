package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for TrainingParticipation entity
 */
@XmlRootElement(name = "trainingParticipation")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingParticipationDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "participantId")
    private Long participantId;
    @XmlElement(name = "managerId")
    private Long managerId;
    @XmlElement(name = "training")
    private TrainingDTO training;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
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

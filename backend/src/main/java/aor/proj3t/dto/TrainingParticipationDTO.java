package aor.proj3t.dto;

import java.time.LocalDateTime;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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
    @XmlElement(name = "trainingId")
    private Long trainingId;
    @XmlElement(name = "createdAt")
    private LocalDateTime createdAt;
    @XmlElement(name = "participatedAt")
    private LocalDateTime participatedAt;

    public TrainingParticipationDTO() {
    }

    public TrainingParticipationDTO(Long id, Long participantId, Long managerId, Long trainingId,
            LocalDateTime createdAt, LocalDateTime participatedAt) {
        this.id = id;
        this.participantId = participantId;
        this.managerId = managerId;
        this.trainingId = trainingId;
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

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(LocalDateTime participatedAt) {
        this.participatedAt = participatedAt;
    }
}

package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

@XmlRootElement(name = "newTrainingParticipation")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewTrainingParticipationDTO {

    @XmlElement(name = "participantId")
    private Long participantId;
    @XmlElement(name = "managerId")
    private Long managerId;
    @XmlElement(name = "trainingId")
    private Long trainingId;
    @XmlElement(name = "participatedAt")
    private Instant participatedAt;

    public NewTrainingParticipationDTO() {
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

    public Instant getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(Instant participatedAt) {
        this.participatedAt = participatedAt;
    }
}

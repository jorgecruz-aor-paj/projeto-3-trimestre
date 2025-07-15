package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for recording a user's participation in a training.
 * <p>
 * Captures the basic linkage between a participant, their manager, and a specific
 * training instance — along with the timestamp of when participation occurred.
 * Commonly used when submitting training completion records or tracking user progress.
 * JAXB annotations support XML serialization for endpoints that exchange XML payloads.
 * </p>
 *
 * Attributes:
 * - {@code participantId}: ID of the user who attended the training
 * - {@code managerId}: ID of the user's manager responsible for assigning or approving training
 * - {@code trainingId}: ID of the training session completed
 * - {@code participatedAt}: Timestamp indicating when the participation took place
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "newTrainingParticipation")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewTrainingParticipationDTO {

    /** ID of the participant who attended the training */
    @XmlElement(name = "participantId")
    private Long participantId;

    /** ID of the manager overseeing the participant’s training */
    @XmlElement(name = "managerId")
    private Long managerId;

    /** ID of the training session completed */
    @XmlElement(name = "trainingId")
    private Long trainingId;

    /** Timestamp of when participation occurred */
    @XmlElement(name = "participatedAt")
     private Instant participatedAt;

    public NewTrainingParticipationDTO() {}

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

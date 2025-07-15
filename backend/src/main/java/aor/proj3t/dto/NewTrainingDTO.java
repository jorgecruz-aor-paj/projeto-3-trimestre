package aor.proj3t.dto;

import aor.proj3t.enums.TrainingLanguage;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for creating a new {@code Training} resource.
 * <p>
 * Encapsulates all necessary information to define a training session, including its
 * metadata, duration, and categorization. Designed for use with REST API endpoints
 * that support training creation. JAXB annotations allow for XML-based serialization
 * and deserialization where needed.
 * </p>
 *
 * Attributes:
 * - {@code title}: Name or headline of the training
 * - {@code duration}: Total time in minutes required to complete the training
 * - {@code description}: Textual overview or objective of the training
 * - {@code link}: External URL or internal reference where the training content is hosted
 * - {@code language}: Language the training is delivered in (e.g., ENGLISH, PORTUGUESE)
 * - {@code trainingAreaId}: Identifier linking the training to a specific knowledge or skill area
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "newTraining")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewTrainingDTO {

    /** Title or name of the training session */
    @XmlElement(name = "title")
    private String title;

    /** Duration of the training session in minutes */
    @XmlElement(name = "duration")
    private Integer duration;

    /** Brief description of the training content or objectives */
    @XmlElement(name = "description")
    private String description;

    /** URL or path to the training material */
    @XmlElement(name = "link")
    private String link;

    /** Language in which the training is delivered */
    @XmlElement(name = "language")
    private TrainingLanguage language;

    /** ID of the training area this training belongs to */
    @XmlElement(name = "trainingAreaId")
    private Long trainingAreaId;

    public NewTrainingDTO() {}


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

    public TrainingLanguage getLanguage() {
        return language;
    }

    public void setLanguage(TrainingLanguage language) {
        this.language = language;
    }

    public Long getTrainingAreaId() {
        return trainingAreaId;
    }

    public void setTrainingAreaId(Long trainingAreaId) {
        this.trainingAreaId = trainingAreaId;
    }
}

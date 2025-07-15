package aor.proj3t.dto;

import aor.proj3t.enums.TrainingLanguage;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for {@code Training} entity.
 * <p>
 * Represents a structured training module including metadata, categorization,
 * lifecycle timestamps, and status flags. This DTO facilitates the exchange
 * of training-related data across REST APIs, client interfaces, and internal services.
 * JAXB annotations allow for XML serialization in services that require it.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the training entry
 * - {@code title}: Name or headline of the training course
 * - {@code duration}: Total duration of the training in minutes
 * - {@code description}: Summary or content overview for the training
 * - {@code link}: URL to training materials or platform
 * - {@code language}: Language in which the training is provided (e.g. ENGLISH, SPANISH)
 * - {@code isActive}: Indicates whether the training is currently available
 * - {@code createdAt}: Timestamp of when the training was created
 * - {@code editedAt}: Timestamp of the most recent modification
 * - {@code trainingArea}: Category or thematic area the training belongs to
 *
 * Use cases:
 * - Displaying available trainings to users
 * - Admin dashboard for managing training catalog
 * - Exporting or syncing training records across systems
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "training")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingDTO {

    /** Unique identifier for the training */
    @XmlElement(name = "id")
    private Long id;

    /** Title or name of the training module */
    @XmlElement(name = "title")
    private String title;

    /** Duration of the training in minutes */
    @XmlElement(name = "duration")
    private Integer duration;

    /** Description or objective overview of the training content */
    @XmlElement(name = "description")
    private String description;

    /** External link or resource location for accessing the training */
    @XmlElement(name = "link")
    private String link;

    /** Language in which the training is presented */
    @XmlElement(name = "language")
    private TrainingLanguage language;

    /** Indicates if the training is currently active and viewable */
    @XmlElement(name = "active")
    private boolean isActive;

    /** Timestamp for when the training record was first created */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Timestamp of the most recent edit to the training */
    @XmlElement(name = "editedAt")
    private Instant editedAt;

    /** Categorization object representing the training area/topic */
    @XmlElement(name = "trainingArea")
    private TrainingAreaDTO trainingArea;

    public TrainingDTO() {}

    public TrainingDTO(Long id, String title, Integer duration, String description, String link,
                       TrainingLanguage language, boolean isActive,
                       Instant createdAt, Instant editedAt, TrainingAreaDTO trainingArea) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.link = link;
        this.language = language;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
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

    public TrainingLanguage getLanguage() {
        return language;
    }

    public void setLanguage(TrainingLanguage language) {
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

    public TrainingAreaDTO getTrainingArea() {
        return trainingArea;
    }

    public void setTrainingArea(TrainingAreaDTO trainingArea) {
        this.trainingArea = trainingArea;
    }
}

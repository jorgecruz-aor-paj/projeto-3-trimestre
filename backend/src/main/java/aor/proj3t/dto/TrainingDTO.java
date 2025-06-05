package aor.proj3t.dto;

import java.time.Instant;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Training entity
 */
@XmlRootElement(name = "training")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "duration")
    private Integer duration;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "link")
    private String link;
    @XmlElement(name = "language")
    private String language;
    @XmlElement(name = "active")
    private boolean isActive;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "editedAt")
    private Instant editedAt;
    @XmlElement(name = "trainingAreaId")
    private Long trainingAreaId;

    public TrainingDTO() {
    }

    public TrainingDTO(Long id, String name, Integer duration, String description, String link, String language,
            boolean isActive, Instant createdAt, Instant editedAt, Long trainingAreaId) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.link = link;
        this.language = language;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.trainingAreaId = trainingAreaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getTrainingAreaId() {
        return trainingAreaId;
    }

    public void setTrainingAreaId(Long trainingAreaId) {
        this.trainingAreaId = trainingAreaId;
    }
}

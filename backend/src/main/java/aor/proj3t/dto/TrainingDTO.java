package aor.proj3t.dto;

import aor.proj3t.enums.TrainingLanguage;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;

/**
 * Data Transfer Object for Training entity
 */
@XmlRootElement(name = "training")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingDTO {
    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "duration")
    private Integer duration;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "link")
    private String link;
    @XmlElement(name = "language")
    private TrainingLanguage language;
    @XmlElement(name = "active")
    private boolean isActive;
    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "editedAt")
    private Instant editedAt;
    @XmlElement(name = "trainingArea")
    private TrainingAreaDTO trainingArea;

    public TrainingDTO() {
    }

    public TrainingDTO(Long id, String title, Integer duration, String description, String link, TrainingLanguage language,
            boolean isActive, Instant createdAt, Instant editedAt, TrainingAreaDTO trainingArea) {
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

package aor.proj3t.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.Instant;

/**
 * DTO specifically designed for CSV export of Training data
 * Uses OpenCSV annotations for automatic CSV mapping
 */
public class TrainingCsvDTO {

    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "Title")
    private String title;

    @CsvBindByName(column = "Duration")
    private Integer duration;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "Link")
    private String link;

    @CsvBindByName(column = "Language")
    private String language;

    @CsvBindByName(column = "Active")
    private boolean isActive;

    @CsvBindByName(column = "Created At")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;

    @CsvBindByName(column = "Edited At")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private Instant editedAt;

    @CsvBindByName(column = "Training Area ID")
    private Long trainingAreaId;

    // Default constructor required by OpenCSV
    public TrainingCsvDTO() {
    }

    // Constructor to convert from TrainingDTO
    public TrainingCsvDTO(TrainingDTO trainingDTO) {
        this.id = trainingDTO.getId();
        this.title = trainingDTO.getTitle();
        this.duration = trainingDTO.getDuration();
        this.description = trainingDTO.getDescription();
        this.link = trainingDTO.getLink();
        this.language = trainingDTO.getLanguage();
        this.isActive = trainingDTO.isActive();
        this.createdAt = trainingDTO.getCreatedAt();
        this.editedAt = trainingDTO.getEditedAt();
        this.trainingAreaId = trainingDTO.getTrainingArea().getId();
    }

    // Getters and setters
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

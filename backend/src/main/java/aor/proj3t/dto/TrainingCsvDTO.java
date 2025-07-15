package aor.proj3t.dto;

import aor.proj3t.enums.TrainingLanguage;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.Instant;

/**
 * DTO specifically designed for CSV export of {@code Training} data.
 * <p>
 * Uses OpenCSV annotations to automatically bind fields to CSV columns during export.
 * Encapsulates key attributes for training records, including metadata, duration,
 * links, and timestamps, along with formatting for date values. The constructor
 * allows seamless conversion from {@link TrainingDTO}, enabling fast serialization
 * workflows.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the training
 * - {@code title}: Title of the training module
 * - {@code duration}: Duration in minutes for the training
 * - {@code description}: Textual summary or overview of the training content
 * - {@code link}: URL or file reference where training resources are located
 * - {@code language}: Language in which the training is offered
 * - {@code isActive}: Flag indicating if the training is currently available
 * - {@code createdAt}: Timestamp of creation, formatted for CSV
 * - {@code editedAt}: Timestamp of last edit, formatted for CSV
 * - {@code trainingArea}: Name of the category/domain the training belongs to
 *
 *
 * Export tip: This class pairs well with OpenCSV writers for bulk export tasks and analytics.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class TrainingCsvDTO {

    /** Unique identifier of the training */
    @CsvBindByName(column = "ID")
    private Long id;

    /** Title of the training content */
    @CsvBindByName(column = "Title")
    private String title;

    /** Duration in minutes required to complete the training */
    @CsvBindByName(column = "Duration")
    private Integer duration;

    /** Textual description of the training objectives or content */
    @CsvBindByName(column = "Description")
    private String description;

    /** Link or reference to training materials */
    @CsvBindByName(column = "Link")
    private String link;

    /** Language in which the training is delivered */
    @CsvBindByName(column = "Language")
    private TrainingLanguage language;

    /** Indicates whether the training is currently active or published */
    @CsvBindByName(column = "Active")
    private boolean isActive;

    /** Timestamp of creation, formatted for CSV output */
    @CsvBindByName(column = "Created At")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;

    /** Timestamp of last modification, formatted for CSV output */
    @CsvBindByName(column = "Edited At")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Instant editedAt;

    /** Name of the training category or area */
    @CsvBindByName(column = "Training Area")
    private String trainingArea;

    /** Default constructor required by OpenCSV */
    public TrainingCsvDTO() {}

    /**
     * Converts a {@link TrainingDTO} into a {@code TrainingCsvDTO} for export.
     *
     * @param trainingDTO the source training data
     */
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
        this.trainingArea = trainingDTO.getTrainingArea().getName();
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

    public String getTrainingAreaId() {
        return trainingArea;
    }

    public void setTrainingAreaId(String trainingArea) {
        this.trainingArea= trainingArea;
    }
}

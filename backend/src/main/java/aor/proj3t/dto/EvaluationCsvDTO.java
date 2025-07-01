package aor.proj3t.dto;

import java.time.Instant;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

/**
 * DTO specifically designed for CSV export of Evaluation data
 * Uses OpenCSV annotations for automatic CSV mapping
 */
public class EvaluationCsvDTO {

    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "Scale")
    private String scale;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "Feedback")
    private String feedback;

    @CsvBindByName(column = "Created At")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private Instant createdAt;

    @CsvBindByName(column = "Edited At")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private Instant editedAt;

    @CsvBindByName(column = "Evaluation Cycle ID")
    private Long evaluationCycleId;

    @CsvBindByName(column = "Evaluatee ID")
    private Long evaluateeId;

    @CsvBindByName(column = "Evaluator ID")
    private Long evaluatorId;

    // Default constructor required by OpenCSV
    public EvaluationCsvDTO() {
    }

    // Constructor to convert from EvaluationDTO
    public EvaluationCsvDTO(EvaluationDTO evaluationDTO) {
        this.id = evaluationDTO.getId();
        this.scale = evaluationDTO.getScale() != null ? evaluationDTO.getScale().toString() : null;
        this.status = evaluationDTO.getStatus() != null ? evaluationDTO.getStatus().toString() : null;
        this.feedback = evaluationDTO.getFeedback();
        this.createdAt = evaluationDTO.getCreatedAt();
        this.editedAt = evaluationDTO.getEditedAt();
        this.evaluationCycleId = evaluationDTO.getEvaluationCycleId();
        this.evaluateeId = evaluationDTO.getEvaluatee().getId();
        this.evaluatorId = evaluationDTO.getEvaluator().getId();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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

    public Long getEvaluationCycleId() {
        return evaluationCycleId;
    }

    public void setEvaluationCycleId(Long evaluationCycleId) {
        this.evaluationCycleId = evaluationCycleId;
    }

    public Long getEvaluateeId() {
        return evaluateeId;
    }

    public void setEvaluateeId(Long evaluateeId) {
        this.evaluateeId = evaluateeId;
    }

    public Long getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
    }
}

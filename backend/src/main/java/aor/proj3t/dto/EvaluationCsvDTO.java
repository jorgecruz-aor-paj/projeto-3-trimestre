package aor.proj3t.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.Instant;

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

    @CsvBindByName(column = "Evaluation Cycle Start Date")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")

    private Instant evaluationCycleStartDate;

    @CsvBindByName(column = "Evaluation Cycle End Date")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private Instant evaluationCycleClosureDate;

    @CsvBindByName(column = "Evaluatee ID")
    private Long evaluateeId;

    @CsvBindByName(column = "Evaluatee Name")
    private String evaluateeName;

    @CsvBindByName(column = "Evaluatee Email")
    private String evaluateeEmail;

    @CsvBindByName(column = "Evaluator ID")
    private Long evaluatorId;

    @CsvBindByName(column = "Evaluator Name")
    private String evaluatorName;

    @CsvBindByName(column = "Evaluator Email")
    private String evaluatorEmail;


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
        this.evaluationCycleId = evaluationDTO.getEvaluationCycle().getId();
        this.evaluationCycleStartDate = evaluationDTO.getEvaluationCycle().getStartDate();
        this.evaluationCycleClosureDate = evaluationDTO.getEvaluationCycle().getClosureDate();
        this.evaluateeId = evaluationDTO.getEvaluatee().getId();
        this.evaluateeName = evaluationDTO.getEvaluatee().getFirstName() + " " + evaluationDTO.getEvaluatee().getLastName();
        this.evaluateeEmail = evaluationDTO.getEvaluatee().getEmail();
        this.evaluatorId = evaluationDTO.getEvaluator().getId();
        this.evaluatorName = evaluationDTO.getEvaluator().getFirstName() + " " + evaluationDTO.getEvaluator().getLastName();
        this.evaluatorEmail = evaluationDTO.getEvaluator().getEmail();
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

    public String getEvaluateeName() {
        return evaluateeName;
    }

    public void setEvaluateeName(String evaluateeName) {
        this.evaluateeName = evaluateeName;
    }

    public String getEvaluateeEmail() {
        return evaluateeEmail;
    }

    public void setEvaluateeEmail(String evaluateeEmail) {
        this.evaluateeEmail = evaluateeEmail;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
    }

    public String getEvaluatorEmail() {
        return evaluatorEmail;
    }

    public void setEvaluatorEmail(String evaluatorEmail) {
        this.evaluatorEmail = evaluatorEmail;
    }

    public Instant getEvaluationCycleStartDate() {
        return evaluationCycleStartDate;
    }

    public void setEvaluationCycleStartDate(Instant evaluationCycleStartDate) {
        this.evaluationCycleStartDate = evaluationCycleStartDate;
    }

    public Instant getEvaluationCycleClosureDate() {
        return evaluationCycleClosureDate;
    }

    public void setEvaluationCycleClosureDate(Instant evaluationCycleClosureDate) {
        this.evaluationCycleClosureDate = evaluationCycleClosureDate;
    }
}

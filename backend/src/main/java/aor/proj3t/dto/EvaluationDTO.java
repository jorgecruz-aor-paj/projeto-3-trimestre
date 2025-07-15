package aor.proj3t.dto;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;
/**
 * Data Transfer Object for {@link Evaluation} entity.
 * <p>
 * Represents a single evaluation record within a cycle, capturing score, feedback,
 * timestamps, and references to participating users. Used primarily in API exchanges
 * and service layer communication. Annotated for XML marshalling via JAXB.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the evaluation
 * - {@code scale}: Rating scale selected during the evaluation
 * - {@code status}: Current state of the evaluation (e.g. PENDING, SUBMITTED)
 * - {@code feedback}: Optional textual commentary provided by the evaluator
 * - {@code createdAt}: When the evaluation was initially created
 * - {@code editedAt}: Last time the evaluation was modified
 * - {@code evaluationCycle}: Reference to the parent cycle this evaluation belongs to
 * - {@code evaluatee}: User being evaluated (lightweight DTO)
 * - {@code evaluator}: User performing the evaluation (lightweight DTO)
 *
 * JAXB-friendly for XML-based API compatibility.
 *
 * Example usage:
 * <pre>{@code
 *     EvaluationDTO dto = new EvaluationDTO();
 *     dto.setScale(EvaluationScale.EXCELLENT);
 *     dto.setStatus(EvaluationStatus.SUBMITTED);
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "evaluation")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationDTO {

    /** Unique identifier for the evaluation record */
    @XmlElement(name = "id")
    private Long id;

    /** Score or rating assigned to the evaluatee */
    @XmlElement(name = "scale")
    private EvaluationScale scale;

    /** Status of the evaluation (e.g. DRAFT, SUBMITTED) */
    @XmlElement(name = "state")
    private EvaluationStatus status;

    /** Optional textual feedback provided by the evaluator */
    @XmlElement(name = "feedback")
    private String feedback;

    /** Timestamp when the evaluation was created */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Timestamp when the evaluation was last edited */
    @XmlElement(name = "editedAt")
    private Instant editedAt;

    /** Reference to the associated evaluation cycle (summary DTO) */
    @XmlElement(name = "evaluationCycle")
    private MiniEvaluationCycleDTO evaluationCycle;

    /** User being evaluated */
    @XmlElement(name = "evaluateeId")
    private MiniUserDTO evaluatee;

    /** User conducting the evaluation */
    @XmlElement(name = "evaluatorId")
    private MiniUserDTO evaluator;

    // Constructors
    public EvaluationDTO() {}

    public EvaluationDTO(Long id, EvaluationScale scale, EvaluationStatus state, String feedback,
                         Instant createdAt, Instant editedAt, MiniEvaluationCycleDTO evaluationCycle,
                         MiniUserDTO evaluatee, MiniUserDTO evaluator) {
        this.id = id;
        this.scale = scale;
        this.status = state;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.evaluationCycle = evaluationCycle;
        this.evaluatee = evaluatee;
        this.evaluator = evaluator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EvaluationScale getScale() {
        return scale;
    }

    public void setScale(EvaluationScale scale) {
        this.scale = scale;
    }

    public EvaluationStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationStatus status) {
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

    public MiniEvaluationCycleDTO getEvaluationCycle() {
        return evaluationCycle;
    }

    public void setEvaluationCycle(MiniEvaluationCycleDTO evaluationCycle) {
        this.evaluationCycle = evaluationCycle;
    }

    public MiniUserDTO getEvaluatee() {
        return evaluatee;
    }

    public void setEvaluatee(MiniUserDTO evaluatee) {
        this.evaluatee = evaluatee;
    }

    public MiniUserDTO getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(MiniUserDTO evaluator) {
        this.evaluator = evaluator;
    }
}

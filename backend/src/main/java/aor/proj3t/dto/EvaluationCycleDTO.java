package aor.proj3t.dto;

import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object for {@link EvaluationCycle} entity.
 * <p>
 * Encapsulates all relevant data representing a full evaluation cycle, including
 * lifecycle dates, status, creator metadata, and associated evaluations. Designed
 * for transmission between services and REST API layers.
 * </p>
 *
 * Key fields:
 * - {@code id}: Unique cycle identifier
 * - {@code createdAt}: Timestamp of cycle creation
 * - {@code startDate}, {@code endDate}, {@code closureDate}: Temporal bounds for evaluation activity
 * - {@code status}: Current lifecycle state
 * - {@code createdById}: ID of the user who initiated the cycle
 * - {@code evaluations}: List of evaluations tied to this cycle
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "evaluationCycle")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationCycleDTO {

    /** Unique identifier of the evaluation cycle */
    @XmlElement(name = "id")
    private Long id;

    /** Timestamp when the cycle was created in the system */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Date when the evaluation cycle officially starts */
    @XmlElement(name = "startDate")
    private Instant startDate;

    /** Target end date for active evaluations in the cycle */
    @XmlElement(name = "endDate")
    private Instant endDate;

    /** Timestamp marking the closure of the evaluation cycle */
    @XmlElement(name = "closureDate")
    private Instant closureDate;

    /** Current lifecycle status of the evaluation cycle (e.g., ACTIVE, COMPLETED, CLOSED) */
    @XmlElement(name = "status")
    private EvaluationCycleStatus status;

    /** ID of the user who created or initiated the evaluation cycle */
    @XmlElement(name = "createdById")
    private Long createdById;

    /** List of evaluations that are part of this cycle */
    @XmlElement(name = "evaluations")
    private List<EvaluationDTO> evaluations;

    /**
     * Default constructor for JAXB and serialization purposes.
     */
    public EvaluationCycleDTO() {}

    /**
     * Constructs an EvaluationCycleDTO with all core fields.
     */
    public EvaluationCycleDTO(Long id, Instant createdAt, Instant startDate, Instant endDate,
            Instant closureDate, EvaluationCycleStatus status, Long createdById) {
        this.id = id;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closureDate = closureDate;
        this.status = status;
        this.createdById = createdById;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(Instant closureDate) {
        this.closureDate = closureDate;
    }

    public EvaluationCycleStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationCycleStatus status) {
        this.status = status;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public List<EvaluationDTO> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationDTO> evaluations) {
        this.evaluations = evaluations;
    }
}

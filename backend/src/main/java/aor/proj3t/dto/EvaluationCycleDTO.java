package aor.proj3t.dto;

import java.time.Instant;
import java.util.List;

import aor.proj3t.entities.Evaluation;
import aor.proj3t.enums.EvaluationCycleStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for EvaluationCycle entity
 */
@XmlRootElement(name = "evaluationCycle")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationCycleDTO {

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "createdAt")
    private Instant createdAt;

    @XmlElement(name = "startDate")
    private Instant startDate;

    @XmlElement(name = "endDate")
    private Instant endDate;

    @XmlElement(name = "closureDate")
    private Instant closureDate;

    @XmlElement(name = "status")
    private EvaluationCycleStatus status;

    @XmlElement(name = "createdById")
    private Long createdById;

    @XmlElement(name = "evaluations")
    private List<EvaluationDTO> evaluations;

    public EvaluationCycleDTO() {
    }

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

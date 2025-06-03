package aor.proj3t.dto;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    @XmlElement(name = "startDate")
    private LocalDateTime startDate;
    @XmlElement(name = "endDate")
    private LocalDateTime endDate;
    @XmlElement(name = "closureDate")
    private LocalDateTime closureDate;
    @XmlElement(name = "status")
    private EvaluationCycleStatus status;
    @XmlElement(name = "createdById")
    private Long createdById;

    public EvaluationCycleDTO() {
    }

    public EvaluationCycleDTO(Long id, LocalDateTime createdAt, LocalDateTime startDate, LocalDateTime endDate,
            LocalDateTime closureDate, EvaluationCycleStatus status, Long createdById) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(LocalDateTime closureDate) {
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
}

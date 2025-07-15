package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Data Transfer Object for bulk closing evaluations within a cycle.
 * <p>
 * Carries the necessary data to trigger a close operation on a group of evaluations
 * that belong to a given cycle. Commonly used in administrative endpoints or automated
 * batch processes.
 * </p>
 *
 * Fields:
 * - {@code cycleId}: ID of the evaluation cycle being closed
 * - {@code evaluationIds}: list of evaluation IDs to be closed
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "evaluationClose")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationCloseDTO {

    @XmlElement(name = "cycleId")
    private Long cycleId;

    @XmlElement(name = "evaluationIds")
    private List<Long> evaluationIds;

    public EvaluationCloseDTO() {}

    /**
     * Returns the ID of the cycle for which evaluations are being closed.
     */
    public Long getCycleId() {
        return cycleId;
    }

    /**
     * Sets the ID of the cycle for which evaluations should be closed.
     *
     * @param cycleId the cycle identifier
     */
    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    /**
     * Returns the list of evaluation IDs to be closed.
     */
    public List<Long> getEvaluationIds() {
        return evaluationIds;
    }

    /**
     * Sets the list of evaluation IDs that should be marked for closure.
     *
     * @param evaluationIds list of evaluation identifiers
     */
    public void setEvaluationIds(List<Long> evaluationIds) {
        this.evaluationIds = evaluationIds;
    }
}


package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "evaluationClose")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationCloseDTO {

    @XmlElement(name = "cycleId")
    private Long cycleId;
    @XmlElement(name = "evaluationIds")
    private List<Long> evaluationIds;

    public EvaluationCloseDTO() {}

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public List<Long> getEvaluationIds() {
        return evaluationIds;
    }

    public void setEvaluationIds(List<Long> evaluationIds) {
        this.evaluationIds = evaluationIds;
    }
}

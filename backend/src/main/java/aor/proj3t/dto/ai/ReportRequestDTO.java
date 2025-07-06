package aor.proj3t.dto.ai;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object for Report Generation Request
 */
@XmlRootElement(name = "report_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "evaluations")
    private List<Float> evaluations;

    @XmlElement(name = "comments")
    private List<String> comments;

    public ReportRequestDTO() {
    }

    public ReportRequestDTO(List<Float> evaluations) {
        this.evaluations = evaluations;
    }

    public ReportRequestDTO(List<Float> evaluations, List<String> comments) {
        this.evaluations = evaluations;
        this.comments = comments;
    }

    public List<Float> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Float> evaluations) {
        this.evaluations = evaluations;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}

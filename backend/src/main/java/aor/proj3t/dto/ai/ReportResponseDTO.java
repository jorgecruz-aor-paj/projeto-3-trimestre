package aor.proj3t.dto.ai;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Report Generation Response
 */
@XmlRootElement(name = "report_response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "average_score")
    private float averageScore;

    @XmlElement(name = "report")
    private String report;

    public ReportResponseDTO() {
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}

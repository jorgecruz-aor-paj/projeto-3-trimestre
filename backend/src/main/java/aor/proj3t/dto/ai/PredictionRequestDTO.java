package aor.proj3t.dto.ai;

import java.io.Serializable;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Performance Prediction Request
 */
@XmlRootElement(name = "prediction_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class PredictionRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "historical_scores")
    private List<Float> historicalScores;

    @XmlElement(name = "extra_feature")
    private Float extraFeature;

    public PredictionRequestDTO() {
    }

    public PredictionRequestDTO(List<Float> historicalScores) {
        this.historicalScores = historicalScores;
    }

    public PredictionRequestDTO(List<Float> historicalScores, Float extraFeature) {
        this.historicalScores = historicalScores;
        this.extraFeature = extraFeature;
    }

    public List<Float> getHistoricalScores() {
        return historicalScores;
    }

    public void setHistoricalScores(List<Float> historicalScores) {
        this.historicalScores = historicalScores;
    }

    public Float getExtraFeature() {
        return extraFeature;
    }

    public void setExtraFeature(Float extraFeature) {
        this.extraFeature = extraFeature;
    }
}

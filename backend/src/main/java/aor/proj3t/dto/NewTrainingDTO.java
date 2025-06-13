package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "newTraining")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewTrainingDTO {

    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "duration")
    private Integer duration;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "link")
    private String link;
    @XmlElement(name = "language")
    private String language;
    @XmlElement(name = "trainingAreaId")
    private Long trainingAreaId;

    public NewTrainingDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getTrainingAreaId() {
        return trainingAreaId;
    }

    public void setTrainingAreaId(Long trainingAreaId) {
        this.trainingAreaId = trainingAreaId;
    }
}

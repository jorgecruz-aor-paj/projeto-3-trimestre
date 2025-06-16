package aor.proj3t.filters;

import aor.proj3t.enums.TrainingSortField;
import jakarta.ws.rs.QueryParam;

public class TrainingFilter {
    @QueryParam("id")
    private Long id;

    @QueryParam("trainingAreaId")
    private Long trainingAreaId;

    @QueryParam("language")
    private String language;

    @QueryParam("duration")
    private Integer duration;

    @QueryParam("startDate")
    private String startDate;

    @QueryParam("sortField")
    private TrainingSortField sortField = TrainingSortField.DATE;

    @QueryParam("sortAscending")
    private Boolean sortAscending = false;

    @QueryParam("searchText")
    private String searchText;

    public TrainingFilter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainingAreaId() {
        return trainingAreaId;
    }

    public void setTrainingAreaId(Long trainingAreaId) {
        this.trainingAreaId = trainingAreaId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public TrainingSortField getSortField() {
        return sortField;
    }

    public void setSortField(TrainingSortField sortField) {
        this.sortField = sortField;
    }

    public Boolean getSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(Boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return "TrainingFilter{" +
                "id=" + id +
                ", trainingAreaId=" + trainingAreaId +
                ", language='" + language + '\'' +
                ", duration=" + duration +
                ", startDate='" + startDate + '\'' +
                ", sortField=" + sortField +
                ", sortAscending=" + sortAscending +
                ", searchText='" + searchText + '\'' +
                '}';
    }
}

package aor.proj3t.filters;

import aor.proj3t.entities.TrainingArea;
import aor.proj3t.enums.TrainingSortField;

public class TrainingFilter {
    private Long trainingId;
    private TrainingArea trainingArea;
    private String language;
    private Integer duration;
    private String startDate;
    private TrainingSortField sortField = TrainingSortField.DATE;
    private Boolean sortAscending = false;
    private String searchText;

    public TrainingFilter() {}

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public TrainingArea getTrainingArea() {
        return trainingArea;
    }

    public void setTrainingArea(TrainingArea trainingArea) {
        this.trainingArea = trainingArea;
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
}

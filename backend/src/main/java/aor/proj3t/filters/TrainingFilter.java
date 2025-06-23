package aor.proj3t.filters;

import aor.proj3t.enums.TrainingSortField;
import aor.proj3t.exceptions.BadRequestException;
import jakarta.ws.rs.QueryParam;

import java.time.Instant;
import java.time.format.DateTimeParseException;

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

    @QueryParam("activeOnly")
    private Boolean activeOnly = true;

    @QueryParam("sortField")
    private TrainingSortField sortField = TrainingSortField.DATE;

    @QueryParam("sortAscending")
    private Boolean sortAscending = false;

    @QueryParam("searchText")
    private String searchText;


    private Instant startDateInstant;

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

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public Instant getStartDateInstant() {
        return startDateInstant;
    }

    public void setStartDateInstant(Instant startDateInstant) {
        this.startDateInstant = startDateInstant;
    }

    public void hydrate() {
        if (startDate != null && !startDate.isEmpty()) {
            try {
                this.startDateInstant = Instant.parse(startDate); // expecting ISO-8601 format like "2025-06-23T08:30:00Z"
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid startDate or endDate format. Use ISO-8601 format like 2025-06-23T08:30:00Z.");
            }
        }
        if (this.sortAscending == null) {
            this.sortAscending = false;
        }

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
                ", activeOnly=" + activeOnly +
                '}';
    }
}

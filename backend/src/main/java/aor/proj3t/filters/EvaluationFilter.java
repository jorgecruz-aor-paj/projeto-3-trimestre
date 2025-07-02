package aor.proj3t.filters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationSortField;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.exceptions.BadRequestException;
import jakarta.ws.rs.QueryParam;

public class EvaluationFilter {
    @QueryParam("evaluationId")
    private Long evaluationId;
    @QueryParam("evaluatedId")
    private Long evaluatedId;
    @QueryParam("evaluatorId")
    private Long evaluatorId;
    @QueryParam("evaluationStatus")
    private EvaluationStatus evaluationStatus;
    @QueryParam("date")
    private String dateString;
    @QueryParam("evaluationScale")
    private EvaluationScale evaluationScale;
    @QueryParam("evaluationSortField")
    private EvaluationSortField evaluationSortField = EvaluationSortField.DATE;
    @QueryParam("ascending")
    private Boolean ascending = false;
    @QueryParam("searchText")
    private String searchText;
    @QueryParam("startDate")
    private String startDate;
    @QueryParam("endDate")
    private String endDate;

    private LocalDate date;
    private DateRange dateRange;

    public EvaluationFilter() {
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getEvaluatedId() {
        return evaluatedId;
    }

    public void setEvaluatedId(Long evaluatedId) {
        this.evaluatedId = evaluatedId;
    }

    public Long getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public EvaluationStatus getEvaluationStatus() {
        return evaluationStatus;
    }

    public void setEvaluationStatus(EvaluationStatus evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public EvaluationScale getEvaluationScale() {
        return evaluationScale;
    }

    public void setEvaluationScale(EvaluationScale evaluationScale) {
        this.evaluationScale = evaluationScale;
    }

    public EvaluationSortField getEvaluationSortField() {
        return evaluationSortField;
    }

    public void setEvaluationSortField(EvaluationSortField evaluationSortField) {
        this.evaluationSortField = evaluationSortField;
    }

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getEndDate() {
        return endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getStartDate() {
        return startDate;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void hydrate() {
        // Hydrate the single date (from dateString to LocalDate)
        if (dateString != null && !dateString.isEmpty()) {
            try {
                this.date = LocalDate.parse(dateString); // default format: yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid date format. Expected yyyy-MM-dd.");
            }
        }

        // Hydrate the DateRange (from Strings to Instant range)
        if (startDate != null || endDate != null) {
            Instant start = null;
            Instant end = null;

            try {
                if (startDate != null && !startDate.isEmpty()) {
                    start = Instant.parse(startDate); // ISO-8601 format expected (e.g., 2025-06-23T08:30:00Z)
                    if(endDate == null || endDate.isEmpty()) {
                        end = Instant.now();
                    }
                }
                if (endDate != null && !endDate.isEmpty()) {
                    end = Instant.parse(endDate);
                    if(start == null || startDate.isEmpty()) {
                        start = Instant.MIN;
                    }
                }
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid startDate or endDate format. Use ISO-8601 format like 2025-06-23T08:30:00Z.");
            }

            this.dateRange = new DateRange(start, end);
        }
    }

    @Override
    public String toString() {
        return "EvaluationFilter{" +
                "evaluationId=" + evaluationId +
                ", evaluatedId=" + evaluatedId +
                ", evaluatorId=" + evaluatorId +
                ", evaluationStatus=" + evaluationStatus +
                ", dateString='" + dateString + '\'' +
                ", evaluationScale=" + evaluationScale +
                ", evaluationSortField=" + evaluationSortField +
                ", ascending=" + ascending +
                ", searchText='" + searchText + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", date=" + date +
                ", dateRange=" + dateRange +
                '}';
    }
}

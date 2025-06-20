package aor.proj3t.filters;

import java.time.LocalDate;

import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationSortField;
import aor.proj3t.enums.EvaluationStatus;
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
    private LocalDate date;
    @QueryParam("evaluationScale")
    private EvaluationScale evaluationScale;
    @QueryParam("evaluationSortField")
    private EvaluationSortField evaluationSortField = EvaluationSortField.DATE;
    @QueryParam("ascending")
    private Boolean ascending = false;
    @QueryParam("searchText")
    private String searchText;
    @QueryParam("dateRange")
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
}

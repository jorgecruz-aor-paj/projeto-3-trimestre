package aor.proj3t.filters;

import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationSortField;
import aor.proj3t.enums.EvaluationStatus;

import java.time.LocalDate;

public class EvaluationFilter {
private Long evaluationId;
private Long evaluatedId;
private Long evaluatorId;
private EvaluationStatus evaluationStatus;
private LocalDate date;
private EvaluationScale evaluationScale;
private EvaluationSortField evaluationSortField;
private Boolean ascending = true;
private DateRange dateRange;
private String searchText;

public EvaluationFilter() {}

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

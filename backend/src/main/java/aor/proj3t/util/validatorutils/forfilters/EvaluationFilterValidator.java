package aor.proj3t.util.validatorutils.forfilters;

import java.util.List;

import aor.proj3t.enums.EvaluationSortField;
import aor.proj3t.filters.EvaluationFilter;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Validator for EvaluationFilter objects.
 * Validates all fields in EvaluationFilter according to business rules.
 */
@ApplicationScoped
public class EvaluationFilterValidator extends BaseFilterValidator<EvaluationFilter> {

    private static final int MAX_SEARCH_TEXT_LENGTH = 255;

    @Override
    protected void doValidate(EvaluationFilter filter, List<String> validationErrors) {
        validateIds(filter, validationErrors);
        validateSearchText(filter, validationErrors);
        validateDateRange(filter, validationErrors);
        validateSortField(filter);
    }

    private void validateIds(EvaluationFilter filter, List<String> validationErrors) {
        if (filter.getEvaluationId() != null && filter.getEvaluationId() < 1) {
            validationErrors.add("Evaluation ID must be a positive number");
        }

        if (filter.getEvaluatedId() != null && filter.getEvaluatedId() < 1) {
            validationErrors.add("Evaluated user ID must be a positive number");
        }

        if (filter.getEvaluatorId() != null && filter.getEvaluatorId() < 1) {
            validationErrors.add("Evaluator ID must be a positive number");
        }
    }

    private void validateSearchText(EvaluationFilter filter, List<String> validationErrors) {
        String searchText = filter.getSearchText();
        if (searchText != null && searchText.length() > MAX_SEARCH_TEXT_LENGTH) {
            validationErrors.add("Search text cannot exceed 255 characters");
        }
    }

    private void validateDateRange(EvaluationFilter filter, List<String> validationErrors) {
        if (filter.getDateRange() != null) {
            if (filter.getDateRange().getStartDate() != null &&
                    filter.getDateRange().getEndDate() != null &&
                    filter.getDateRange().getStartDate().isAfter(filter.getDateRange().getEndDate())) {
                validationErrors.add("Start date cannot be after end date");
            }
        }
    }

    private void validateSortField(EvaluationFilter filter) {
        if (filter.getEvaluationSortField() == null) {
            filter.setEvaluationSortField(EvaluationSortField.DATE); // Set default if null
        }
    }
}

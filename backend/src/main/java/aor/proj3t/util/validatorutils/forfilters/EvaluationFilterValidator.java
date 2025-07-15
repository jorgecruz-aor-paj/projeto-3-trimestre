package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.enums.EvaluationSortField;
import aor.proj3t.filters.EvaluationFilter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Validator for {@link EvaluationFilter} objects.
 * <p>
 * Performs custom business rule validation on fields such as IDs, search text length,
 * date ranges, and default sort field assignment. Extends {@link BaseFilterValidator}
 * to ensure both Bean Validation and domain-specific rules are applied consistently.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class EvaluationFilterValidator extends BaseFilterValidator<EvaluationFilter> {

    private static final int MAX_SEARCH_TEXT_LENGTH = 255;

    /**
     * Performs additional validation logic specific to {@link EvaluationFilter} fields.
     *
     * @param filter the filter object to validate
     * @param validationErrors a list to collect validation error messages
     */
    @Override
    protected void doValidate(EvaluationFilter filter, List<String> validationErrors) {
        validateIds(filter, validationErrors);
        validateSearchText(filter, validationErrors);
        validateDateRange(filter, validationErrors);
        validateSortField(filter);
    }

    /**
     * Validates that evaluation-related IDs are positive if present.
     *
     * @param filter the evaluation filter
     * @param validationErrors the collection of errors to append to
     */
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

    /**
     * Validates that search text length does not exceed maximum bounds.
     *
     * @param filter the evaluation filter
     * @param validationErrors the collection of errors to append to
     */
    private void validateSearchText(EvaluationFilter filter, List<String> validationErrors) {
        String searchText = filter.getSearchText();
        if (searchText != null && searchText.length() > MAX_SEARCH_TEXT_LENGTH) {
            validationErrors.add("Search text cannot exceed 255 characters");
        }
    }

    /**
     * Validates that the date range, if specified, has valid start and end dates.
     *
     * @param filter the evaluation filter
     * @param validationErrors the collection of errors to append to
     */
    private void validateDateRange(EvaluationFilter filter, List<String> validationErrors) {
        if (filter.getDateRange() != null) {
            if (filter.getDateRange().getStartDate() != null &&
                filter.getDateRange().getEndDate() != null &&
                filter.getDateRange().getStartDate().isAfter(filter.getDateRange().getEndDate())) {
                validationErrors.add("Start date cannot be after end date");
            }
        }
    }

    /**
     * Ensures that a sort field is always set, assigning a default if necessary.
     *
     * @param filter the evaluation filter to update
     */
    private void validateSortField(EvaluationFilter filter) {
        if (filter.getEvaluationSortField() == null) {
            filter.setEvaluationSortField(EvaluationSortField.DATE); // Set default if null
        }
    }
}

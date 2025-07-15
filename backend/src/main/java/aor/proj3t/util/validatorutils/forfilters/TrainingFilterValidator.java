package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.enums.TrainingSortField;
import aor.proj3t.filters.TrainingFilter;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Validator for {@link TrainingFilter} objects.
 * <p>
 * Enforces business rules related to training filters, including ID checks,
 * duration bounds, date format compliance, and default sorting behavior.
 * It extends {@link BaseFilterValidator} to maintain a unified validation strategy
 * that incorporates both bean-level and custom logic.
 * </p>
 * Examples of invalid input include negative IDs, overly long search text,
 * incorrect ISO-8601 date formats, and durations exceeding limits.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class TrainingFilterValidator extends BaseFilterValidator<TrainingFilter> {

    private static final int MAX_SEARCH_TEXT_LENGTH = 255;
    private static final int MIN_DURATION = 1;
    private static final int MAX_DURATION = 1000;

    /**
     * Applies business logic validations to a {@link TrainingFilter} instance.
     *
     * @param filter the filter to validate
     * @param validationErrors a list used to collect validation error messages
     */
    @Override
    protected void doValidate(TrainingFilter filter, List<String> validationErrors) {
        validateId(filter, validationErrors);
        validateTrainingAreaId(filter, validationErrors);
        validateLanguage(filter, validationErrors);
        validateDuration(filter, validationErrors);
        validateStartDate(filter, validationErrors);
        validateSearchText(filter, validationErrors);
        validateSortField(filter);
    }

    /**
     * Validates that the filter ID is a positive number if provided.
     */
    private void validateId(TrainingFilter filter, List<String> validationErrors) {
        if (filter.getId() != null && filter.getId() < 1) {
            validationErrors.add("ID must be a positive number");
        }
    }

    /**
     * Validates that the training area ID is a positive number if provided.
     */
    private void validateTrainingAreaId(TrainingFilter filter, List<String> validationErrors) {
        if (filter.getTrainingAreaId() != null && filter.getTrainingAreaId() < 1) {
            validationErrors.add("Training area ID must be a positive number");
        }
    }

    /**
     * Validates the training language value if present.
     * No need to validate further as enum values are assumed valid.
     */
    private void validateLanguage(TrainingFilter filter, List<String> validationErrors) {
        if (filter.getLanguage() == null) {
            return; // Optional field
        }
        // Validity is guaranteed by type
    }

    /**
     * Validates that the duration is within acceptable limits if specified.
     */
    private void validateDuration(TrainingFilter filter, List<String> validationErrors) {
        Integer duration = filter.getDuration();
        if (duration != null) {
            if (duration < MIN_DURATION) {
                validationErrors.add("Duration must be a positive number");
            } else if (duration > MAX_DURATION) {
                validationErrors.add("Duration cannot exceed 1000");
            }
        }
    }

    /**
     * Validates that the start date string is in correct ISO-8601 format.
     */
    private void validateStartDate(TrainingFilter filter, List<String> validationErrors) {
        String startDate = filter.getStartDate();
        if (startDate != null && !startDate.isEmpty()) {
            try {
                Instant.parse(startDate);
            } catch (DateTimeParseException e) {
                validationErrors.add("Start date must be in ISO-8601 format, e.g., 2025-07-01T09:00:00Z");
            }
        }
    }

    /**
     * Validates that the search text does not exceed the character limit.
     */
    private void validateSearchText(TrainingFilter filter, List<String> validationErrors) {
        String searchText = filter.getSearchText();
        if (searchText != null && searchText.length() > MAX_SEARCH_TEXT_LENGTH) {
            validationErrors.add("Search text cannot exceed 255 characters");
        }
    }

    /**
     * Ensures a sort field is set, assigning a default if necessary.
     */
    private void validateSortField(TrainingFilter filter) {
        if (filter.getSortField() == null) {
            filter.setSortField(TrainingSortField.DATE);
        }
    }
}


package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.enums.UserSortField;
import aor.proj3t.filters.UserFilter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Validator for {@link UserFilter} objects.
 * <p>
 * Enforces business rules and structural integrity for user filter fields,
 * including ID positivity checks, search text length limits, and default sort field assignment.
 * This class extends {@link BaseFilterValidator} to integrate both Bean Validation
 * and custom logic for flexible and robust filter validation.
 * </p>
 * Typical use cases include validating search queries for user listings or enforcing constraints
 * before applying filters to a data source.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class UserFilterValidator extends BaseFilterValidator<UserFilter> {

    private static final int MAX_SEARCH_TEXT_LENGTH = 255;

    /**
     * Applies custom validation rules to the {@link UserFilter}.
     *
     * @param filter the user filter object to validate
     * @param validationErrors list to collect any validation error messages
     */
    @Override
    protected void doValidate(UserFilter filter, List<String> validationErrors) {
        validateId(filter, validationErrors);
        validateSearchText(filter, validationErrors);
        validateSortField(filter);
    }

    /**
     * Validates that the user ID, if present, is a positive number.
     */
    private void validateId(UserFilter filter, List<String> validationErrors) {
        if (filter.getId() != null && filter.getId() < 1) {
            validationErrors.add("ID must be a positive number");
        }
    }

    /**
     * Validates the search text length, ensuring it does not exceed the maximum allowed.
     */
    private void validateSearchText(UserFilter filter, List<String> validationErrors) {
        String searchText = filter.getSearchText();
        if (searchText != null && searchText.length() > MAX_SEARCH_TEXT_LENGTH) {
            validationErrors.add("Search text cannot exceed 255 characters");
        }
    }

    /**
     * Assigns a default sort field if none was explicitly provided.
     */
    private void validateSortField(UserFilter filter) {
        if (filter.getSortField() == null) {
            filter.setSortField(UserSortField.NAME);
        }
    }
}


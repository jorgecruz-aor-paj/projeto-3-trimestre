package aor.proj3t.util.validatorutils.forfilters;

import java.util.List;

import aor.proj3t.enums.UserSortField;
import aor.proj3t.filters.UserFilter;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Validator for UserFilter objects.
 * Validates all fields in UserFilter according to business rules.
 */
@ApplicationScoped
public class UserFilterValidator extends BaseFilterValidator<UserFilter> {

    private static final int MAX_SEARCH_TEXT_LENGTH = 255;

    @Override
    protected void doValidate(UserFilter filter, List<String> validationErrors) {
        validateId(filter, validationErrors);
        validateSearchText(filter, validationErrors);
        validateSortField(filter);
    }

    private void validateId(UserFilter filter, List<String> validationErrors) {
        if (filter.getId() != null && filter.getId() < 1) {
            validationErrors.add("ID must be a positive number");
        }
    }

    private void validateSearchText(UserFilter filter, List<String> validationErrors) {
        String searchText = filter.getSearchText();
        if (searchText != null && searchText.length() > MAX_SEARCH_TEXT_LENGTH) {
            validationErrors.add("Search text cannot exceed 255 characters");
        }
    }

    private void validateSortField(UserFilter filter) {
        if (filter.getSortField() == null) {
            filter.setSortField(UserSortField.NAME); // Set default if null
        }
    }
}

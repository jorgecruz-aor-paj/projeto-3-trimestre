package aor.proj3t.enums;

/**
 * Enum representing the available fields for sorting user records.
 * Each field corresponds to a specific user property that can be used
 * to order user results in queries and listings.
 * 
 * @author Project Team
 * @version 1.0
 * @since 1.0
 */
public enum UserSortField {

    /** Sort by user ID */
    ID("id"),

    /** Sort by user workplace */
    WORKPLACE("workplace"),

    /** Sort by user's manager */
    MANAGER("manager"),

    /** Sort by user's first name */
    NAME("firstName"),

    /** Sort by user's date of birth */
    DATE_OF_BIRTH("birthdate"),

    /** Sort by user creation date */
    DATE("createdAt");

    /** The database field name used for sorting */
    private final String fieldName;

    /**
     * Constructor for UserSortField enum values.
     * 
     * @param fieldName The database field name for sorting
     */
    UserSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Gets the database field name for this sort field.
     * 
     * @return The field name used in database queries
     */
    public String getFieldName() {
        return fieldName;
    }
}

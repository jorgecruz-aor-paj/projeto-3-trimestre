package aor.proj3t.enums;

import aor.proj3t.exceptions.NotFoundException;

/**
 * Enum representing the available fields for sorting training records.
 * Each field corresponds to a specific property that can be used
 * to order training results in queries. Each sort field has both
 * a unique identifier and a corresponding database field name.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public enum TrainingSortField {

    /** Sort by training ID */
    TRAINING_ID(1L, "id"),

    /** Sort by training title */
    TITLE(2L, "title"),

    /** Sort by creation date */
    DATE(3L, "createdAt"),

    /** Sort by training duration */
    DURATION(4L, "duration"),

    /** Sort by training language */
    LANGUAGE(5L, "language"),

    /** Sort by training area/category */
    AREA(6L, "trainingArea");

    /** The unique identifier for this sort field */
    private final Long id;

    /** The database field name used for sorting */
    private final String fieldName;

    /**
     * Constructor for TrainingSortField enum values.
     * 
     * @param id        The unique identifier for this sort field
     * @param fieldName The database field name for sorting
     */
    TrainingSortField(Long id, String fieldName) {
        this.id = id;
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

    /**
     * Gets the unique identifier for this sort field.
     * 
     * @return The unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Finds a TrainingSortField by its database field name.
     * 
     * @param fieldName The field name to search for
     * @return The TrainingSortField with the matching field name
     * @throws NotFoundException if no sort field is found for the given field name
     */
    public static TrainingSortField getByFieldName(String fieldName) {
        for (TrainingSortField trainingSortField : TrainingSortField.values()) {
            if (trainingSortField.getFieldName().equals(fieldName)) {
                return trainingSortField;
            }
        }
        throw new NotFoundException(
                "Invalid sort field",
                "No training sort field found for field name: " + fieldName);
    }

    /**
     * Finds a TrainingSortField by its unique identifier.
     * 
     * @param id The ID to search for
     * @return The TrainingSortField with the matching ID
     * @throws NotFoundException if no sort field is found for the given ID
     */
    public static TrainingSortField getById(Long id) {
        for (TrainingSortField trainingSortField : TrainingSortField.values()) {
            if (trainingSortField.getId().equals(id)) {
                return trainingSortField;
            }
        }
        throw new NotFoundException(
                "Invalid sort field",
                "No training sort field found for id: " + id);
    }
}

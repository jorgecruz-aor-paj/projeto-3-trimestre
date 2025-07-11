package aor.proj3t.enums;

/**
 * Enum representing the available fields for sorting evaluations.
 * Each field corresponds to a specific property that can be used
 * to order evaluation results in queries.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public enum EvaluationSortField {

    /** Sort by evaluation ID */
    EVALUATION_ID("id"),

    /** Sort by the ID of the person being evaluated */
    EVALUATED_ID("evaluated"),

    /** Sort by the ID of the person performing the evaluation */
    EVALUATOR_ID("evaluator"),

    /** Sort by evaluation status */
    STATUS("status"),

    /** Sort by creation date */
    DATE("createdAt"),

    /** Sort by evaluation scale/score */
    SCALE("scale");

    /** The field name used in database queries */
    private final String fieldName;

    /**
     * Constructor for EvaluationSortField enum values.
     * 
     * @param fieldName The database field name for sorting
     */
    EvaluationSortField(String fieldName) {
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

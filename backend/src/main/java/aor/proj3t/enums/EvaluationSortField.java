package aor.proj3t.enums;

public enum EvaluationSortField {

    EVALUATION_ID("id"),
    EVALUATED_ID("evaluated"),
    EVALUATOR_ID("evaluator"),
    STATUS("status"),
    DATE("createdAt"),
    SCALE("scale");

    private final String fieldName;

    EvaluationSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}



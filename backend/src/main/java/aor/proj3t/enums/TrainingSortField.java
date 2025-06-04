package aor.proj3t.enums;

public enum TrainingSortField {

    TRAINING_ID("id"),
    TITLE("title"),
    DATE("created_at"),
    DURATION("duration"),
    LANGUAGE("language"),
    AREA("training_area");


    private final String fieldName;

    TrainingSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

package aor.proj3t.enums;

import aor.proj3t.exceptions.NotFoundException;

public enum TrainingSortField {

    TRAINING_ID(1L, "id"),
    TITLE(2L, "title"),
    DATE(3L, "createdAt"),
    DURATION(4L, "duration"),
    LANGUAGE(5L, "language"),
    AREA(6L, "trainingArea");

    private final Long id;
    private final String fieldName;

    TrainingSortField(Long id, String fieldName) {
        this.id = id;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getId() {
        return id;
    }

    public static TrainingSortField getByFieldName(String fieldName) {
        for (TrainingSortField trainingSortField : TrainingSortField.values()) {
            if (trainingSortField.getFieldName().equals(fieldName)) {
                return trainingSortField;
            }
        }
        throw new NotFoundException(
                "Invalid sort field",
                "No training sort field found for field name: " + fieldName
        );
    }

    public static TrainingSortField getById(Long id) {
        for (TrainingSortField trainingSortField : TrainingSortField.values()) {
            if (trainingSortField.getId().equals(id)) {
                return trainingSortField;
            }
        }
        throw new NotFoundException(
                "Invalid sort field",
                "No training sort field found for id: " + id
        );
    }
}

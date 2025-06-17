package aor.proj3t.enums;

public enum EvaluationScale {
    LOW_CONTRIBUTION(1, "Contribuição baixa"),
    PARCIAL_CONTRIBUTION(2, "Contribuição parcial"),
    EXPECTED_CONTRIBUTION(3, "Contribuição conforme o esperado"),
    EXCEED_CONTRIBUTION(4, "Contribuição excedida");

    private Integer score;
    private String description;

    EvaluationScale(Integer score, String description) {
        this.score = score;
        this.description = description;
    }

    public Integer getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }

    public static EvaluationScale getByScore(Integer score) {
        for (EvaluationScale scale : EvaluationScale.values()) {
            if (scale.getScore().equals(score)) {
                return scale;
            }
        }
        throw new IllegalArgumentException("No EvaluationScale found for score: " + score);
    }

    public static EvaluationScale getByDescription(String description) {
        for (EvaluationScale scale : EvaluationScale.values()) {
            if (scale.getDescription().equals(description)) {
                return scale;
            }
        }
        throw new IllegalArgumentException("No EvaluationScale found for description: " + description);
    }
}


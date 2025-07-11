package aor.proj3t.enums;

/**
 * Enum representing the evaluation scale used to rate contributions.
 * Each scale level has an associated numeric score and Portuguese description.
 * The scale ranges from 1 (low contribution) to 4 (exceeded contribution).
 * 
 * @author Project Team
 * @version 1.0
 * @since 1.0
 */
public enum EvaluationScale {
    /** Low contribution level - score 1 */
    LOW_CONTRIBUTION(1, "Contribuição baixa"),

    /** Partial contribution level - score 2 */
    PARCIAL_CONTRIBUTION(2, "Contribuição parcial"),

    /** Expected contribution level - score 3 */
    EXPECTED_CONTRIBUTION(3, "Contribuição conforme o esperado"),

    /** Exceeded contribution level - score 4 */
    EXCEED_CONTRIBUTION(4, "Contribuição excedida");

    /** The numeric score associated with this evaluation level */
    private final Integer score;

    /** The Portuguese description of this evaluation level */
    private final String description;

    /**
     * Constructor for EvaluationScale enum values.
     * 
     * @param score       The numeric score for this evaluation level
     * @param description The Portuguese description for this evaluation level
     */
    EvaluationScale(Integer score, String description) {
        this.score = score;
        this.description = description;
    }

    /**
     * Gets the numeric score for this evaluation level.
     * 
     * @return The numeric score (1-4)
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Gets the Portuguese description for this evaluation level.
     * 
     * @return The description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Finds an EvaluationScale by its numeric score.
     * 
     * @param score The numeric score to search for
     * @return The EvaluationScale with the matching score
     * @throws IllegalArgumentException if no scale is found for the given score
     */
    public static EvaluationScale getByScore(Integer score) {
        for (EvaluationScale scale : EvaluationScale.values()) {
            if (scale.getScore().equals(score)) {
                return scale;
            }
        }
        throw new IllegalArgumentException("No EvaluationScale found for score: " + score);
    }

    /**
     * Finds an EvaluationScale by its Portuguese description.
     * 
     * @param description The description text to search for
     * @return The EvaluationScale with the matching description
     * @throws IllegalArgumentException if no scale is found for the given
     *                                  description
     */
    public static EvaluationScale getByDescription(String description) {
        for (EvaluationScale scale : EvaluationScale.values()) {
            if (scale.getDescription().equals(description)) {
                return scale;
            }
        }
        throw new IllegalArgumentException("No EvaluationScale found for description: " + description);
    }
}

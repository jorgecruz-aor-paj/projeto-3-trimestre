package aor.proj3t.enums;

/**
 * Enum representing the supported languages for trainings.
 * This enum provides a standardized way to handle multiple languages
 * in the training system, with utility methods for string conversion.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public enum TrainingLanguage {
    /** English language */
    ENGLISH,

    /** Portuguese language */
    PORTUGUESE,

    /** Spanish language */
    SPANISH,

    /** French language */
    FRENCH,

    /** German language */
    GERMAN,

    /** Italian language */
    ITALIAN,

    /** Dutch language */
    DUTCH;

    /**
     * Converts a string to the corresponding enum value.
     * This method performs case-insensitive matching and handles null/empty inputs.
     * 
     * @param name the language name to convert
     * @return the TrainingLanguage enum value, or null if not found or input is
     *         invalid
     */
    public static TrainingLanguage fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try {
            // Try to convert directly with toUpperCase
            return TrainingLanguage.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            // If that fails, try to match case-insensitively
            for (TrainingLanguage lang : TrainingLanguage.values()) {
                if (lang.name().equalsIgnoreCase(name)) {
                    return lang;
                }
            }
            return null;
        }
    }
}

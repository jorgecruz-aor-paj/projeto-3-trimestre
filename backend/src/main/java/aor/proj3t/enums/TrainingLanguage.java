package aor.proj3t.enums;

/**
 * Enum representing the supported languages for trainings
 */
public enum TrainingLanguage {
    ENGLISH,
    PORTUGUESE,
    SPANISH,
    FRENCH,
    GERMAN,
    ITALIAN,
    DUTCH;
    
    /**
     * Converts a string to the corresponding enum value
     * @param name the language name
     * @return the TrainingLanguage enum value, or null if not found
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

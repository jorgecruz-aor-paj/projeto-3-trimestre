package aor.proj3t.converters;

import aor.proj3t.enums.TrainingLanguage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter for TrainingLanguage enum to handle case-sensitivity issues in the database
 */
@Converter
public class TrainingLanguageConverter implements AttributeConverter<TrainingLanguage, String> {

    @Override
    public String convertToDatabaseColumn(TrainingLanguage attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TrainingLanguage convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }

        // Try to match directly first
        try {
            return TrainingLanguage.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            // If direct match fails, try case-insensitive matching
            for (TrainingLanguage lang : TrainingLanguage.values()) {
                if (lang.name().equalsIgnoreCase(dbData)) {
                    return lang;
                }
            }
            
            // If all else fails, throw the exception
            throw new IllegalArgumentException("Unknown language: " + dbData);
        }
    }
}

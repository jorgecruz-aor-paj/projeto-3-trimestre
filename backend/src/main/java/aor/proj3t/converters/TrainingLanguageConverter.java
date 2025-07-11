package aor.proj3t.converters;

import aor.proj3t.enums.TrainingLanguage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Attribute Converter for TrainingLanguage Enum.
 * 
 * This converter handles the mapping between {@link TrainingLanguage} enum
 * values
 * and their string representations in the database. It provides
 * case-insensitive
 * conversion to handle potential data inconsistencies.
 * 
 * <p>
 * The converter automatically converts enum values to their string names when
 * storing to the database, and attempts both exact and case-insensitive
 * matching
 * when converting back from database strings to enum values.
 * </p>
 * 
 * <p>
 * This converter is automatically applied to all {@link TrainingLanguage}
 * attributes in JPA entities.
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see TrainingLanguage
 * @see AttributeConverter
 */
@Converter
public class TrainingLanguageConverter implements AttributeConverter<TrainingLanguage, String> {

    /**
     * Converts a TrainingLanguage enum to its database column representation.
     * 
     * @param attribute the TrainingLanguage enum value to convert, may be null
     * @return the string representation of the enum (enum name), or null if input
     *         is null
     */
    @Override
    public String convertToDatabaseColumn(TrainingLanguage attribute) {
        return attribute == null ? null : attribute.name();
    }

    /**
     * Converts a database column value back to a TrainingLanguage enum.
     * 
     * <p>
     * This method first attempts an exact match with the enum values.
     * If that fails, it performs a case-insensitive search through all
     * enum values to handle potential case inconsistencies in the database.
     * </p>
     * 
     * @param dbData the string value from the database, may be null or empty
     * @return the corresponding TrainingLanguage enum value, or null if input is
     *         null/empty
     * @throws IllegalArgumentException if the database value doesn't match any enum
     *                                  value
     */
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

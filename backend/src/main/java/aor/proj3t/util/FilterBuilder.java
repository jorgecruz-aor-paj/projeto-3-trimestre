package aor.proj3t.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for building filter maps to use with DAO search methods.
 * <p>
 * This builder provides a fluent API for constructing filter maps, making
 * code more readable and reducing repetition.
 * </p>
 */
public class FilterBuilder {
    private final Map<String, Object> filters = new HashMap<>();

    /**
     * Creates a new FilterBuilder instance.
     * 
     * @return A new FilterBuilder instance
     */
    public static FilterBuilder create() {
        return new FilterBuilder();
    }

    /**
     * Adds a filter criterion if the value is not null.
     * 
     * @param key   The filter key
     * @param value The filter value
     * @return This FilterBuilder instance for method chaining
     */
    public FilterBuilder with(String key, Object value) {
        if (value != null) { // Only add non-null values
            filters.put(key, value);
        }
        return this;
    }

    /**
     * Builds and returns the completed filters map.
     * 
     * @return The filters map
     */
    public Map<String, Object> build() {
        return filters;
    }
}
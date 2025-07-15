package aor.proj3t.util;

import java.util.HashMap;
import java.util.Map;
/**
 * Utility class for building filter maps for DAO search queries.
 * <p>
 * Offers a fluent and expressive API for composing query filters as key-value pairs.
 * Filters are conditionally added only if their values are non-null, promoting clean
 * and concise controller or service logic when assembling queries.
 * Common use cases include building dynamic search parameters or simplifying DAO lookups.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 *     Map<String, Object> filters = FilterBuilder.create()
 *         .with("status", "ACTIVE")
 *         .with("department", departmentId)
 *         .build();
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class FilterBuilder {
    private final Map<String, Object> filters = new HashMap<>();

    /**
     * Creates a new instance of {@link FilterBuilder}.
     *
     * @return a fresh {@link FilterBuilder} instance
     */
    public static FilterBuilder create() {
        return new FilterBuilder();
    }

    /**
     * Adds a filter key-value pair to the map if the value is not null.
     *
     * @param key the filter key (usually a field name or query parameter)
     * @param value the filter value to associate
     * @return this {@link FilterBuilder} instance for method chaining
     */
    public FilterBuilder with(String key, Object value) {
        if (value != null) {
            filters.put(key, value);
        }
        return this;
    }

    /**
     * Finalizes the filter map and returns it.
     *
     * @return an immutable {@link Map} of filter criteria
     */
    public Map<String, Object> build() {
        return filters;
    }
}

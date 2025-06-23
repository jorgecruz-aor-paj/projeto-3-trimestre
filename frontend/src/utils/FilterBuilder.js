/**
 * A functional utility for building search filters in a fluent API style.
 * This approach mirrors the backend FilterBuilder pattern but uses a more
 * modern functional approach instead of classes.
 */

/**
 * Creates a new filter builder with fluent API
 * @returns {Object} Filter builder object with chainable methods
 */
export const createFilterBuilder = () => {
  // Use closure to store the filter state
  const filters = {};

  return {
    /**
     * Adds a filter parameter if the value is not null or undefined
     * @param {string} key - The filter key
     * @param {any} value - The filter value
     * @returns {Object} The filter builder for method chaining
     */
    with(key, value) {
      if (value !== null && value !== undefined) {
        filters[key] = value;
      }
      return this;
    },

    /**
     * Builds and returns the completed filters object
     * @returns {Object} The filters object
     */
    build() {
      return { ...filters };
    },
  };
};

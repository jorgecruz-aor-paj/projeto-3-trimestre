/**
 * @fileoverview Functional utility for building search filters with fluent API
 * @module utils/FilterBuilder
 */

/**
 * @typedef {Object} FilterBuilder
 * @property {function} with - Add a filter parameter if value is not null/undefined
 * @property {function} build - Build and return the completed filters object
 */

/**
 * A functional utility for building search filters in a fluent API style.
 *
 * This approach mirrors the backend FilterBuilder pattern but uses a more
 * modern functional approach instead of classes. It provides a clean, chainable
 * API for building complex filter objects while automatically handling null/undefined
 * values to prevent unnecessary parameters in API calls.
 *
 * The FilterBuilder uses closure to maintain state and provides methods for:
 * - Adding conditional filters (only if value is not null/undefined)
 * - Building the final filter object
 * - Chaining multiple filter conditions
 *
 * @function createFilterBuilder
 * @returns {FilterBuilder} Filter builder object with chainable methods
 *
 * @example
 * // Basic usage with simple filters
 * import { createFilterBuilder } from 'utils/FilterBuilder';
 *
 * const filters = createFilterBuilder()
 *   .with('search', 'john')
 *   .with('role', 'MANAGER')
 *   .with('active', true)
 *   .build();
 *
 * console.log(filters); // { search: 'john', role: 'MANAGER', active: true }
 *
 * @example
 * // Conditional filters (null/undefined values are ignored)
 * const searchTerm = ''; // Empty string
 * const selectedRole = null; // Null value
 * const isActive = true;
 *
 * const filters = createFilterBuilder()
 *   .with('search', searchTerm || null) // Will be ignored
 *   .with('role', selectedRole) // Will be ignored
 *   .with('active', isActive) // Will be included
 *   .build();
 *
 * console.log(filters); // { active: true }
 *
 * @example
 * // Building complex filters for API calls
 * import { userService } from 'services';
 *
 * function searchUsers(searchParams) {
 *   const filters = createFilterBuilder()
 *     .with('search', searchParams.query)
 *     .with('role', searchParams.role)
 *     .with('workplace', searchParams.workplace)
 *     .with('managerId', searchParams.managerId)
 *     .with('active', searchParams.includeInactive ? undefined : true)
 *     .with('page', searchParams.page || 0)
 *     .with('size', searchParams.size || 10)
 *     .build();
 *
 *   return userService.getAll(filters);
 * }
 *
 * @example
 * // Reusable filter builder for different contexts
 * function createUserFilters(params) {
 *   return createFilterBuilder()
 *     .with('search', params.search)
 *     .with('role', params.role)
 *     .with('workplace', params.workplace)
 *     .with('active', params.active)
 *     .build();
 * }
 *
 * // Usage in different components
 * const adminFilters = createUserFilters({ role: 'ADMIN', active: true });
 * const managerFilters = createUserFilters({ role: 'MANAGER', workplace: 'HQ' });
 *
 * @example
 * // Integration with form state
 * import { useState } from 'react';
 *
 * function UserSearchForm() {
 *   const [searchForm, setSearchForm] = useState({
 *     query: '',
 *     role: '',
 *     workplace: '',
 *     active: true
 *   });
 *
 *   const handleSearch = () => {
 *     const filters = createFilterBuilder()
 *       .with('search', searchForm.query)
 *       .with('role', searchForm.role)
 *       .with('workplace', searchForm.workplace)
 *       .with('active', searchForm.active)
 *       .build();
 *
 *     // Use filters in API call
 *     userService.getAll(filters).then(handleResults);
 *   };
 *
 *   return (
 *     <form onSubmit={handleSearch}>
 *       // Form fields would go here
 *     </form>
 *   );
 * }
 *
 * @since 1.0.0
 * @author Development Team
 */
export const createFilterBuilder = () => {
  // Use closure to store the filter state
  const filters = {};

  return {
    /**
     * Adds a filter parameter if the value is not null or undefined
     *
     * This method provides conditional filtering - it only adds the parameter
     * to the filter object if the value is meaningful (not null or undefined).
     * This prevents sending unnecessary parameters to the API.
     *
     * @method with
     * @memberof FilterBuilder
     * @param {string} key - The filter key/parameter name
     * @param {*} value - The filter value (added only if not null/undefined)
     * @returns {FilterBuilder} The filter builder instance for method chaining
     *
     * @example
     * // Values that will be included
     * builder.with('search', 'john'); // ✓ Included
     * builder.with('active', true); // ✓ Included
     * builder.with('count', 0); // ✓ Included (0 is valid)
     * builder.with('empty', ''); // ✓ Included (empty string is valid)
     *
     * @example
     * // Values that will be ignored
     * builder.with('optional', null); // ✗ Ignored
     * builder.with('missing', undefined); // ✗ Ignored
     *
     * @example
     * // Conditional inclusion
     * const searchTerm = user.query || null;
     * builder.with('search', searchTerm); // Only included if query exists
     */
    with(key, value) {
      if (value !== null && value !== undefined) {
        filters[key] = value;
      }
      return this;
    },

    /**
     * Builds and returns the completed filters object
     *
     * This method finalizes the filter building process and returns
     * a new object containing all the added filters. The returned object
     * is a copy, so the original builder state is preserved.
     *
     * @method build
     * @memberof FilterBuilder
     * @returns {Object} The completed filters object with all added parameters
     *
     * @example
     * // Basic build
     * const filters = createFilterBuilder()
     *   .with('search', 'john')
     *   .with('role', 'ADMIN')
     *   .build();
     *
     * console.log(filters); // { search: 'john', role: 'ADMIN' }
     *
     * @example
     * // Empty filters (no parameters added)
     * const emptyFilters = createFilterBuilder().build();
     * console.log(emptyFilters); // {}
     *
     * @example
     * // Reusing builder after build
     * const builder = createFilterBuilder().with('base', 'value');
     * const filters1 = builder.with('extra1', 'value1').build();
     * const filters2 = builder.with('extra2', 'value2').build();
     *
     * // Both filters contain the base parameter plus their extras
     */
    build() {
      return { ...filters };
    },
  };
};

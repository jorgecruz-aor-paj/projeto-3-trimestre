/**
 * API Service
 * Base service for making API requests using axios
 */
import { axiosInstance } from 'api/axiosInstance';

/**
 * Makes an API request using the axios instance
 * @param {Object} endpoint - The endpoint configuration object from API_ENDPOINTS
 * @param {Object} options - Additional axios request options
 * @param {Object} pathParams - Parameters to replace in the URL (e.g., :id)
 * @returns {Promise} - The API response data
 */
export const apiRequest = (endpoint, options = {}, pathParams = {}) => {
  // If endpoint is a function (with params), call it to get the actual endpoint config
  const endpointConfig =
    typeof endpoint === 'function' ? endpoint(pathParams) : endpoint;

  // Error handling is done in the axiosInstance interceptor
  return axiosInstance({
    url: endpointConfig.url,
    ...options,
  });
};

/**
 * Generic CRUD operations
 */
export const apiService = {
  /**
   * Fetch a list of items
   * @param {Object} endpoint - The endpoint configuration
   * @param {Object} params - Query parameters
   */
  getAll: (endpoint, params = {}) => {
    return apiRequest(endpoint, {
      method: 'GET',
      params,
    });
  },

  /**
   * Fetch a single item by ID
   * @param {Function} endpointFn - Function that takes an ID and returns endpoint configuration
   * @param {String|Number} id - The item ID
   * @param {Object} params - Query parameters
   */
  getById: (endpointFn, id, params = {}) => {
    return apiRequest(
      endpointFn,
      {
        method: 'GET',
        params,
      },
      id
    );
  },

  /**
   * Create a new item
   * @param {Object} endpoint - The endpoint configuration
   * @param {Object} data - The data to create
   */
  create: (endpoint, data) => {
    return apiRequest(endpoint, {
      method: 'POST',
      data,
    });
  },

  /**
   * Update an existing item
   * @param {Function} endpointFn - Function that takes an ID and returns endpoint configuration
   * @param {String|Number} id - The item ID
   * @param {Object} data - The data to update
   */
  update: (endpointFn, id, data) => {
    return apiRequest(
      endpointFn,
      {
        method: 'PUT',
        data,
      },
      id
    );
  },

  /**
   * Delete an item
   * @param {Function} endpointFn - Function that takes an ID and returns endpoint configuration
   * @param {String|Number} id - The item ID
   */
  delete: (endpointFn, id) => {
    return apiRequest(
      endpointFn,
      {
        method: 'DELETE',
      },
      id
    );
  },
};

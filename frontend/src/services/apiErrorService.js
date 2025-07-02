import { useErrorHandler } from 'hooks/useErrorHandler';

/**
 * Example API service that demonstrates error handling
 * This shows how to integrate the error handling system with API calls
 */
export class ApiService {
  constructor(errorHandler = null) {
    this.errorHandler = errorHandler;
    this.baseURL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
  }

  /**
   * Generic fetch wrapper with error handling
   */
  async fetchWithErrorHandling(url, options = {}) {
    try {
      const response = await fetch(`${this.baseURL}${url}`, {
        headers: {
          'Content-Type': 'application/json',
          ...options.headers,
        },
        ...options,
      });

      if (!response.ok) {
        const error = new Error(
          `HTTP ${response.status}: ${response.statusText}`
        );
        error.response = response;
        error.status = response.status;
        throw error;
      }

      return await response.json();
    } catch (error) {
      // Handle different types of errors
      if (this.errorHandler) {
        this.errorHandler.handleError(error);
      }
      throw error;
    }
  }

  /**
   * Example method that might encounter different errors
   */
  getData(endpoint) {
    return this.fetchWithErrorHandling(endpoint);
  }

  /**
   * Example method that might encounter a 404
   */
  getUser(userId) {
    return this.fetchWithErrorHandling(`/users/${userId}`);
  }

  /**
   * Example method that might encounter a 403
   */
  getAdminData() {
    return this.fetchWithErrorHandling('/admin/data');
  }
}

/**
 * React hook for using the API service with error handling
 */
export const useApiService = () => {
  const errorHandler = useErrorHandler();

  // Create API service instance with error handler
  const apiService = new ApiService(errorHandler);

  return {
    apiService,
    errorHandler,
  };
};

/**
 * Higher-order function to wrap API calls with error handling
 */
export const withErrorHandling = apiCall => {
  return async (...args) => {
    try {
      return await apiCall(...args);
    } catch (error) {
      // You can add additional error processing here
      console.error('API call failed:', error);
      throw error;
    }
  };
};

/**
 * Utility function to handle different types of errors
 */
export const handleApiError = (error, errorHandler) => {
  if (error?.response?.status === 401) {
    // Handle unauthorized - redirect to login
    window.location.href = '/auth';
  } else if (error?.response?.status === 403) {
    errorHandler.handleForbidden();
  } else if (error?.response?.status === 404) {
    errorHandler.handleNotFound();
  } else if (error?.response?.status >= 500) {
    errorHandler.handleServerError();
  } else if (error?.name === 'NetworkError' || !error?.response) {
    errorHandler.handleNetworkError();
  } else {
    errorHandler.handleGenericError(error.message);
  }
};

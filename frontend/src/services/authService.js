/**
 * Auth Service
 * Handles authentication related API calls
 */
import { apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const authService = {
  /**
   * Login with email and password
   * @param {Object} credentials - User credentials {email, password}
   * @returns {Promise} - Auth data including token and user info
   */
  login: credentials => {
    return apiRequest(API_ENDPOINTS.auth.login, {
      method: 'POST',
      data: credentials,
    });
  },

  /**
   * Register a new user
   * @param {Object} userData - User registration data
   * @returns {Promise} - Created user data
   */
  register: userData => {
    return apiRequest(API_ENDPOINTS.auth.register, {
      method: 'POST',
      data: userData,
    });
  },

  /**
   * Logout the current user
   * @returns {Promise}
   */
  logout: () => {
    return apiRequest(API_ENDPOINTS.auth.logout, {
      method: 'POST',
    });
  },

  /**
   * Note: The refresh token endpoint is not currently implemented in the backend
   */
};

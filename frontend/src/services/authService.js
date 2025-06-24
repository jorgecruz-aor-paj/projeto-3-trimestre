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
   * Confirm user registration with token and user data
   * @param {String} confirmationToken - Token received via email
   * @param {Object} userData - User registration data including personal info
   * @returns {Promise}
   */
  confirmUser: (confirmationToken, userData) => {
    return apiRequest(
      `${API_ENDPOINTS.auth.confirm.url}?confirmationToken=${confirmationToken}`,
      {
        method: 'POST',
        data: userData,
      }
    );
  },

  /**
   * Request password reset link for a specific email
   * @param {String} email - User's email address
   * @returns {Promise}
   */
  requestPasswordReset: email => {
    return apiRequest(
      `${API_ENDPOINTS.auth.resetPasswordRequest.url.replace(
        '{email}',
        email
      )}`,
      {
        method: 'GET',
      }
    );
  },

  /**
   * Reset user password with token and new password
   * @param {String} resetToken - Token received via email
   * @param {Object} credentials - Object containing new password
   * @returns {Promise}
   */
  resetPassword: (resetToken, credentials) => {
    return apiRequest(
      `${API_ENDPOINTS.auth.resetPassword.url}?resetPasswordToken=${resetToken}`,
      {
        method: 'PATCH',
        data: credentials,
      }
    );
  },
};

/**
 * User Service
 * Handles user-related API calls
 */
import apiService, { apiRequest } from './apiService';
import { API_ENDPOINTS } from '../api/apiConfig';

export const userService = {
  /**
   * Get the current user's profile
   * @returns {Promise} - Current user data
   */
  getMe: () => {
    return apiService.getAll(API_ENDPOINTS.user.getMe);
  },

  /**
   * Get all users
   * @param {Object} params - Query parameters for pagination, sorting, filtering
   * @returns {Promise} - List of users
   */
  getAll: params => {
    return apiService.getAll(API_ENDPOINTS.user.getAll, params);
  },

  /**
   * Update a user
   * @param {Object} data - User data to update
   * @returns {Promise} - Updated user data
   */
  update: data => {
    return apiRequest(API_ENDPOINTS.user.update, {
      method: 'PUT',
      data,
    });
  },

  /**
   * Assign a manager to users
   * @param {Object} data - Manager assignment data
   * @returns {Promise} - Assignment result
   */
  assignManager: data => {
    return apiRequest(API_ENDPOINTS.user.assignManager, {
      method: 'PATCH',
      data,
    });
  },

  /**
   * Upload a profile picture
   * @param {Number|String} userId - User ID
   * @param {File} file - Image file to upload
   * @returns {Promise} - Upload result
   */
  uploadProfilePicture: (userId, file) => {
    const formData = new FormData();
    formData.append('file', file);

    return apiRequest(API_ENDPOINTS.user.profilePicture(userId), {
      method: 'POST',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
};

export default userService;

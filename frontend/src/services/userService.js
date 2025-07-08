/**
 * User Service
 * Handles user-related API calls
 */
import { apiService, apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

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
   * Get users without assigned managers
   * @returns {Promise} - List of unmanaged users
   */
  getUnmanagedUsers: () => {
    return apiService.getAll(API_ENDPOINTS.user.getAll, { noManager: true });
  },

  /**
   * Get all users with manager role
   * @returns {Promise} - List of managers
   */
  getManagers: () => {
    return apiService.getAll(API_ENDPOINTS.user.getAllManagers);
  },

  /**
   * Assign a manager to multiple users
   * @param {Object} assignment - Assignment data with managerId and userIds array
   * @returns {Promise} - Assignment result
   */
  assignManager: assignment => {
    return apiRequest(API_ENDPOINTS.user.assignManager, {
      method: 'PATCH',
      data: assignment,
    });
  },

  /**
   * Get mini user profiles
   * @returns {Promise} - List of mini user profiles
   */
  getMiniUsers: () => {
    return apiRequest(API_ENDPOINTS.user.getMiniUsers, {
      method: 'GET',
    });
  },

  /**
   * Upload a profile picture
   * @param {Number|String} userId - User ID
   * @param {File} file - Image file to upload
   * @returns {Promise} - Upload result
   */
  uploadProfilePicture: async (userId, file) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
      const result = await apiRequest(
        API_ENDPOINTS.user.profilePicture(userId),
        {
          method: 'POST',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }
      );

      // Trigger profile picture update event for cache refresh
      if (result.success) {
        window.dispatchEvent(
          new CustomEvent('profilePictureUpdated', {
            detail: { userId: parseInt(userId) },
          })
        );
      }

      return result;
    } catch (error) {
      console.error('Error uploading profile picture:', error);
      throw error;
    }
  },

  /**
   * Reset profile picture to default
   * @param {Number|String} userId - User ID
   * @returns {Promise} - Reset result
   */
  resetProfilePicture: async userId => {
    const result = await apiRequest(API_ENDPOINTS.user.profilePicture(userId), {
      method: 'DELETE',
    });

    // Trigger profile picture update event for cache refresh
    if (result.success) {
      window.dispatchEvent(
        new CustomEvent('profilePictureUpdated', {
          detail: { userId: parseInt(userId) },
        })
      );
    }

    return result;
  },
};

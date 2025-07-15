import { fileUploadInstance } from 'api/fileUploadInstance';
import { apiService, apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

/**
 * @fileoverview User Service - Handles all user-related API operations
 * @module services/userService
 * @requires api/fileUploadInstance
 * @requires ./apiService
 * @requires api/apiConfig
 */

/**
 * @typedef {Object} User
 * @property {number} id - User unique identifier
 * @property {string} email - User email address
 * @property {string} firstName - User first name
 * @property {string} lastName - User last name
 * @property {string} role - User role (ADMIN, MANAGER, EMPLOYEE)
 * @property {boolean} active - Whether user is active
 * @property {string} workplace - User workplace location
 * @property {number} managerId - ID of assigned manager (if any)
 * @property {string} createdAt - Account creation timestamp
 * @property {string} updatedAt - Last update timestamp
 */

/**
 * @typedef {Object} MiniUser
 * @property {number} id - User unique identifier
 * @property {string} firstName - User first name
 * @property {string} lastName - User last name
 * @property {string} email - User email address
 * @property {string} role - User role
 */

/**
 * @typedef {Object} UserFilters
 * @property {string} [search] - Search term for name or email
 * @property {string} [role] - Filter by user role
 * @property {string} [workplace] - Filter by workplace
 * @property {boolean} [active] - Filter by active status
 * @property {boolean} [noManager] - Filter users without managers
 * @property {boolean} [includeAdmins] - Include admin users in results
 * @property {number} [page] - Page number for pagination
 * @property {number} [size] - Page size for pagination
 */

/**
 * @typedef {Object} ManagerAssignment
 * @property {number} managerId - ID of the manager to assign
 * @property {number[]} userIds - Array of user IDs to assign to the manager
 */

/**
 * User Service - Comprehensive service for user management operations
 *
 * This service provides a complete set of methods for managing users,
 * including profile management, role assignments, and profile picture handling.
 * All methods return Promises that resolve to the API response data.
 *
 * @namespace userService
 */
export const userService = {
  /**
   * Retrieves the current authenticated user's profile information
   *
   * @method getMe
   * @memberof userService
   * @returns {Promise<User>} Promise that resolves to the current user's profile data
   *
   * @example
   * // Get current user profile
   * const currentUser = await userService.getMe();
   * console.log(currentUser.firstName, currentUser.lastName);
   *
   * @throws {Error} When user is not authenticated or server error occurs
   * @since 1.0.0
   */
  getMe: () => {
    return apiService.getAll(API_ENDPOINTS.user.getMe);
  },

  /**
   * Retrieves all users with optional filtering and pagination
   *
   * @method getAll
   * @memberof userService
   * @param {UserFilters} [params={}] - Query parameters for filtering and pagination
   * @returns {Promise<User[]>} Promise that resolves to an array of user objects
   *
   * @example
   * // Get all users
   * const users = await userService.getAll();
   *
   * @example
   * // Get users with filters
   * const managers = await userService.getAll({
   *   role: 'MANAGER',
   *   active: true,
   *   page: 0,
   *   size: 10
   * });
   *
   * @example
   * // Search users by name or email
   * const searchResults = await userService.getAll({
   *   search: 'john',
   *   workplace: 'HEADQUARTERS'
   * });
   *
   * @throws {Error} When server error occurs or user lacks permissions
   * @since 1.0.0
   */
  getAll: params => {
    return apiService.getAll(API_ENDPOINTS.user.getAll, params);
  },

  /**
   * Updates user information
   *
   * @method update
   * @memberof userService
   * @param {Partial<User>} data - User data to update (must include id)
   * @returns {Promise<User>} Promise that resolves to the updated user data
   *
   * @example
   * // Update user profile
   * const updatedUser = await userService.update({
   *   id: 123,
   *   firstName: 'John',
   *   lastName: 'Doe',
   *   workplace: 'BRANCH_OFFICE'
   * });
   *
   * @throws {Error} When user ID is invalid, user lacks permissions, or validation fails
   * @since 1.0.0
   */
  update: data => {
    return apiRequest(API_ENDPOINTS.user.update, {
      method: 'PUT',
      data,
    });
  },

  /**
   * Retrieves users who don't have assigned managers
   *
   * @method getUnmanagedUsers
   * @memberof userService
   * @returns {Promise<User[]>} Promise that resolves to array of users without managers
   *
   * @example
   * // Get all users without managers for assignment
   * const unmanagedUsers = await userService.getUnmanagedUsers();
   * console.log(`Found ${unmanagedUsers.length} users without managers`);
   *
   * @throws {Error} When server error occurs or user lacks admin permissions
   * @since 1.0.0
   */
  getUnmanagedUsers: () => {
    return apiService.getAll(API_ENDPOINTS.user.getAll, { noManager: true });
  },

  /**
   * Retrieves all users with manager role
   *
   * @method getManagers
   * @memberof userService
   * @returns {Promise<User[]>} Promise that resolves to array of manager users
   *
   * @example
   * // Get all managers for assignment dropdown
   * const managers = await userService.getManagers();
   * const managerOptions = managers.map(m => ({
   *   value: m.id,
   *   label: `${m.firstName} ${m.lastName}`
   * }));
   *
   * @throws {Error} When server error occurs or user lacks permissions
   * @since 1.0.0
   */
  getManagers: () => {
    return apiService.getAll(API_ENDPOINTS.user.getAllManagers);
  },

  /**
   * Assigns a manager to multiple users
   *
   * @method assignManager
   * @memberof userService
   * @param {ManagerAssignment} assignment - Assignment data with manager and user IDs
   * @returns {Promise<Object>} Promise that resolves to assignment result
   *
   * @example
   * // Assign manager to multiple users
   * const result = await userService.assignManager({
   *   managerId: 5,
   *   userIds: [10, 11, 12]
   * });
   *
   * @throws {Error} When manager ID is invalid, user IDs are invalid, or user lacks admin permissions
   * @since 1.0.0
   */
  assignManager: assignment => {
    return apiRequest(API_ENDPOINTS.user.assignManager, {
      method: 'PATCH',
      data: assignment,
    });
  },

  /**
   * Retrieves minimal user profiles for dropdowns and selections
   *
   * @method getMiniUsers
   * @memberof userService
   * @param {UserFilters} [params={}] - Query parameters for filtering
   * @returns {Promise<MiniUser[]>} Promise that resolves to array of mini user profiles
   *
   * @example
   * // Get mini users for dropdown
   * const miniUsers = await userService.getMiniUsers();
   *
   * @example
   * // Get mini users including admins
   * const allMiniUsers = await userService.getMiniUsers({
   *   includeAdmins: true
   * });
   *
   * @throws {Error} When server error occurs
   * @since 1.0.0
   */
  getMiniUsers: (params = {}) => {
    return apiService.getAll(API_ENDPOINTS.user.getMiniUsers, params);
  },

  /**
   * Uploads a profile picture for a user
   *
   * @method uploadProfilePicture
   * @memberof userService
   * @param {number|string} userId - User ID for whom to upload the picture
   * @param {File} file - Image file to upload (must be valid image format)
   * @returns {Promise<Object>} Promise that resolves to upload result
   *
   * @example
   * // Upload profile picture from file input
   * const fileInput = document.getElementById('profilePicture');
   * const file = fileInput.files[0];
   *
   * try {
   *   const result = await userService.uploadProfilePicture(123, file);
   *   console.log('Profile picture uploaded successfully');
   * } catch (error) {
   *   console.error('Upload failed:', error.message);
   * }
   *
   * @throws {Error} When file is invalid, user ID is invalid, or upload fails
   * @since 1.0.0
   */
  uploadProfilePicture: async (userId, file) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
      const result = await fileUploadInstance.post(
        API_ENDPOINTS.user.profilePicture(userId).url,
        formData
      );

      // Trigger profile picture update event for cache refresh
      // Add a small delay to ensure database transaction is committed
      setTimeout(() => {
        window.dispatchEvent(
          new CustomEvent('profilePictureUpdated', {
            detail: { userId: parseInt(userId) },
          })
        );
      }, 500); // 500ms delay

      return result;
    } catch (error) {
      console.error('Error uploading profile picture:', error);
      throw error;
    }
  },

  /**
   * Resets user's profile picture to default
   *
   * @method resetProfilePicture
   * @memberof userService
   * @param {number|string} userId - User ID for whom to reset the picture
   * @returns {Promise<Object>} Promise that resolves to reset result
   *
   * @example
   * // Reset profile picture to default
   * try {
   *   await userService.resetProfilePicture(123);
   *   console.log('Profile picture reset to default');
   * } catch (error) {
   *   console.error('Reset failed:', error.message);
   * }
   *
   * @throws {Error} When user ID is invalid or reset fails
   * @since 1.0.0
   */
  resetProfilePicture: async userId => {
    const result = await apiRequest(API_ENDPOINTS.user.profilePicture(userId), {
      method: 'DELETE',
    });

    // Trigger profile picture update event for cache refresh
    // Add a small delay to ensure database transaction is committed
    setTimeout(() => {
      window.dispatchEvent(
        new CustomEvent('profilePictureUpdated', {
          detail: { userId: parseInt(userId) },
        })
      );
    }, 500); // 500ms delay

    return result;
  },
};

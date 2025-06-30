/**
 * Training Service
 * Handles training-related API calls
 */
import { apiService, apiRequest } from './apiService';
import { API_ENDPOINTS } from '../api/apiConfig';

export const trainingService = {
  /**
   * Get all trainings with optional filters
   * @param {Object} params - Query parameters for pagination, sorting, filtering
   * @returns {Promise} - List of trainings
   */
  getAll: params => {
    return apiService.getAll(API_ENDPOINTS.trainings.getAll, params);
  },

  /**
   * Create a new training (admin only)
   * @param {Object} data - Training data
   * @returns {Promise} - Created training
   */
  create: data => {
    return apiService.create(API_ENDPOINTS.trainings.create, data);
  },

  /**
   * Update an existing training (admin only)
   * @param {Object} data - Training data to update
   * @returns {Promise} - Updated training data
   */
  update: data => {
    return apiRequest(API_ENDPOINTS.trainings.update, {
      method: 'PUT',
      data,
    });
  },

  /**
   * Create/update a training area
   * @param {Object} data - Training area data
   * @returns {Promise} - Training area result
   */
  manageTrainingArea: data => {
    return apiService.create(API_ENDPOINTS.trainings.area, data);
  },

  /**
   * Get trainings a user has not participated in (for managers/admin)
   * @param {Number|String} userId - User ID
   * @returns {Promise} - List of trainings
   */
  getUnparticipatedTrainings: userId => {
    return apiRequest(API_ENDPOINTS.trainings.unparticipated(userId), {
      method: 'GET',
    });
  },

  /**
   * Get all training areas
   * @returns {Promise} - List of training areas
   */
  getAllAreas: () => {
    return apiService.getAll(API_ENDPOINTS.trainings.areas);
  },

  /**
   * Deactivate a training (admin only)
   * @param {Number|String} trainingId - Training ID
   * @returns {Promise} - Operation result
   */
  deactivate: trainingId => {
    return apiRequest(API_ENDPOINTS.trainings.deactivate(trainingId), {
      method: 'PATCH',
    });
  },
};

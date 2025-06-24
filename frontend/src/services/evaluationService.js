/**
 * Evaluation Service
 * Handles evaluation-related API calls
 */
import apiService, { apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const evaluationService = {
  /**
   * Get all evaluations with optional filters
   * @param {Object} params - Query parameters for pagination, sorting, filtering
   * @returns {Promise} - List of evaluations
   */
  getAll: params => {
    return apiService.getAll(API_ENDPOINTS.evaluations.getAll, params);
  },

  /**
   * Update an evaluation
   * @param {Object} data - Evaluation data to update
   * @returns {Promise} - Updated evaluation data
   */
  update: data => {
    return apiRequest(API_ENDPOINTS.evaluations.update, {
      method: 'PUT',
      data,
    });
  },

  /**
   * Close an evaluation (admin only)
   * @param {Object} data - Evaluation closing data
   * @returns {Promise} - Operation result
   */
  closeEvaluation: data => {
    return apiRequest(API_ENDPOINTS.evaluations.close, {
      method: 'PATCH',
      data,
    });
  },
};

export default evaluationService;

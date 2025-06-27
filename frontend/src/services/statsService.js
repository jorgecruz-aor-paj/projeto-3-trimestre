/**
 * Stats Service
 * Handles statistics-related API calls
 */
import { apiService } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const statsService = {
  /**
   * Get manager assignment statistics
   * @returns {Promise} - Manager assignment data
   */
  getManagerStats: () => {
    return apiService.getAll(API_ENDPOINTS.stats.managers);
  },

  /**
   * Get evaluation completion statistics
   * @returns {Promise} - Evaluation statistics data
   */
  getEvaluationStats: () => {
    return apiService.getAll(API_ENDPOINTS.stats.evaluations);
  },

  /**
   * Get team statistics for managers
   * @param {number} managerId - Manager ID
   * @returns {Promise} - Team statistics data
   */
  getTeamStats: managerId => {
    return apiService.getAll(API_ENDPOINTS.stats.team(managerId));
  },

  /**
   * Get personal statistics for users
   * @param {number} userId - User ID
   * @returns {Promise} - Personal statistics data
   */
  getPersonalStats: userId => {
    return apiService.getAll(API_ENDPOINTS.stats.personal(userId));
  },
};

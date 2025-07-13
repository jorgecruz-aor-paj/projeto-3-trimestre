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
   * Get training participation stats by year for a user
   * @param {number|string} userId
   * @returns {Promise}
   */
  getParticipationStats: userId => {
    return apiService.getAll(API_ENDPOINTS.stats.participations(userId));
  },

  /**
   * Get personal statistics for a user
   * @param {number|string} userId
   * @returns {Promise} - Personal statistics data
   */
  getPersonalStats: userId => {
    return apiService.getAll(API_ENDPOINTS.stats.personal(userId));
  },
};

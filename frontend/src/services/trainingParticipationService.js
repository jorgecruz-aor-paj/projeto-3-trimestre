import { apiService } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const trainingParticipationService = {
  /**
   * Register training participation
   * @param {Object} data - Participation data
   * @returns {Promise} - Registration result
   */
  register: data => {
    return apiService.create(
      API_ENDPOINTS.trainingParticipation.register,
      data
    );
  },

  /**
   * Get user's training participations
   * @param {Number|String} userId - User ID
   * @returns {Promise} - List of participations
   */
  getByUser: userId => {
    return apiService.getAll(
      API_ENDPOINTS.trainingParticipation.byUser(userId)
    );
  },
};

export default trainingParticipationService;

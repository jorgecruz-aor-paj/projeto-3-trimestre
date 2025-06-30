import { apiService } from './apiService';
import { API_ENDPOINTS } from '../api/apiConfig';

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
  getByParticipantId: participantId => {
    // API_ENDPOINTS.trainingParticipation.getByUser is a function returning an object with url
    const endpoint =
      API_ENDPOINTS.trainingParticipation.getByUser(participantId);
    // Use apiService.getAll to match the rest of the codebase and support interceptors, etc.
    return apiService.getAll(endpoint);
  },
};

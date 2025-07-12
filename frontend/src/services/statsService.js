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
    // For now, return mock data until backend implementation
    // eslint-disable-next-line no-unused-vars
    const _userId = userId;
    return Promise.resolve({
      completedTrainings: Math.floor(Math.random() * 10) + 5,
      pendingEvaluations: Math.floor(Math.random() * 3) + 1,
      performanceScore: Math.floor(Math.random() * 40) + 60,
      goalsAchieved: Math.floor(Math.random() * 8) + 2,
      totalGoals: Math.floor(Math.random() * 5) + 10,
      trainingHistory: [
        { year: 2024, month: 7, minutes: 120 },
        { year: 2024, month: 8, minutes: 180 },
        { year: 2024, month: 9, minutes: 90 },
        { year: 2024, month: 10, minutes: 240 },
        { year: 2024, month: 11, minutes: 150 },
        { year: 2024, month: 12, minutes: 200 },
      ],
      performanceHistory: [
        { period: 'Q1 2024', score: 3.2 },
        { period: 'Q2 2024', score: 3.4 },
        { period: 'Q3 2024', score: 3.6 },
        { period: 'Q4 2024', score: 3.8 },
      ],
    });
    // TODO: Replace with actual API call when backend is ready
    // return apiService.getAll(API_ENDPOINTS.stats.personal(userId));
  },
};

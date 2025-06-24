/**
 * AI Service
 * Handles AI-related API calls
 */
import apiService from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const aiService = {
  /**
   * Analyze sentiment of text
   * @param {Object} feedback - Feedback text to analyze
   * @returns {Promise} - Sentiment analysis results
   */
  analyzeSentiment: feedback => {
    return apiService.create(API_ENDPOINTS.ai.analyzeSentiment, feedback);
  },

  /**
   * Get training recommendations based on evaluations
   * @param {Object} data - Training request data with evaluations, skills, and history
   * @returns {Promise} - Training recommendations
   */
  recommendTraining: data => {
    return apiService.create(API_ENDPOINTS.ai.recommendTraining, data);
  },

  /**
   * Predict future performance based on historical scores
   * @param {Object} data - Prediction request with historical scores
   * @returns {Promise} - Performance prediction
   */
  predictPerformance: data => {
    return apiService.create(API_ENDPOINTS.ai.predictPerformance, data);
  },
};

export default aiService;

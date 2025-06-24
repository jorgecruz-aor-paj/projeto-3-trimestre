/**
 * Services Module Index
 * Central export point for all application services
 */
import {
  API_BASE_URL,
  WEBSOCKET_BASE_URL,
  API_ENDPOINTS,
} from '../api/apiConfig';
import apiService from './apiService';
import authService from './authService';
import userService from './userService';
import trainingService from './trainingService';
import trainingParticipationService from './trainingParticipationService';
import evaluationService from './evaluationService';
import aiService from './aiService';
import exportService from './exportService';
import statsService from './statsService';
import websocketService from './websocketService';

// Export all services and configurations
export {
  API_BASE_URL,
  WEBSOCKET_BASE_URL,
  API_ENDPOINTS,
  apiService,
  authService,
  userService,
  trainingService,
  trainingParticipationService,
  evaluationService,
  aiService,
  exportService,
  statsService,
  websocketService,
};

// Default export for convenient importing
export default {
  api: apiService,
  auth: authService,
  users: userService,
  trainings: trainingService,
  trainingParticipation: trainingParticipationService,
  evaluations: evaluationService,
  ai: aiService,
  export: exportService,
  stats: statsService,
  websocket: websocketService,
};

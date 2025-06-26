/**
 * Services Module Index
 * Central export point for all application services
 */
import { API_BASE_URL, WEBSOCKET_BASE_URL, API_ENDPOINTS } from 'api/apiConfig';
import { apiService, apiRequest } from './apiService';
import { authService } from './authService';
import { userService } from './userService';
import { trainingService } from './trainingService';
import { trainingParticipationService } from './trainingParticipationService';
import { evaluationService } from './evaluationService';
import { aiService } from './aiService';
import { exportService, downloadBlob } from './exportService';
import { statsService } from './statsService';
import { websocketService } from './websocketService';

// Export all services and configurations
export {
  API_BASE_URL,
  WEBSOCKET_BASE_URL,
  API_ENDPOINTS,
  apiService,
  apiRequest,
  authService,
  userService,
  trainingService,
  trainingParticipationService,
  evaluationService,
  aiService,
  exportService,
  downloadBlob,
  statsService,
  websocketService,
};

// Default export for convenient importing
const services = {
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

export default services;

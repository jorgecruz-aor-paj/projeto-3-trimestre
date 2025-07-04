/**
 * Evaluation cycle Service
 * Handles evaluation cycles-related API calls
 */
import { apiService, apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const cycleService = {

getCurrent: () => {
    return apiService.getAll(API_ENDPOINTS.cycles.getCurrent);
},
start: (data) => {
    return apiRequest(API_ENDPOINTS.cycles.startNew, {
      method: 'POST',
      data,
    });
  },
getAll: (params) => {
    return apiService.getAll(API_ENDPOINTS.cycles.getAll, params);
  },
  // Add more methods as needed
}
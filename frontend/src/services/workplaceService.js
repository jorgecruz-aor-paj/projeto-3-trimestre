// src/services/workplaceService.js
import { apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const workplaceService = {
  getWorkplaces: () => {
    return apiRequest(API_ENDPOINTS.workplaces.getAll, {
      method: 'GET',
    });
  },
};

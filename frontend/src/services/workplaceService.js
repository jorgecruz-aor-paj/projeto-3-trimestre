// src/services/workplaceService.js
import { apiService } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const workplaceService = {
  async getWorkplaces() {
    const response = await apiService.getAll(API_ENDPOINTS.workplaces.getAll);
    return response;
  },
};

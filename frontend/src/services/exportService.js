/**
 * Export Service
 * Handles export-related API calls
 */
import apiService from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const exportService = {
  /**
   * Export users to CSV
   * @returns {Promise} - CSV data as blob
   */
  usersToCSV: () => {
    return apiService.getAll(
      API_ENDPOINTS.export.usersCSV,
      {},
      { responseType: 'blob' }
    );
  },
};

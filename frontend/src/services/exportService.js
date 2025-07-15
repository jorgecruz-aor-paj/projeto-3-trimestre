/**
 * Export Service
 * Handles export-related API calls
 */
import { apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const exportService = {
  // Users exports
  /**
   * Export all users to CSV
   * @returns {Promise} - CSV data as blob
   */
  usersToCSV: () => {
    return apiRequest(API_ENDPOINTS.export.usersCSV, {
      method: 'GET',
      responseType: 'blob',
    });
  },

  /**
   * Export filtered users to CSV
   * @param {Object} filters - Filter parameters for user export
   * @returns {Promise} - CSV data as blob
   */
  usersToCSVFiltered: (filters = {}) => {
    return apiRequest(API_ENDPOINTS.export.usersCSVFiltered, {
      method: 'GET',
      params: filters,
      responseType: 'blob',
    });
  },

  /**
   * Export all users to XLSX
   * @returns {Promise} - XLSX data as blob
   */
  usersToXLSX: () => {
    return apiRequest(API_ENDPOINTS.export.usersXLSX, {
      method: 'GET',
      responseType: 'blob',
    });
  },

  /**
   * Export filtered users to XLSX
   * @param {Object} filters - Filter parameters for user export
   * @returns {Promise} - XLSX data as blob
   */
  usersToXLSXFiltered: (filters = {}) => {
    return apiRequest(API_ENDPOINTS.export.usersXLSXFiltered, {
      method: 'GET',
      params: filters,
      responseType: 'blob',
    });
  },

  // Trainings exports
  /**
   * Export all trainings to CSV
   * @returns {Promise} - CSV data as blob
   */
  trainingsToCSV: () => {
    return apiRequest(API_ENDPOINTS.export.trainingsCSV, {
      method: 'GET',
      responseType: 'blob',
    });
  },

  /**
   * Export filtered trainings to CSV
   * @param {Object} filters - Filter parameters for training export
   * @returns {Promise} - CSV data as blob
   */
  trainingsToCSVFiltered: (filters = {}) => {
    return apiRequest(API_ENDPOINTS.export.trainingsCSVFiltered, {
      method: 'GET',
      params: filters,
      responseType: 'blob',
    });
  },

  /**
   * Export all trainings to XLSX
   * @returns {Promise} - XLSX data as blob
   */
  trainingsToXLSX: () => {
    return apiRequest(API_ENDPOINTS.export.trainingsXLSX, {
      method: 'GET',
      responseType: 'blob',
    });
  },

  /**
   * Export filtered trainings to XLSX
   * @param {Object} filters - Filter parameters for training export
   * @returns {Promise} - XLSX data as blob
   */
  trainingsToXLSXFiltered: (filters = {}) => {
    return apiRequest(API_ENDPOINTS.export.trainingsXLSXFiltered, {
      method: 'GET',
      params: filters,
      responseType: 'blob',
    });
  },

  // Evaluations exports
  /**
   * Export all evaluations to CSV
   * @returns {Promise} - CSV data as blob
   */
  evaluationsToCSV: () => {
    return apiRequest(API_ENDPOINTS.export.evaluationsCSV, {
      method: 'GET',
      responseType: 'blob',
    });
  },

  /**
   * Export filtered evaluations to CSV
   * @param {Object} filters - Filter parameters for evaluation export
   * @returns {Promise} - CSV data as blob
   */
  evaluationsToCSVFiltered: (filters = {}) => {
    return apiRequest(API_ENDPOINTS.export.evaluationsCSVFiltered, {
      method: 'GET',
      params: filters,
      responseType: 'blob',
    });
  },

  /**
   * Export all evaluations to XLSX
   * @returns {Promise} - XLSX data as blob
   */
  evaluationsToXLSX: () => {
    return apiRequest(API_ENDPOINTS.export.evaluationsXLSX, {
      method: 'GET',
      responseType: 'blob',
    });
  },

  /**
   * Export filtered evaluations to XLSX
   * @param {Object} filters - Filter parameters for evaluation export
   * @returns {Promise} - XLSX data as blob
   */
  evaluationsToXLSXFiltered: (filters = {}) => {
    return apiRequest(API_ENDPOINTS.export.evaluationsXLSXFiltered, {
      method: 'GET',
      params: filters,
      responseType: 'blob',
    });
  },

  // PDF exports
  /**
   * Export single evaluation to PDF
   * @param {String|Number} evaluationId - The evaluation ID
   * @returns {Promise} - PDF data as blob
   */
  evaluationToPDF: evaluationId => {
    return apiRequest(API_ENDPOINTS.export.evaluationPDF(evaluationId), {
      method: 'GET',
      responseType: 'blob',
    });
  },
};

/**
 * Utility function to download a blob as a file
 * @param {Blob} blob - The blob data to download
 * @param {String} filename - The filename for the download
 * @param {String} contentType - The MIME type of the file
 */
export const downloadBlob = (
  blob,
  filename,
  contentType = 'application/octet-stream'
) => {
  // Create a blob with the specified content type if not already a blob
  const file =
    blob instanceof Blob ? blob : new Blob([blob], { type: contentType });

  // Create a temporary URL for the blob
  const url = window.URL.createObjectURL(file);

  // Create a temporary anchor element and trigger download
  const anchor = document.createElement('a');
  anchor.href = url;
  anchor.download = filename;
  document.body.appendChild(anchor);
  anchor.click();

  // Clean up
  document.body.removeChild(anchor);
  window.URL.revokeObjectURL(url);
};

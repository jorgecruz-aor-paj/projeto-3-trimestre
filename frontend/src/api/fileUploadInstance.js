import axios from 'axios';
import { API_BASE_URL } from './apiConfig';
import { useAuthStore } from 'stores/useAuthStore';

const fileUploadInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    // Don't set Content-Type for multipart/form-data - let browser set it with boundary
  },
  timeout: 30000, // Longer timeout for file uploads
});

// Request interceptor: attach token
fileUploadInstance.interceptors.request.use(
  config => {
    const token =
      useAuthStore.getState()?.token || sessionStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

let isHandling401 = false;

// Response interceptor: similar to main instance but without data extraction for progress tracking
fileUploadInstance.interceptors.response.use(
  response => response, // Keep full response for upload progress
  error => {
    const { response } = error;

    if (!response) {
      return Promise.reject({
        code: 'NETWORK_ERROR',
        message: 'Network connection lost during upload.',
        original: error,
      });
    }

    // Handle 401 - but only clear auth state, let components handle UI
    if (response?.status === 401 && !isHandling401) {
      isHandling401 = true;

      // Clear auth state silently
      useAuthStore.getState().clearState?.();

      // Reset flag after delay
      setTimeout(() => (isHandling401 = false), 1000);

      // Return structured error for components to handle
      return Promise.reject({
        code: 'UNAUTHORIZED',
        message: 'Session expired. Please log in again.',
        type: 'auth',
        shouldRedirect: true,
        redirectTo: '/login',
        original: error,
      });
    }

    // Format all other errors consistently
    const structuredError = {
      code: response?.status || 'UPLOAD_ERROR',
      message:
        response?.data?.message ||
        response?.statusText ||
        'Upload failed. Please try again.',
      type: response?.status >= 500 ? 'server' : 'client',
      original: error,
    };

    return Promise.reject(structuredError);
  }
);

export { fileUploadInstance };

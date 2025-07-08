import axios from 'axios';
import { API_BASE_URL } from './apiConfig';
import { useAuthStore } from 'stores/AuthStore';

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor: attach token
axiosInstance.interceptors.request.use(
  config => {
    const token =
      useAuthStore.getState()?.token || sessionStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // Log file uploads for debugging
    if (
      config.data instanceof FormData &&
      config.url.includes('/profile/picture')
    ) {
      console.log('Sending profile picture upload request:', {
        url: config.url,
        method: config.method,
        headers: config.headers,
        hasFile: config.data.has('file'),
      });
    }

    return config;
  },
  error => Promise.reject(error)
);

let isHandling401 = false;

axiosInstance.interceptors.response.use(
  response => {
    // Log profile picture responses
    if (response.config.url.includes('/profile/picture')) {
      console.log('Profile picture request completed:', {
        url: response.config.url,
        status: response.status,
        statusText: response.statusText,
        headers: response.headers,
      });
    }
    return response.data;
  },
  error => {
    const { response, config } = error;

    // Log profile picture errors
    if (config && config.url && config.url.includes('/profile/picture')) {
      console.error('Profile picture request failed:', {
        url: config.url,
        method: config.method,
        response: response,
      });
    }

    // Handle network errors
    if (!response) {
      return Promise.reject({
        code: 'NETWORK_ERROR',
        message: 'Network connection lost or server unreachable.',
        type: 'network',
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
      code: response?.status || 'UNKNOWN_ERROR',
      message:
        response?.data?.message ||
        response?.statusText ||
        'Something went wrong. Please try again.',
      type: response?.status >= 500 ? 'server' : 'client',
      original: error,
    };

    return Promise.reject(structuredError);
  }
);

export { axiosInstance };

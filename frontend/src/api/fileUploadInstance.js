import axios from 'axios';
import { API_BASE_URL } from './apiConfig';
import { useAuthStore } from 'stores/AuthStore';

// Global flag to prevent multiple simultaneous logout processes
let isHandling401 = false;

const fileUploadInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    // Don't set Content-Type for multipart/form-data - let browser set it with boundary
  },
  timeout: 30000, // Longer timeout for file uploads
});

// Function to handle automatic logout (enhanced version)
const handleAutomaticLogout = () => {
  if (isHandling401) return;
  isHandling401 = true;

  try {
    // Clear auth store
    const authStore = useAuthStore.getState();
    authStore.clearState?.();

    // Clear session storage
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('useAuthStore');
    sessionStorage.removeItem('workplaces'); // Also remove workplaces from session storage

    // Clear localStorage (if any auth data is stored there)
    const authKeys = ['token', 'user', 'authStore', 'workplaces'];
    authKeys.forEach(key => {
      localStorage.removeItem(key);
      sessionStorage.removeItem(key);
    });

    // Disconnect WebSocket if connected
    try {
      const { websocketService } = require('services/websocketService');
      websocketService.disconnect();
    } catch (e) {
      console.warn('WebSocket service not available during logout:', e);
    }

    console.log(
      'Automatic logout completed - session expired or invalid token'
    );

    // Show alert to user before redirect
    try {
      // We're accessing the global alertContext which should be available
      // from the AlertProvider at the app root
      const showAlert =
        window.showGlobalAlert ||
        (() => {
          // Fallback if global alert function is not available: use a browser alert
          alert('Your session has expired. Please log in again to continue.');
        });

      // We'll store redirect info to be used after the alert is dismissed
      const currentPath = window.location.pathname;
      const shouldRedirect =
        currentPath !== '/login' &&
        currentPath !== '/register' &&
        currentPath !== '/';

      // Use the alert context to show a message to the user
      if (shouldRedirect) {
        // Set a flag in sessionStorage to show the message after redirect
        // The LoginAuthForm component will read this flag and show a properly
        // translated message using the i18n context available in the component
        sessionStorage.setItem('sessionExpired', 'true');

        // Redirect to login page
        window.location.href = '/login';
      }
    } catch (alertError) {
      console.error('Error showing alert during logout:', alertError);

      // Fallback redirect if alert fails
      const currentPath = window.location.pathname;
      if (
        currentPath !== '/login' &&
        currentPath !== '/register' &&
        currentPath !== '/'
      ) {
        window.location.href = '/login';
      }
    }
  } catch (error) {
    console.error('Error during automatic logout:', error);

    // Fallback redirect if anything fails
    window.location.href = '/login';
  } finally {
    // Reset flag after a delay
    setTimeout(() => {
      isHandling401 = false;
    }, 2000);
  }
};

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

    // Handle 401 - unauthorized/expired token
    if (response?.status === 401) {
      console.log(
        '401 Unauthorized response received during file upload - performing automatic logout'
      );
      handleAutomaticLogout();

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

    // Handle 403 - forbidden
    if (response?.status === 403) {
      return Promise.reject({
        code: 'FORBIDDEN',
        message: 'Access denied. You do not have permission to upload files.',
        type: 'auth',
        shouldRedirect: true,
        redirectTo: '/error/403',
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

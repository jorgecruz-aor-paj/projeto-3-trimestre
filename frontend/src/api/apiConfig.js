/**
 * API Configuration
 * Centralized configuration for all API endpoints
 */

// Function to get the base URL for the API
const getApiBaseUrl = () => {
  const url = `https://${window.location.hostname}:8443/jorgeosorio-proj3t/api`;
  return url;
};

// Function to get the base URL for the WebSocket API
const getWebSocketBaseUrl = () => {
  const url = `wss://${window.location.hostname}:8443/jorgeosorio-proj3t/websocket`;
  return url;
};

// Base URL
const API_BASE_URL = getApiBaseUrl();
const WEBSOCKET_BASE_URL = getWebSocketBaseUrl();

// API specific URL's
const AUTH_ENDPOINT = `${API_BASE_URL}/auth`;
const USER_ENDPOINT = `${API_BASE_URL}/users`;
const TRAININGS_ENDPOINT = `${API_BASE_URL}/trainings`;
const EVALUATIONS_ENDPOINT = `${API_BASE_URL}/evaluations`;

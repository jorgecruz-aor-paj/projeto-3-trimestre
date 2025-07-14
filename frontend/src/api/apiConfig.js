/**
 * API Configuration
 * Centralized configuration for all API endpoints
 */

// const VPS_URL = 'proj3taor.vps.webdock.cloud';

// Function to get the base URL for the API
const getApiBaseUrl = () => {
  const url = `https://${window.location.hostname}:8443/jorgeosorio-proj3t/api`;
  return url;
};

// Function to get the base URL for the WebSocket API
const getWebSocketBaseUrl = () => {
  const url = `wss://${window.location.hostname}:8443/jorgeosorio-proj3t`;
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
const CYCLES_ENDPOINT = `${EVALUATIONS_ENDPOINT}/cycle`;
const AI_ENDPOINT = `${API_BASE_URL}/ai`;
const EXPORT_ENDPOINT = `${API_BASE_URL}/export`;
const STATS_ENDPOINT = `${API_BASE_URL}/stats`;
const TEST_ENDPOINT = `${API_BASE_URL}/test`;
const WORKPLACES_ENDPOINT = `${API_BASE_URL}/workplaces`;
const MESSAGES_ENDPOINT = `${API_BASE_URL}/messages`;
const NOTIFICATIONS_ENDPOINT = `${API_BASE_URL}/notifications`;

/**
 * API Endpoints configuration
 * Each endpoint defines:
 * - url: The full endpoint URL
 * - requiresAuth: Whether the endpoint requires authentication
 * - methods: Available HTTP methods for this endpoint
 */
const API_ENDPOINTS = {
  auth: {
    login: {
      url: `${AUTH_ENDPOINT}/login`,
      requiresAuth: false,
    },
    register: {
      url: `${AUTH_ENDPOINT}/register`,
      requiresAuth: false,
    },
    logout: {
      url: `${AUTH_ENDPOINT}/logout`,
      requiresAuth: true,
    },
    confirm: {
      url: `${AUTH_ENDPOINT}/confirm`,
      requiresAuth: false,
    },
    resetPasswordRequest: {
      url: `${AUTH_ENDPOINT}/resetPassword/{email}`,
      requiresAuth: false,
    },
    resetPassword: {
      url: `${AUTH_ENDPOINT}/resetPassword`,
      requiresAuth: false,
    },
    twoFactor: {
      setup: {
        url: `${AUTH_ENDPOINT}/2fa/setup`,
        requiresAuth: true,
      },
      setupVerify: {
        url: `${AUTH_ENDPOINT}/2fa/setup/verify`,
        requiresAuth: true,
      },
      loginVerify: {
        url: `${AUTH_ENDPOINT}/complete-2fa`,
        requiresAuth: false,
      },
      disable: {
        url: `${AUTH_ENDPOINT}/2fa/disable`,
        requiresAuth: true,
      },
      status: {
        url: `${AUTH_ENDPOINT}/2fa/status`,
        requiresAuth: true,
      },
      regenerateBackupCodes: {
        url: `${AUTH_ENDPOINT}/2fa/backup-codes/regenerate`,
        requiresAuth: true,
      },
    },
  },
  user: {
    getAll: {
      url: USER_ENDPOINT,
      requiresAuth: true,
    },
    getMe: {
      url: `${USER_ENDPOINT}/me`,
      requiresAuth: true,
    },
    getById: id => ({
      url: `${USER_ENDPOINT}/${id}`,
      requiresAuth: true,
    }),
    update: {
      url: USER_ENDPOINT,
      requiresAuth: true,
    },
    assignManager: {
      url: USER_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
    profilePicture: userId => ({
      url: `${USER_ENDPOINT}/${userId}/profile/picture`,
      requiresAuth: true,
      /*
      fullUrl: `https://${window.location.hostname}:8443/jorgeosorio-proj3t/api/users/${userId}/profile/picture`,
      */
    }),
    getAllManagers: {
      url: `${USER_ENDPOINT}/managers`,
      requiresAuth: true,
    },
    getMiniUsers: {
      url: `${USER_ENDPOINT}/compact`,
      requiresAuth: true,
    },
  },
  trainings: {
    getAll: {
      url: TRAININGS_ENDPOINT,
      requiresAuth: true,
    },
    create: {
      url: TRAININGS_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
    update: {
      url: TRAININGS_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
    area: {
      url: `${TRAININGS_ENDPOINT}/area`,
      requiresAuth: true,
      adminOnly: true,
    },
    unparticipated: userId => ({
      url: `${TRAININGS_ENDPOINT}/unparticipated/${userId}`,
      requiresAuth: true,
      managerOrAdmin: true,
    }),
  },
  trainingParticipation: {
    getByUser: userId => ({
      url: `${TRAININGS_ENDPOINT}/participation/${userId}`,
      requiresAuth: true,
    }),
    create: {
      url: `${TRAININGS_ENDPOINT}/participations`,
      requiresAuth: true,
    },
    getUnparticipatedByUser: userId => ({
      url: `${TRAININGS_ENDPOINT}/unparticipated/${userId}`,
      requiresAuth: true,
      managerOrAdmin: true,
    }),
    register: {
      url: `${TRAININGS_ENDPOINT}/participation`,
      requiresAuth: true,
    },
  },
  evaluations: {
    getAll: {
      url: EVALUATIONS_ENDPOINT,
      requiresAuth: true,
    },
    update: {
      url: EVALUATIONS_ENDPOINT,
      requiresAuth: true,
      managerOrAdmin: true,
    },
    close: {
      url: EVALUATIONS_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
  },
  ai: {
    analyzeSentiment: {
      url: `${AI_ENDPOINT}/analyze-sentiment`,
      requiresAuth: true,
    },
    recommendTraining: {
      url: `${AI_ENDPOINT}/recommend-training`,
      requiresAuth: true,
    },
    predictPerformance: {
      url: `${AI_ENDPOINT}/predict-performance`,
      requiresAuth: true,
    },
    chatbot: {
      url: `${AI_ENDPOINT}/chatbot`,
      requiresAuth: true,
    },
    generateReport: {
      url: `${AI_ENDPOINT}/generate-report`,
      requiresAuth: true,
    },
  },
  export: {
    usersCSV: {
      url: `${EXPORT_ENDPOINT}/users/csv`,
      requiresAuth: true,
    },
    usersCSVFiltered: {
      url: `${EXPORT_ENDPOINT}/users/csv/filtered`,
      requiresAuth: true,
    },
    trainingsCSV: {
      url: `${EXPORT_ENDPOINT}/trainings/csv`,
      requiresAuth: true,
    },
    trainingsCSVFiltered: {
      url: `${EXPORT_ENDPOINT}/trainings/csv/filtered`,
      requiresAuth: true,
    },
    evaluationsCSV: {
      url: `${EXPORT_ENDPOINT}/evaluations/csv`,
      requiresAuth: true,
    },
    evaluationsCSVFiltered: {
      url: `${EXPORT_ENDPOINT}/evaluations/csv/filtered`,
      requiresAuth: true,
    },
    evaluationPDF: evaluationId => ({
      url: `${EXPORT_ENDPOINT}/evaluations/${evaluationId}/pdf`,
      requiresAuth: true,
    }),
  },
  stats: {
    managers: {
      url: `${STATS_ENDPOINT}/managers`,
      requiresAuth: true,
      adminOnly: true,
    },
    evaluations: {
      url: `${STATS_ENDPOINT}/evaluations`,
      requiresAuth: true,
      adminOnly: true,
    },
    participations: userId => ({
      url: `${STATS_ENDPOINT}/participation/${userId}`,
      requiresAuth: true,
    }),
    personal: userId => ({
      url: `${STATS_ENDPOINT}/personal/${userId}`,
      requiresAuth: true,
    }),
  },
  test: {
    ping: {
      url: `${TEST_ENDPOINT}/ping`,
      requiresAuth: false,
    },
    email: {
      url: `${TEST_ENDPOINT}/email`,
      requiresAuth: false,
    },
  },
  workplaces: {
    getAll: {
      url: WORKPLACES_ENDPOINT,
      requiresAuth: false,
    },
  },
  messages: {
    send: {
      url: recipientId => `${MESSAGES_ENDPOINT}/send/${recipientId}`,
      requiresAuth: true,
    },
    conversation: {
      url: otherUserId => `${MESSAGES_ENDPOINT}/conversation/${otherUserId}`,
      requiresAuth: true,
    },
    conversations: {
      url: `${MESSAGES_ENDPOINT}/conversations`,
      requiresAuth: true,
    },
    unreadCount: {
      url: `${MESSAGES_ENDPOINT}/unread/count`,
      requiresAuth: true,
    },
    unreadCountFromSender: {
      url: senderId => `${MESSAGES_ENDPOINT}/unread/count/${senderId}`,
      requiresAuth: true,
    },
    markAsRead: {
      url: messageId => `${MESSAGES_ENDPOINT}/${messageId}/read`,
      requiresAuth: true,
    },
    all: {
      url: `${MESSAGES_ENDPOINT}/all`,
      requiresAuth: true,
    },
  },
  cycles: {
    getCurrent: {
      url: `${CYCLES_ENDPOINT}/current`,
      requiresAuth: true,
      adminOnly: true,
    },
    startNew: {
      url: `${CYCLES_ENDPOINT}`,
      requiresAuth: true,
      adminOnly: true,
    },
    getAll: {
      url: `${CYCLES_ENDPOINT}/all`,
      requiresAuth: true,
      adminOnly: true,
    },
  },
  notifications: {
    getUserNotifications: {
      url: `${NOTIFICATIONS_ENDPOINT}/user`,
      requiresAuth: true,
    },
    getUserNotificationsPaginated: {
      url: `${NOTIFICATIONS_ENDPOINT}/user/paginated`,
      requiresAuth: true,
    },
    markAsRead: {
      url: notificationId => `${NOTIFICATIONS_ENDPOINT}/${notificationId}/read`,
      requiresAuth: true,
    },
    markAllAsRead: {
      url: `${NOTIFICATIONS_ENDPOINT}/read-all`,
      requiresAuth: true,
    },
    delete: {
      url: notificationId => `${NOTIFICATIONS_ENDPOINT}/${notificationId}`,
      requiresAuth: true,
    },
  },
};

export {
  API_BASE_URL,
  WEBSOCKET_BASE_URL,
  API_ENDPOINTS,
  AUTH_ENDPOINT,
  USER_ENDPOINT,
  TRAININGS_ENDPOINT,
  EVALUATIONS_ENDPOINT,
  CYCLES_ENDPOINT,
  AI_ENDPOINT,
  EXPORT_ENDPOINT,
  STATS_ENDPOINT,
  TEST_ENDPOINT,
  WORKPLACES_ENDPOINT,
  MESSAGES_ENDPOINT,
  NOTIFICATIONS_ENDPOINT,
};

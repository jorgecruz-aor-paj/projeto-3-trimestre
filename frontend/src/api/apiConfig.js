/**
 * @fileoverview API Configuration - Centralized configuration for all API endpoints
 * @module api/apiConfig
 */

/**
 * @typedef {Object} EndpointConfig
 * @property {string} url - The full endpoint URL
 * @property {boolean} requiresAuth - Whether the endpoint requires authentication
 * @property {boolean} [adminOnly] - Whether the endpoint requires admin role
 * @property {boolean} [managerOrAdmin] - Whether the endpoint requires manager or admin role
 * @property {string[]} [methods] - Available HTTP methods for this endpoint
 */

/**
 * @typedef {function} EndpointFunction
 * @param {string|number} param - Parameter to inject into URL
 * @returns {EndpointConfig} Endpoint configuration object
 */

// const VPS_URL = 'proj3taor.vps.webdock.cloud';

/**
 * Generates the base API URL based on current hostname
 *
 * @function getApiBaseUrl
 * @returns {string} The base API URL for the current environment
 * @private
 */
const getApiBaseUrl = () => {
  // const url = `https://${window.location.hostname}/api`; // For production
  const url = `https://${window.location.hostname}:8443/jorgeosorio-proj3t/api`; // For development
  return url;
};

/**
 * Generates the WebSocket base URL based on current hostname
 *
 * @function getWebSocketBaseUrl
 * @returns {string} The base WebSocket URL for the current environment
 * @private
 */
const getWebSocketBaseUrl = () => {
  // const url = `wss://${window.location.hostname}/jorgeosorio-proj3t`; // For production
  const url = `wss://${window.location.hostname}:8443/jorgeosorio-proj3t`; // For development
  return url;
};

/**
 * Base API URL for HTTP requests
 * @constant {string}
 */
const API_BASE_URL = getApiBaseUrl();

/**
 * Base WebSocket URL for real-time connections
 * @constant {string}
 */
const WEBSOCKET_BASE_URL = getWebSocketBaseUrl();

/**
 * Authentication endpoints base URL
 * @constant {string}
 */
const AUTH_ENDPOINT = `${API_BASE_URL}/auth`;

/**
 * User management endpoints base URL
 * @constant {string}
 */
const USER_ENDPOINT = `${API_BASE_URL}/users`;

/**
 * Training management endpoints base URL
 * @constant {string}
 */
const TRAININGS_ENDPOINT = `${API_BASE_URL}/trainings`;

/**
 * Evaluation management endpoints base URL
 * @constant {string}
 */
const EVALUATIONS_ENDPOINT = `${API_BASE_URL}/evaluations`;

/**
 * Evaluation cycles endpoints base URL
 * @constant {string}
 */
const CYCLES_ENDPOINT = `${EVALUATIONS_ENDPOINT}/cycle`;

/**
 * AI services endpoints base URL
 * @constant {string}
 */
const AI_ENDPOINT = `${API_BASE_URL}/ai`;

/**
 * Export services endpoints base URL
 * @constant {string}
 */
const EXPORT_ENDPOINT = `${API_BASE_URL}/export`;

/**
 * Statistics endpoints base URL
 * @constant {string}
 */
const STATS_ENDPOINT = `${API_BASE_URL}/stats`;

/**
 * Test endpoints base URL
 * @constant {string}
 */
const TEST_ENDPOINT = `${API_BASE_URL}/test`;

/**
 * Workplace endpoints base URL
 * @constant {string}
 */
const WORKPLACES_ENDPOINT = `${API_BASE_URL}/workplaces`;

/**
 * Messaging endpoints base URL
 * @constant {string}
 */
const MESSAGES_ENDPOINT = `${API_BASE_URL}/messages`;

/**
 * Notifications endpoints base URL
 * @constant {string}
 */
const NOTIFICATIONS_ENDPOINT = `${API_BASE_URL}/notifications`;

/**
 * Comprehensive API endpoints configuration object
 *
 * This object contains all API endpoint configurations organized by feature area.
 * Each endpoint defines:
 * - url: The full endpoint URL
 * - requiresAuth: Whether the endpoint requires authentication
 * - adminOnly: Whether the endpoint requires admin role (optional)
 * - managerOrAdmin: Whether the endpoint requires manager or admin role (optional)
 * - methods: Available HTTP methods for this endpoint (optional)
 *
 * @namespace API_ENDPOINTS
 * @type {Object}
 */
const API_ENDPOINTS = {
  /**
   * Authentication related endpoints
   * @namespace API_ENDPOINTS.auth
   */
  auth: {
    /** @type {EndpointConfig} User login endpoint */
    login: {
      url: `${AUTH_ENDPOINT}/login`,
      requiresAuth: false,
    },
    /** @type {EndpointConfig} User registration endpoint */
    register: {
      url: `${AUTH_ENDPOINT}/register`,
      requiresAuth: false,
    },
    /** @type {EndpointConfig} User logout endpoint */
    logout: {
      url: `${AUTH_ENDPOINT}/logout`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Account confirmation endpoint */
    confirm: {
      url: `${AUTH_ENDPOINT}/confirm`,
      requiresAuth: false,
    },
    /** @type {EndpointConfig} Password reset request endpoint */
    resetPasswordRequest: {
      url: `${AUTH_ENDPOINT}/resetPassword/{email}`,
      requiresAuth: false,
    },
    /** @type {EndpointConfig} Password reset endpoint */
    resetPassword: {
      url: `${AUTH_ENDPOINT}/resetPassword`,
      requiresAuth: false,
    },
    /**
     * Two-factor authentication endpoints
     * @namespace API_ENDPOINTS.auth.twoFactor
     */
    twoFactor: {
      /** @type {EndpointConfig} Setup 2FA endpoint */
      setup: {
        url: `${AUTH_ENDPOINT}/2fa/setup`,
        requiresAuth: true,
      },
      /** @type {EndpointConfig} Verify 2FA setup endpoint */
      setupVerify: {
        url: `${AUTH_ENDPOINT}/2fa/setup/verify`,
        requiresAuth: true,
      },
      /** @type {EndpointConfig} Verify 2FA during login endpoint */
      loginVerify: {
        url: `${AUTH_ENDPOINT}/complete-2fa`,
        requiresAuth: false,
      },
      /** @type {EndpointConfig} Disable 2FA endpoint */
      disable: {
        url: `${AUTH_ENDPOINT}/2fa/disable`,
        requiresAuth: true,
      },
      /** @type {EndpointConfig} Get 2FA status endpoint */
      status: {
        url: `${AUTH_ENDPOINT}/2fa/status`,
        requiresAuth: true,
      },
      /** @type {EndpointConfig} Regenerate backup codes endpoint */
      regenerateBackupCodes: {
        url: `${AUTH_ENDPOINT}/2fa/backup-codes/regenerate`,
        requiresAuth: true,
      },
    },
  },
  /**
   * User management endpoints
   * @namespace API_ENDPOINTS.user
   */
  user: {
    /** @type {EndpointConfig} Get all users endpoint */
    getAll: {
      url: USER_ENDPOINT,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Get current user profile endpoint */
    getMe: {
      url: `${USER_ENDPOINT}/me`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Get user by ID endpoint */
    getById: id => ({
      url: `${USER_ENDPOINT}/${id}`,
      requiresAuth: true,
    }),
    /** @type {EndpointConfig} Update user endpoint */
    update: {
      url: USER_ENDPOINT,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Assign manager to users endpoint */
    assignManager: {
      url: USER_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointFunction} User profile picture endpoints */
    profilePicture: userId => ({
      url: `${USER_ENDPOINT}/${userId}/profile/picture`,
      requiresAuth: true,
    }),
    /** @type {EndpointConfig} Get all managers endpoint */
    getAllManagers: {
      url: `${USER_ENDPOINT}/managers`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Get mini user profiles endpoint */
    getMiniUsers: {
      url: `${USER_ENDPOINT}/compact`,
      requiresAuth: true,
    },
  },
  /**
   * Training management endpoints
   * @namespace API_ENDPOINTS.trainings
   */
  trainings: {
    /** @type {EndpointConfig} Get all trainings endpoint */
    getAll: {
      url: TRAININGS_ENDPOINT,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Create training endpoint */
    create: {
      url: TRAININGS_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointConfig} Update training endpoint */
    update: {
      url: TRAININGS_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointFunction} Deactivate training endpoint */
    deactivate: trainingId => ({
      url: `${TRAININGS_ENDPOINT}/${trainingId}`,
      requiresAuth: true,
      adminOnly: true,
    }),
    /** @type {EndpointConfig} Training area management endpoint */
    area: {
      url: `${TRAININGS_ENDPOINT}/area`,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointFunction} Get unparticipated trainings for user endpoint */
    unparticipated: userId => ({
      url: `${TRAININGS_ENDPOINT}/unparticipated/${userId}`,
      requiresAuth: true,
      managerOrAdmin: true,
    }),
  },
  /**
   * Training participation endpoints
   * @namespace API_ENDPOINTS.trainingParticipation
   */
  trainingParticipation: {
    /** @type {EndpointFunction} Get participations by user endpoint */
    getByUser: userId => ({
      url: `${TRAININGS_ENDPOINT}/participation/${userId}`,
      requiresAuth: true,
    }),
    /** @type {EndpointConfig} Create participation endpoint */
    create: {
      url: `${TRAININGS_ENDPOINT}/participations`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Get unparticipated trainings by user endpoint */
    getUnparticipatedByUser: userId => ({
      url: `${TRAININGS_ENDPOINT}/unparticipated/${userId}`,
      requiresAuth: true,
      managerOrAdmin: true,
    }),
    /** @type {EndpointConfig} Register participation endpoint */
    register: {
      url: `${TRAININGS_ENDPOINT}/participation`,
      requiresAuth: true,
    },
  },
  /**
   * Evaluation management endpoints
   * @namespace API_ENDPOINTS.evaluations
   */
  evaluations: {
    /** @type {EndpointConfig} Get all evaluations endpoint */
    getAll: {
      url: EVALUATIONS_ENDPOINT,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Update evaluation endpoint */
    update: {
      url: EVALUATIONS_ENDPOINT,
      requiresAuth: true,
      managerOrAdmin: true,
    },
    /** @type {EndpointConfig} Close evaluation endpoint */
    close: {
      url: EVALUATIONS_ENDPOINT,
      requiresAuth: true,
      adminOnly: true,
    },
  },
  /**
   * AI services endpoints
   * @namespace API_ENDPOINTS.ai
   */
  ai: {
    /** @type {EndpointConfig} Sentiment analysis endpoint */
    analyzeSentiment: {
      url: `${AI_ENDPOINT}/analyze-sentiment`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Training recommendation endpoint */
    recommendTraining: {
      url: `${AI_ENDPOINT}/recommend-training`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Performance prediction endpoint */
    predictPerformance: {
      url: `${AI_ENDPOINT}/predict-performance`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Chatbot interaction endpoint */
    chatbot: {
      url: `${AI_ENDPOINT}/chatbot`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Generate report endpoint */
    generateReport: {
      url: `${AI_ENDPOINT}/generate-report`,
      requiresAuth: true,
    },
  },
  /**
   * Export services endpoints
   * @namespace API_ENDPOINTS.export
   */
  export: {
    /** @type {EndpointConfig} Export users to CSV endpoint */
    usersCSV: {
      url: `${EXPORT_ENDPOINT}/users/csv`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export filtered users to CSV endpoint */
    usersCSVFiltered: {
      url: `${EXPORT_ENDPOINT}/users/csv/filtered`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export users to XLSX endpoint */
    usersXLSX: {
      url: `${EXPORT_ENDPOINT}/users/xlsx`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export filtered users to XLSX endpoint */
    usersXLSXFiltered: {
      url: `${EXPORT_ENDPOINT}/users/xlsx/filtered`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export trainings to CSV endpoint */
    trainingsCSV: {
      url: `${EXPORT_ENDPOINT}/trainings/csv`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export filtered trainings to CSV endpoint */
    trainingsCSVFiltered: {
      url: `${EXPORT_ENDPOINT}/trainings/csv/filtered`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export trainings to XLSX endpoint */
    trainingsXLSX: {
      url: `${EXPORT_ENDPOINT}/trainings/xlsx`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export filtered trainings to XLSX endpoint */
    trainingsXLSXFiltered: {
      url: `${EXPORT_ENDPOINT}/trainings/xlsx/filtered`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export evaluations to CSV endpoint */
    evaluationsCSV: {
      url: `${EXPORT_ENDPOINT}/evaluations/csv`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export filtered evaluations to CSV endpoint */
    evaluationsCSVFiltered: {
      url: `${EXPORT_ENDPOINT}/evaluations/csv/filtered`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export evaluations to XLSX endpoint */
    evaluationsXLSX: {
      url: `${EXPORT_ENDPOINT}/evaluations/xlsx`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Export filtered evaluations to XLSX endpoint */
    evaluationsXLSXFiltered: {
      url: `${EXPORT_ENDPOINT}/evaluations/xlsx/filtered`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Export specific evaluation to PDF endpoint */
    evaluationPDF: evaluationId => ({
      url: `${EXPORT_ENDPOINT}/evaluations/${evaluationId}/pdf`,
      requiresAuth: true,
    }),
  },
  /**
   * Statistics endpoints
   * @namespace API_ENDPOINTS.stats
   */
  stats: {
    /** @type {EndpointConfig} Manager statistics endpoint */
    managers: {
      url: `${STATS_ENDPOINT}/managers`,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointConfig} Evaluation statistics endpoint */
    evaluations: {
      url: `${STATS_ENDPOINT}/evaluations`,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointFunction} User participation statistics endpoint */
    participations: userId => ({
      url: `${STATS_ENDPOINT}/participation/${userId}`,
      requiresAuth: true,
    }),
    /** @type {EndpointFunction} Personal statistics endpoint */
    personal: userId => ({
      url: `${STATS_ENDPOINT}/personal/${userId}`,
      requiresAuth: true,
    }),
  },
  /**
   * Test endpoints
   * @namespace API_ENDPOINTS.test
   */
  test: {
    /** @type {EndpointConfig} Ping test endpoint */
    ping: {
      url: `${TEST_ENDPOINT}/ping`,
      requiresAuth: false,
    },
    /** @type {EndpointConfig} Email test endpoint */
    email: {
      url: `${TEST_ENDPOINT}/email`,
      requiresAuth: false,
    },
  },
  /**
   * Workplace endpoints
   * @namespace API_ENDPOINTS.workplaces
   */
  workplaces: {
    /** @type {EndpointConfig} Get all workplaces endpoint */
    getAll: {
      url: WORKPLACES_ENDPOINT,
      requiresAuth: false,
    },
  },
  /**
   * Messaging endpoints
   * @namespace API_ENDPOINTS.messages
   */
  messages: {
    /** @type {EndpointFunction} Send message endpoint */
    send: {
      url: recipientId => `${MESSAGES_ENDPOINT}/send/${recipientId}`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Get conversation endpoint */
    conversation: {
      url: otherUserId => `${MESSAGES_ENDPOINT}/conversation/${otherUserId}`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Get all conversations endpoint */
    conversations: {
      url: `${MESSAGES_ENDPOINT}/conversations`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Get unread count endpoint */
    unreadCount: {
      url: `${MESSAGES_ENDPOINT}/unread/count`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Get unread count from sender endpoint */
    unreadCountFromSender: {
      url: senderId => `${MESSAGES_ENDPOINT}/unread/count/${senderId}`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Mark message as read endpoint */
    markAsRead: {
      url: messageId => `${MESSAGES_ENDPOINT}/${messageId}/read`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Get all messages endpoint */
    all: {
      url: `${MESSAGES_ENDPOINT}/all`,
      requiresAuth: true,
    },
  },
  /**
   * Evaluation cycle endpoints
   * @namespace API_ENDPOINTS.cycles
   */
  cycles: {
    /** @type {EndpointConfig} Get current cycle endpoint */
    getCurrent: {
      url: `${CYCLES_ENDPOINT}/current`,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointConfig} Start new cycle endpoint */
    startNew: {
      url: `${CYCLES_ENDPOINT}`,
      requiresAuth: true,
      adminOnly: true,
    },
    /** @type {EndpointConfig} Get all cycles endpoint */
    getAll: {
      url: `${CYCLES_ENDPOINT}/all`,
      requiresAuth: true,
      adminOnly: true,
    },
  },
  /**
   * Notification endpoints
   * @namespace API_ENDPOINTS.notifications
   */
  notifications: {
    /** @type {EndpointConfig} Get user notifications endpoint */
    getUserNotifications: {
      url: `${NOTIFICATIONS_ENDPOINT}/user`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Get paginated user notifications endpoint */
    getUserNotificationsPaginated: {
      url: `${NOTIFICATIONS_ENDPOINT}/user/paginated`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Mark notification as read endpoint */
    markAsRead: {
      url: notificationId => `${NOTIFICATIONS_ENDPOINT}/${notificationId}/read`,
      requiresAuth: true,
    },
    /** @type {EndpointConfig} Mark all notifications as read endpoint */
    markAllAsRead: {
      url: `${NOTIFICATIONS_ENDPOINT}/read-all`,
      requiresAuth: true,
    },
    /** @type {EndpointFunction} Delete notification endpoint */
    delete: {
      url: notificationId => `${NOTIFICATIONS_ENDPOINT}/${notificationId}`,
      requiresAuth: true,
    },
  },
};

/**
 * Exported API configuration constants and endpoints
 */
export {
  /** Base API URL */
  API_BASE_URL,
  /** Base WebSocket URL */
  WEBSOCKET_BASE_URL,
  /** Complete API endpoints configuration */
  API_ENDPOINTS,
  /** Authentication endpoints base URL */
  AUTH_ENDPOINT,
  /** User endpoints base URL */
  USER_ENDPOINT,
  /** Training endpoints base URL */
  TRAININGS_ENDPOINT,
  /** Evaluation endpoints base URL */
  EVALUATIONS_ENDPOINT,
  /** Cycle endpoints base URL */
  CYCLES_ENDPOINT,
  /** AI endpoints base URL */
  AI_ENDPOINT,
  /** Export endpoints base URL */
  EXPORT_ENDPOINT,
  /** Statistics endpoints base URL */
  STATS_ENDPOINT,
  /** Test endpoints base URL */
  TEST_ENDPOINT,
  /** Workplace endpoints base URL */
  WORKPLACES_ENDPOINT,
  /** Message endpoints base URL */
  MESSAGES_ENDPOINT,
  /** Notification endpoints base URL */
  NOTIFICATIONS_ENDPOINT,
};

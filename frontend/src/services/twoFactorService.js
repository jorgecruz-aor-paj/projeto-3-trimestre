/**
 * Two-Factor Authentication Service
 * Handles 2FA related API calls
 */
import { apiRequest } from './apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const twoFactorService = {
  /**
   * Setup two-factor authentication for the current user
   * @returns {Promise<Object>} Setup data including secret, QR code URL, and backup codes
   */
  setup: () => {
    return apiRequest(API_ENDPOINTS.auth.twoFactor.setup, {
      method: 'POST',
    });
  },

  /**
   * Verify and enable two-factor authentication
   * @param {string} code - TOTP code from authenticator app
   * @returns {Promise<Object>} Success response
   */
  verifyAndEnable: code => {
    return apiRequest(API_ENDPOINTS.auth.twoFactor.setupVerify, {
      method: 'POST',
      data: { code },
    });
  },

  /**
   * Verify two-factor authentication code during login
   * @param {string} temporaryToken - Temporary token from initial login
   * @param {string} code - TOTP code or backup code
   * @returns {Promise<string>} Session token for completed login
   */
  verifyLogin: (temporaryToken, code) => {
    return apiRequest(API_ENDPOINTS.auth.twoFactor.loginVerify, {
      method: 'POST',
      data: {
        temporaryToken,
        code,
      },
    });
  },

  /**
   * Disable two-factor authentication
   * @param {string} password - User's current password for confirmation
   * @returns {Promise<Object>} Success response
   */
  disable: password => {
    return apiRequest(API_ENDPOINTS.auth.twoFactor.disable, {
      method: 'DELETE',
      data: { password },
    });
  },

  /**
   * Get two-factor authentication status
   * @returns {Promise<Object>} Status response with isEnabled boolean
   */
  getStatus: () => {
    return apiRequest(API_ENDPOINTS.auth.twoFactor.status, {
      method: 'GET',
    });
  },

  /**
   * Regenerate backup codes
   * @returns {Promise<Object>} Response with new backup codes
   */
  regenerateBackupCodes: () => {
    return apiRequest(API_ENDPOINTS.auth.twoFactor.regenerateBackupCodes, {
      method: 'POST',
    });
  },
};

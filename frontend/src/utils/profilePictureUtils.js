/**
 * Profile Picture utilities
 * Helper functions for managing profile picture cache and updates
 */

import { API_ENDPOINTS } from 'api/apiConfig';

/**
 * Trigger a profile picture update event
 * This will cause all ProfilePicture components to refresh for the specified user
 * @param {Number|String} userId - The ID of the user whose profile picture was updated
 */
export const triggerProfilePictureUpdate = userId => {
  window.dispatchEvent(
    new CustomEvent('profilePictureUpdated', {
      detail: { userId: parseInt(userId) },
    })
  );
};

/**
 * Preload a profile picture for better UX
 * @param {Number|String} userId - The ID of the user whose profile picture to preload
 * @returns {Promise} - Resolves when image is loaded
 */
export const preloadProfilePicture = userId => {
  return new Promise((resolve, reject) => {
    if (!userId) {
      resolve();
      return;
    }

    const img = new Image();
    img.onload = () => resolve();
    img.onerror = () => reject(new Error('Failed to preload profile picture'));
    img.src = API_ENDPOINTS.user.profilePicture(userId).url;
  });
};

/**
 * Get profile picture URL for a user
 * @param {Number|String} userId - The ID of the user
 * @param {String} fallback - Fallback image path
 * @returns {String} - Profile picture URL
 */
export const getProfilePictureUrl = (
  userId,
  fallback = '/images/default-profile-picture.png'
) => {
  if (!userId) return fallback;
  return API_ENDPOINTS.user.profilePicture(userId).url;
};

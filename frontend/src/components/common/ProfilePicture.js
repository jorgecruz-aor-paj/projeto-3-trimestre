import React, { useState, useEffect, useCallback } from 'react';
import PropTypes from 'prop-types';
import { API_ENDPOINTS } from 'api/apiConfig';

// Simple in-memory cache to avoid redundant requests
const imageCache = new Map();

// Debug function to inspect cache (available in browser console)
window.profilePictureCache = {
  getCache: () => imageCache,
  clearCache: () => imageCache.clear(),
  clearUser: userId => imageCache.delete(`profile_${userId}`),
  inspect: () => {
    console.log('Profile Picture Cache Contents:');
    imageCache.forEach((value, key) => {
      console.log(`${key}: ${value}`);
    });
  },
};

/**
 * Reusable ProfilePicture component that handles user profile pictures
 * with automatic cache management and fallback support
 */
export const ProfilePicture = ({
  userId,
  className = '',
  alt = 'Profile Picture',
  fallbackSrc = '/images/default-profile-picture.png', // Extreme fallback only if backend is completely down
  style = {},
  onLoad,
  onError,
  ...props
}) => {
  const [imgSrc, setImgSrc] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // Generate profile picture URL - backend handles all cases including default
  const generateSrc = useCallback(() => {
    if (!userId) return fallbackSrc; // Only use frontend fallback if no userId provided

    // Check if we have a cached URL for this user
    const cacheKey = `profile_${userId}`;
    if (imageCache.has(cacheKey)) {
      return imageCache.get(cacheKey);
    }

    // Backend will serve either user's picture or default-profile-picture.png
    const url = API_ENDPOINTS.user.profilePicture(userId).url;

    /* TODO delete if not needed
    // Just use the path directly without any replacement
    // This ensures we're not making a cross-origin request
    const url = `${
      API_ENDPOINTS.user.profilePicture(userId).fullUrl
    }?_t=${Date.now()}`;
    */

    imageCache.set(cacheKey, url);
    return url;
  }, [userId, fallbackSrc]);

  // Initialize image source
  useEffect(() => {
    const newSrc = generateSrc();
    // Only set imgSrc if we have a valid URL (not empty string)
    if (newSrc && newSrc.trim() !== '') {
      setImgSrc(newSrc);
      setIsLoading(true);
    } else {
      setImgSrc(null);
      setIsLoading(false);
    }
  }, [generateSrc]);

  // Listen for profile picture updates
  useEffect(() => {
    const handleProfilePictureUpdate = event => {
      console.log('Profile picture update event received:', event.detail);
      if (event.detail?.userId === userId) {
        console.log(`Updating profile picture for user ${userId}`);
        // Clear cache for this user
        const cacheKey = `profile_${userId}`;
        imageCache.delete(cacheKey);

        // Force reload with timestamp to bypass browser cache
        const timestamp = Date.now();
        const newSrc = `${
          API_ENDPOINTS.user.profilePicture(userId).url
        }?_t=${timestamp}`;
        // TODO delete if not needed
        //API_ENDPOINTS.user.profilePicture(userId).fullUrl

        console.log(`New profile picture URL: ${newSrc}`);

        // Update our app cache with the new timestamped URL
        imageCache.set(cacheKey, newSrc);

        if (newSrc && newSrc.trim() !== '') {
          setImgSrc(newSrc);
          setIsLoading(true);
        }
      }
    };

    window.addEventListener(
      'profilePictureUpdated',
      handleProfilePictureUpdate
    );

    return () => {
      window.removeEventListener(
        'profilePictureUpdated',
        handleProfilePictureUpdate
      );
    };
  }, [userId]);

  // Handle image load error - only use frontend fallback if backend completely fails
  const handleError = e => {
    console.warn(`Profile picture load error for user ${userId}:`, e);
    setIsLoading(false);

    // Set image src to fallback image if not already set
    if (e.target.src !== fallbackSrc) {
      console.log(`Falling back to default image for user ${userId}`);
      e.target.src = fallbackSrc;
    }

    // Call custom onError handler if provided
    if (onError) {
      onError(e);
    }
  };

  // Handle successful image load
  const handleLoad = e => {
    setIsLoading(false);

    // Call custom onLoad handler if provided
    if (onLoad) {
      onLoad(e);
    }
  };

  return imgSrc ? (
    <img
      src={imgSrc}
      alt={alt}
      className={`${className} ${isLoading ? 'loading' : ''}`}
      style={style}
      onError={handleError}
      onLoad={handleLoad}
      crossOrigin="anonymous"
      {...props}
    />
  ) : null;
};

ProfilePicture.propTypes = {
  userId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  className: PropTypes.string,
  alt: PropTypes.string,
  fallbackSrc: PropTypes.string,
  style: PropTypes.object,
  onLoad: PropTypes.func,
  onError: PropTypes.func,
};

ProfilePicture.defaultProps = {
  className: '',
  alt: 'Profile Picture',
  fallbackSrc: '/images/default-profile-picture.png',
  style: {},
  onLoad: null,
  onError: null,
};

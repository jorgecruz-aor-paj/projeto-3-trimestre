import React, { useState, useEffect, useCallback } from 'react';
import PropTypes from 'prop-types';

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
  const [imgSrc, setImgSrc] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  // Generate profile picture URL - backend handles all cases including default
  const generateSrc = useCallback(() => {
    if (!userId) return fallbackSrc; // Only use frontend fallback if no userId provided
    // Backend will serve either user's picture or default-profile-picture.png
    return `/api/users/${userId}/profile/picture`;
  }, [userId, fallbackSrc]);

  // Initialize image source
  useEffect(() => {
    const newSrc = generateSrc();
    setImgSrc(newSrc);
    setIsLoading(true);
  }, [generateSrc]);

  // Listen for profile picture updates
  useEffect(() => {
    const handleProfilePictureUpdate = event => {
      if (event.detail?.userId === userId) {
        // Force reload with timestamp to bypass cache
        const timestamp = Date.now();
        setImgSrc(`/api/users/${userId}/profile/picture?_t=${timestamp}`);
        setIsLoading(true);
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
    setIsLoading(false);
    // This should rarely happen since backend serves default-profile-picture.png
    // Only use frontend fallback if backend is completely down
    if (e.target.src !== fallbackSrc) {
      console.warn(
        `Failed to load profile picture from backend for user ${userId}, falling back to frontend default`
      );
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

  return (
    <img
      src={imgSrc}
      alt={alt}
      className={`${className} ${isLoading ? 'loading' : ''}`}
      style={style}
      onError={handleError}
      onLoad={handleLoad}
      {...props}
    />
  );
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

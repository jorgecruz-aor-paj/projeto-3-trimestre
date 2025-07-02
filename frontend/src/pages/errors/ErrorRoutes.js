import React from 'react';
import { useLocation } from 'react-router-dom';
import {
  NotFoundPage,
  ForbiddenPage,
  ServerErrorPage,
  NetworkErrorPage,
  GenericErrorPage,
} from 'pages/errors';

/**
 * Route wrapper for 404 errors
 */
export const NotFoundRoute = () => {
  return <NotFoundPage />;
};

/**
 * Route wrapper for 403 errors
 */
export const ForbiddenRoute = () => {
  return <ForbiddenPage />;
};

/**
 * Route wrapper for 500 errors
 */
export const ServerErrorRoute = () => {
  return <ServerErrorPage />;
};

/**
 * Route wrapper for network errors
 */
export const NetworkErrorRoute = () => {
  return <NetworkErrorPage />;
};

/**
 * Route wrapper for generic errors that can receive state from navigation
 */
export const GenericErrorRoute = () => {
  const location = useLocation();
  const state = location.state || {};

  return (
    <GenericErrorPage
      errorCode={state.errorCode}
      errorMessage={state.errorMessage}
      onRetry={state.onRetry}
    />
  );
};

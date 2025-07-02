import { useNavigate } from 'react-router-dom';
import { useCallback } from 'react';

/**
 * Custom hook for handling different types of errors
 * Provides navigation to appropriate error pages based on error type
 * @returns {Object} Error handling functions
 */
export const useErrorHandler = () => {
  const navigate = useNavigate();

  const handleError = useCallback(
    (error, errorInfo = {}) => {
      // Log error for debugging
      console.error('Error handled by useErrorHandler:', error, errorInfo);

      // Determine error type and navigate accordingly
      if (error?.response?.status) {
        const status = error.response.status;

        switch (status) {
          case 404:
            navigate('/error/404');
            break;
          case 403:
          case 401:
            navigate('/error/403');
            break;
          case 500:
          case 502:
          case 503:
          case 504:
            navigate('/error/500');
            break;
          default:
            navigate('/error/generic', {
              state: {
                errorCode: status,
                errorMessage: error.response?.data?.message || error.message,
              },
            });
        }
      } else if (
        error?.name === 'NetworkError' ||
        error?.message?.includes('Network')
      ) {
        navigate('/error/network');
      } else if (
        error?.name === 'TypeError' &&
        error?.message?.includes('fetch')
      ) {
        navigate('/error/network');
      } else {
        // Generic error
        navigate('/error/generic', {
          state: {
            errorMessage: error?.message || 'An unexpected error occurred',
          },
        });
      }
    },
    [navigate]
  );

  const handleNotFound = useCallback(() => {
    navigate('/error/404');
  }, [navigate]);

  const handleForbidden = useCallback(() => {
    navigate('/error/403');
  }, [navigate]);

  const handleServerError = useCallback(() => {
    navigate('/error/500');
  }, [navigate]);

  const handleNetworkError = useCallback(() => {
    navigate('/error/network');
  }, [navigate]);

  const handleGenericError = useCallback(
    (message = null, code = null) => {
      navigate('/error/generic', {
        state: {
          errorMessage: message,
          errorCode: code,
        },
      });
    },
    [navigate]
  );

  return {
    handleError,
    handleNotFound,
    handleForbidden,
    handleServerError,
    handleNetworkError,
    handleGenericError,
  };
};

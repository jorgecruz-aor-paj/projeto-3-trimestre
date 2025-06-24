import { useState } from 'react';
import { useAlert } from 'contexts/AlertContext';
import { useNavigate } from 'react-router-dom';

export const useApiError = () => {
  const [error, setError] = useState(null);
  const { showAlert } = useAlert();
  const navigate = useNavigate();

  const handleError = apiError => {
    console.error('API Error:', apiError);

    switch (apiError.type) {
      case 'auth':
        showAlert({
          title: 'Session Expired',
          message: apiError.message,
          variant: 'warning',
          confirmLabel: 'Go to Login',
          onConfirm: () => navigate('/login'),
        });
        break;

      case 'server':
        showAlert({
          title: 'Server Error',
          message: 'Something went wrong. Please try again later.',
          variant: 'danger',
        });
        break;

      case 'network':
        showAlert({
          title: 'Connection Error',
          message: apiError.message,
          variant: 'warning',
        });
        break;

      default:
        showAlert({
          title: 'Error',
          message: apiError.message,
          variant: 'danger',
        });
    }
  };

  const clearError = () => setError(null);

  return { error, handleError, clearError };
};

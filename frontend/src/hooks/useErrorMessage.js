import { useTranslation } from 'react-i18next';

/**
 * Custom hook for handling form error messages with translation
 * @returns {Function} getErrorMessage function
 */
export const useErrorMessage = () => {
  const { t } = useTranslation();

  const getErrorMessage = error => {
    if (!error) return null;
    if (typeof error === 'string') {
      return t(error);
    }
    if (error.key && error.options) {
      return t(error.key, error.options);
    }
    return t(error);
  };

  return { getErrorMessage };
};

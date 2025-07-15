/**
 * @fileoverview Custom hook for handling form error messages with translation support
 * @module hooks/useErrorMessage
 * @requires react-i18next
 */

import { useTranslation } from 'react-i18next';

/**
 * @typedef {Object} ErrorMessage
 * @property {string} key - Translation key for the error message
 * @property {Object} [options] - Options for error message interpolation
 */

/**
 * @typedef {Object} ErrorMessageHook
 * @property {function} getErrorMessage - Function to get translated error message
 */

/**
 * Custom hook for handling form error messages with translation support
 *
 * This hook provides a unified way to handle error messages throughout the application,
 * supporting both simple string errors and complex error objects with translation keys
 * and interpolation options.
 *
 * The hook handles multiple error formats:
 * - Simple strings (directly translated)
 * - Error objects with `key` and `options` properties
 * - Translation keys with or without interpolation
 *
 * @function useErrorMessage
 * @returns {ErrorMessageHook} Object containing the getErrorMessage function
 *
 * @example
 * // Basic usage in a form component
 * import { useErrorMessage } from 'hooks/useErrorMessage';
 *
 * function MyForm() {
 *   const { getErrorMessage } = useErrorMessage();
 *   const [errors, setErrors] = useState({});
 *
 *   return (
 *     <form>
 *       <input name="email" />
 *       {errors.email && (
 *         <div className="error">
 *           {getErrorMessage(errors.email)}
 *         </div>
 *       )}
 *     </form>
 *   );
 * }
 *
 * @example
 * // Handling different error formats
 * const { getErrorMessage } = useErrorMessage();
 *
 * // Simple string error
 * const simpleError = getErrorMessage('validation.required');
 *
 * // Error object with interpolation
 * const complexError = getErrorMessage({
 *   key: 'validation.minLength',
 *   options: { min: 8 }
 * });
 *
 * @example
 * // Integration with validation system
 * import { useFormValidation } from 'utils/validation';
 * import { useErrorMessage } from 'hooks/useErrorMessage';
 *
 * function LoginForm() {
 *   const { getErrorMessage } = useErrorMessage();
 *   const { formData, errors, handleChange } = useFormValidation(
 *     { email: '', password: '' },
 *     loginValidationRules
 *   );
 *
 *   return (
 *     <form>
 *       <input
 *         type="email"
 *         value={formData.email}
 *         onChange={(e) => handleChange('email', e.target.value)}
 *       />
 *       {errors.email && (
 *         <span className="error-message">
 *           {getErrorMessage(errors.email)}
 *         </span>
 *       )}
 *     </form>
 *   );
 * }
 *
 * @since 1.0.0
 * @author Development Team
 */
export const useErrorMessage = () => {
  const { t } = useTranslation();

  /**
   * Get translated error message from various error formats
   *
   * @function getErrorMessage
   * @memberof useErrorMessage
   * @param {string|ErrorMessage|null} error - The error to translate
   * @returns {string|null} Translated error message or null if no error
   *
   * @example
   * // Simple string error
   * getErrorMessage('validation.required'); // "This field is required"
   *
   * @example
   * // Error object with interpolation
   * getErrorMessage({
   *   key: 'validation.minLength',
   *   options: { min: 8 }
   * }); // "Minimum length is 8 characters"
   *
   * @example
   * // Null/undefined error
   * getErrorMessage(null); // null
   * getErrorMessage(undefined); // null
   */
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

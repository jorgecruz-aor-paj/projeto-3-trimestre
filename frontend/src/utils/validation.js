/**
 * @fileoverview Comprehensive form validation utilities and rules
 * @module utils/validation
 * @requires react
 */

import { useState } from 'react';
import { REGEX, VALIDATION_LIMITS } from './constants';

/**
 * @typedef {Object} ValidationRule
 * @property {boolean} [required] - Whether the field is required
 * @property {number} [minLength] - Minimum length for string values
 * @property {number} [maxLength] - Maximum length for string values
 * @property {RegExp} [pattern] - Regular expression pattern to match
 * @property {string} [patternError] - Custom error message for pattern mismatch
 * @property {boolean} [url] - Whether the field should be a valid URL
 * @property {function} [custom] - Custom validation function
 */

/**
 * @typedef {Object} ValidationRules
 * @property {ValidationRule} [fieldName] - Validation rules for each field
 */

/**
 * @typedef {Object} ValidationError
 * @property {string} key - Translation key for the error message
 * @property {Object} [options] - Options for error message interpolation
 */

/**
 * @typedef {Object} FormValidationHook
 * @property {Object} formData - Current form data state
 * @property {Object} errors - Current validation errors
 * @property {function} handleChange - Function to handle form field changes
 * @property {function} validateAll - Function to validate all form fields
 * @property {function} resetForm - Function to reset form to initial state
 * @property {function} setFormData - Function to directly set form data
 */

/**
 * Validates a single field value against a set of validation rules
 *
 * This function performs comprehensive validation including:
 * - Required field validation
 * - String length validation (min/max)
 * - Pattern/regex validation
 * - URL format validation
 * - Custom validation functions
 *
 * @function validateField
 * @param {*} value - The value to validate
 * @param {ValidationRule} rules - The validation rules to apply
 * @param {Object} [formData=null] - The entire form data (for cross-field validation)
 * @returns {string|ValidationError|null} Error message/object or null if valid
 *
 * @example
 * // Basic required field validation
 * const error = validateField('', { required: true });
 * console.log(error); // 'validation.required'
 *
 * @example
 * // Pattern validation with custom error
 * const emailError = validateField('invalid-email', {
 *   pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
 *   patternError: 'validation.emailInvalid'
 * });
 *
 * @example
 * // Custom validation with form data
 * const passwordError = validateField('123', {
 *   custom: (value, formData) => {
 *     if (value !== formData.confirmPassword) {
 *       return 'validation.passwordsDontMatch';
 *     }
 *     return null;
 *   }
 * }, { confirmPassword: '456' });
 *
 * @since 1.0.0
 */
export const validateField = (value, rules, formData = null) => {
  // Return null if no rules are defined for this field
  if (!rules) {
    return null;
  }

  if (
    rules.required &&
    (!value || (typeof value === 'string' && !value.trim()))
  ) {
    return 'validation.required';
  }

  if (rules.minLength && value?.length < rules.minLength) {
    return {
      key: 'validation.minLength',
      options: { min: rules.minLength },
    };
  }

  if (rules.maxLength && value?.length > rules.maxLength) {
    return {
      key: 'validation.maxLength',
      options: { max: rules.maxLength },
    };
  }

  if (rules.pattern && value && !rules.pattern.test(value)) {
    return rules.patternError || 'validation.pattern';
  }

  if (rules.url && value && value.trim()) {
    try {
      new URL(value);
    } catch {
      return 'validation.url';
    }
  }

  if (rules.custom && typeof rules.custom === 'function') {
    return rules.custom(value, formData);
  }

  return null;
};

/**
 * Validates an entire form object against validation rules
 *
 * This function validates all fields in a form object and returns
 * an object containing validation errors for each field that fails validation.
 *
 * @function validateForm
 * @param {Object} formData - The form data object to validate
 * @param {ValidationRules} validationRules - The validation rules for each field
 * @returns {Object} Object with field names as keys and error messages as values
 *
 * @example
 * // Validate a complete form
 * const formData = {
 *   email: 'invalid-email',
 *   password: '123'
 * };
 *
 * const rules = {
 *   email: { required: true, pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/ },
 *   password: { required: true, minLength: 8 }
 * };
 *
 * const errors = validateForm(formData, rules);
 * // Returns: { email: 'validation.pattern', password: { key: 'validation.minLength', options: { min: 8 } } }
 *
 * @since 1.0.0
 */
export const validateForm = (formData, validationRules) => {
  try {
    const errors = {};

    Object.keys(validationRules).forEach(field => {
      // Skip validation if the field doesn't exist in formData
      if (field in formData) {
        const error = validateField(
          formData[field],
          validationRules[field],
          formData
        );
        if (error) {
          errors[field] = error;
        }
      }
    });

    return errors;
  } catch (err) {
    console.error('Error in form validation:', err);
    return {}; // Return empty errors to avoid blocking the form
  }
};

/**
 * Custom React hook for form validation with real-time feedback
 *
 * This hook provides a complete form validation solution with:
 * - Real-time validation on field changes
 * - Form-wide validation
 * - Form state management
 * - Reset functionality
 *
 * @function useFormValidation
 * @param {Object} initialFormData - Initial form data state
 * @param {ValidationRules} validationRules - Validation rules for the form
 * @returns {FormValidationHook} Form validation utilities and state
 *
 * @example
 * // Using the hook in a component
 * function MyForm() {
 *   const { formData, errors, handleChange, validateAll, resetForm } = useFormValidation(
 *     { email: '', password: '' },
 *     {
 *       email: { required: true, pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/ },
 *       password: { required: true, minLength: 8 }
 *     }
 *   );
 *
 *   const handleSubmit = (e) => {
 *     e.preventDefault();
 *     if (validateAll()) {
 *       // Form is valid, submit data
 *       console.log('Form submitted:', formData);
 *     }
 *   };
 *
 *   return (
 *     <form onSubmit={handleSubmit}>
 *       <input
 *         type="email"
 *         value={formData.email}
 *         onChange={(e) => handleChange('email', e.target.value)}
 *       />
 *       {errors.email && <span>{errors.email}</span>}
 *
 *       <input
 *         type="password"
 *         value={formData.password}
 *         onChange={(e) => handleChange('password', e.target.value)}
 *       />
 *       {errors.password && <span>{errors.password}</span>}
 *
 *       <button type="submit">Submit</button>
 *       <button type="button" onClick={resetForm}>Reset</button>
 *     </form>
 *   );
 * }
 *
 * @since 1.0.0
 */
export const useFormValidation = (initialFormData, validationRules) => {
  const [formData, setFormData] = useState(initialFormData);
  const [errors, setErrors] = useState({});

  const handleChange = (name, value) => {
    const newFormData = { ...formData, [name]: value };
    setFormData(newFormData);

    // Validate single field on change
    const fieldError = validateField(value, validationRules[name], newFormData);
    setErrors(prevErrors => ({
      ...prevErrors,
      [name]: fieldError,
    }));
  };

  const validateAll = () => {
    const newErrors = validateForm(formData, validationRules);
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const resetForm = () => {
    setFormData(initialFormData);
    setErrors({});
  };

  return {
    formData,
    errors,
    handleChange,
    validateAll,
    resetForm,
    setFormData,
  };
};

/**
 * Validation rules for training area management forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} name - Training area name validation
 */
export const trainingAreaValidationRules = {
  name: {
    required: true,
    minLength: VALIDATION_LIMITS.TRAINING_AREA_NAME_MIN_LENGTH,
    maxLength: VALIDATION_LIMITS.TRAINING_AREA_NAME_MAX_LENGTH,
    pattern: REGEX.TRAINING_AREA_NAME,
    patternError: 'validation.trainingAreaNameFormat',
  },
};

/**
 * Validation rules for training management forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} title - Training title validation
 * @property {ValidationRule} description - Training description validation
 * @property {ValidationRule} duration - Training duration validation (15-3000 minutes)
 * @property {ValidationRule} language - Training language validation
 * @property {ValidationRule} link - Training link URL validation
 * @property {ValidationRule} trainingArea - Training area selection validation
 */
export const trainingValidationRules = {
  title: {
    required: true,
    minLength: VALIDATION_LIMITS.TRAINING_TITLE_MIN,
    maxLength: VALIDATION_LIMITS.TRAINING_TITLE_MAX,
  },
  description: {
    required: true,
    minLength: VALIDATION_LIMITS.TRAINING_DESC_MIN,
    maxLength: VALIDATION_LIMITS.TRAINING_DESC_MAX,
  },
  duration: {
    required: true,
    pattern: REGEX.NUMBER_ONLY,
    patternError: 'validation.numberOnly',
    //enforces a whole number between 15 and 3000
    custom: value => {
      const numValue = Number(value);
      if (isNaN(numValue) || !Number.isInteger(numValue)) {
        return 'validation.numberOnly';
      }
      if (numValue < VALIDATION_LIMITS.TRAINING_DURATION_MIN) {
        return {
          key: 'validation.minValue',
          options: { min: VALIDATION_LIMITS.TRAINING_DURATION_MIN },
        };
      }
      if (numValue > VALIDATION_LIMITS.TRAINING_DURATION_MAX) {
        return {
          key: 'validation.maxValue',
          options: { max: VALIDATION_LIMITS.TRAINING_DURATION_MAX },
        };
      }
      return null;
    },
  },
  language: {
    required: true,
  },
  link: {
    required: true,
    url: true,
  },
  trainingArea: {
    required: true,
    custom: value => (!value?.id ? 'validation.trainingAreaRequired' : null),
  },
};

/**
 * Validation rules for user authentication (login) forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} email - Email validation with format checking
 * @property {ValidationRule} password - Password validation with security requirements
 */
export const loginValidationRules = {
  email: {
    required: true,
    pattern: REGEX.EMAIL_STRICT,
    patternError: 'validation.emailInvalid',
    maxLength: VALIDATION_LIMITS.LOGIN_EMAIL_MAX_LENGTH,
  },
  password: {
    required: true,
    pattern: REGEX.PASSWORD,
    patternError: 'validation.passwordInvalid',
    minLength: VALIDATION_LIMITS.LOGIN_PASSWORD_MIN_LENGTH,
  },
};

/**
 * Validation rules for user registration forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} email - Email validation with format checking
 * @property {ValidationRule} password - Password validation with security requirements
 * @property {ValidationRule} confirmPassword - Password confirmation validation
 */
export const registerValidationRules = {
  email: {
    required: true,
    pattern: REGEX.EMAIL,
    patternError: 'validation.emailInvalid',
    maxLength: VALIDATION_LIMITS.REGISTER_EMAIL_MAX_LENGTH,
  },
  password: {
    required: true,
    pattern: REGEX.PASSWORD,
    patternError: 'validation.passwordInvalid',
    minLength: VALIDATION_LIMITS.PASSWORD_MIN_LENGTH,
  },
  confirmPassword: {
    required: true,
    minLength: VALIDATION_LIMITS.CONFIRM_PASSWORD_MIN_LENGTH,
    custom: (value, formData) => {
      if (value !== formData?.password) {
        return 'validation.passwordsDontMatch';
      }
      return null;
    },
  },
};

/**
 * Validation rules for user profile management forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} firstName - First name validation
 * @property {ValidationRule} lastName - Last name validation
 * @property {ValidationRule} phoneNumber - Phone number validation (9 digits)
 * @property {ValidationRule} address - Address validation
 * @property {ValidationRule} birthdate - Birthdate validation (age 13-120)
 * @property {ValidationRule} biography - Biography validation (optional)
 * @property {ValidationRule} workplaceId - Workplace selection validation
 */
export const profileValidationRules = {
  firstName: {
    required: true,
    minLength: VALIDATION_LIMITS.NAME_MIN_LENGTH,
    maxLength: VALIDATION_LIMITS.NAME_MAX_LENGTH,
  },
  lastName: {
    required: true,
    minLength: VALIDATION_LIMITS.NAME_MIN_LENGTH,
    maxLength: VALIDATION_LIMITS.NAME_MAX_LENGTH,
  },
  phoneNumber: {
    required: true,
    pattern: REGEX.PHONE,
    patternError: 'validation.phoneInvalid',
  },
  address: {
    required: true,
    maxLength: VALIDATION_LIMITS.ADDRESS_MAX_LENGTH,
  },
  birthdate: {
    required: true,
    custom: value => {
      if (!value) return null;
      const date = new Date(value);
      const today = new Date();
      const minDate = new Date(
        today.getFullYear() - VALIDATION_LIMITS.BIRTHDATE_MAX_AGE,
        today.getMonth(),
        today.getDate()
      );
      const maxDate = new Date(
        today.getFullYear() - VALIDATION_LIMITS.BIRTHDATE_MIN_AGE,
        today.getMonth(),
        today.getDate()
      );

      if (date < minDate || date > maxDate) {
        return 'validation.birthdateInvalid';
      }
      return null;
    },
  },
  biography: {
    required: false,
    maxLength: VALIDATION_LIMITS.BIOGRAPHY_MAX_LENGTH,
  },
  workplaceId: {
    required: true,
  },
};

/**
 * Validation rules for evaluation forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} title - Evaluation title validation
 * @property {ValidationRule} feedback - Evaluation feedback validation
 * @property {ValidationRule} status - Evaluation status validation
 * @property {ValidationRule} scale - Evaluation scale validation
 */
export const evaluationValidationRules = {
  title: {
    required: true,
    minLength: VALIDATION_LIMITS.EVALUATION_TITLE_MIN,
    maxLength: VALIDATION_LIMITS.EVALUATION_TITLE_MAX,
  },
  feedback: {
    required: true,
    minLength: VALIDATION_LIMITS.EVALUATION_FEEDBACK_MIN,
    maxLength: VALIDATION_LIMITS.EVALUATION_FEEDBACK_MAX,
  },
  status: {
    required: true,
  },
  scale: {
    required: true,
  },
};

/**
 * Validation rules for messaging forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} content - Message content validation
 */
export const messageValidationRules = {
  content: {
    required: true,
    maxLength: VALIDATION_LIMITS.MESSAGE_MAX_LENGTH,
  },
};

/**
 * Validation rules for password reset request forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} email - Email validation for reset requests
 */
export const resetPasswordRequestValidationRules = {
  email: {
    required: true,
    pattern: REGEX.EMAIL_STRICT,
    patternError: 'validation.emailInvalid',
    maxLength: VALIDATION_LIMITS.EMAIL_MAX_LENGTH,
  },
};

/**
 * Validation rules for setting new password forms
 *
 * @constant {ValidationRules}
 * @property {ValidationRule} password - New password validation with security requirements
 * @property {ValidationRule} confirmPassword - Password confirmation validation
 */
export const setNewPasswordValidationRules = {
  password: {
    required: true,
    pattern: REGEX.PASSWORD,
    patternError: 'validation.passwordInvalid',
    minLength: VALIDATION_LIMITS.SET_NEW_PASSWORD_MIN_LENGTH,
  },
  confirmPassword: {
    required: true,
    minLength: VALIDATION_LIMITS.CONFIRM_PASSWORD_MIN_LENGTH,
    custom: (value, formData) => {
      if (value !== formData?.password) {
        return 'validation.passwordsDontMatch';
      }
      return null;
    },
  },
};

import { useState } from 'react';

/**
 * Validates a value against a set of validation rules
 * @param {*} value - The value to validate
 * @param {Object} rules - The validation rules
 * @param {Object} formData - The entire form data (for cross-field validation)
 * @returns {string|object|null} Error message/object or null if valid
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
 * @param {Object} formData - The form data object
 * @param {Object} validationRules - The validation rules for each field
 * @returns {Object} Object with field names as keys and error messages as values
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
 * Custom hook for form validation
 * @param {Object} initialFormData - Initial form data
 * @param {Object} validationRules - Validation rules for the form
 * @returns {Object} Form validation utilities
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
 * Validation rules for training area form
 */
export const trainingAreaValidationRules = {
  name: {
    required: true,
    minLength: 2,
    maxLength: 50,
    pattern: /^[a-zA-Z0-9\s\-]+$/,
    patternError: 'validation.trainingAreaNameFormat',
  },
};

/**
 * Validation rules for training forms
 */
export const trainingValidationRules = {
  title: {
    required: true,
    minLength: 3,
    maxLength: 100,
  },
  description: {
    required: true,
    minLength: 10,
    maxLength: 2000,
  },
  duration: {
    required: true,
    pattern: /^[0-9]+$/,
    patternError: 'validation.numberOnly',
    //enforces a whole number between 15 and 3000
    custom: value => {
      const numValue = Number(value);
      if (isNaN(numValue) || !Number.isInteger(numValue)) {
        return 'validation.numberOnly';
      }
      if (numValue < 15) {
        return {
          key: 'validation.minValue',
          options: { min: 15 },
        };
      }
      if (numValue > 3000) {
        return {
          key: 'validation.maxValue',
          options: { max: 3000 },
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
 * Validation rules for login forms
 */
export const loginValidationRules = {
  email: {
    required: true,
    pattern:
      /^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,})$/,
    patternError: 'validation.emailInvalid',
    maxLength: 255,
  },
  password: {
    required: true,
    pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*#?&.-_])\S{8,}$/,
    patternError: 'validation.passwordInvalid',
    minLength: 8,
  },
};

/**
 * Validation rules for registration forms
 */
export const registerValidationRules = {
  email: {
    required: true,
    pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    patternError: 'validation.emailInvalid',
    maxLength: 255,
  },
  password: {
    required: true,
    pattern: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?!.*[ \\t\\n\\r\\f\\u0000\\u00A0]).{8,}$/,
    patternError: 'validation.passwordInvalid',
    minLength: 8,
  },
  confirmPassword: {
    required: true,
    minLength: 8,
    custom: (value, formData) => {
      if (value !== formData?.password) {
        return 'validation.passwordsDontMatch';
      }
      return null;
    },
  },
};

/**
 * Validation rules for user profile forms
 */
export const profileValidationRules = {
  firstName: {
    required: true,
    minLength: 2,
    maxLength: 50,
  },
  lastName: {
    required: true,
    minLength: 2,
    maxLength: 50,
  },
  phoneNumber: {
    required: true,
    pattern: /^\d{9}$/,
    patternError: 'validation.phoneInvalid',
  },
  address: {
    required: true,
    maxLength: 200,
  },
  birthdate: {
    required: true,
    custom: value => {
      if (!value) return null;
      const date = new Date(value);
      const today = new Date();
      const minDate = new Date(
        today.getFullYear() - 120,
        today.getMonth(),
        today.getDate()
      );
      const maxDate = new Date(
        today.getFullYear() - 13,
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
    maxLength: 500,
  },
  workplaceId: {
    required: true,
  },
};

/**
 * Validation rules for evaluation forms
 */
export const evaluationValidationRules = {
  title: {
    required: true,
    minLength: 5,
    maxLength: 150,
  },
  feedback: {
    required: true,
    minLength: 3,
    maxLength: 2000,
  },
  status: {
    required: true,
  },
  scale: {
    required: true,
  },
};

/**
 * Validation rules for message forms
 */
export const messageValidationRules = {
  content: {
    required: true,
    maxLength: 5000,
  },
};

/**
 * Validation rules for reset password request form
 */
export const resetPasswordRequestValidationRules = {
  email: {
    required: true,
    pattern:
      /^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,})$/,
    patternError: 'validation.emailInvalid',
    maxLength: 255,
  },
};

export const setNewPasswordValidationRules = {
  password: {
    required: true,
    pattern: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?!.*[ \\t\\n\\r\\f\\u0000\\u00A0]).{8,}$/,
    patternError: 'validation.passwordInvalid',
    minLength: 8,
  },
  confirmPassword: {
    required: true,
    minLength: 8,
    custom: (value, formData) => {
      if (value !== formData?.password) {
        return 'validation.passwordsDontMatch';
      }
      return null;
    },
  },
};

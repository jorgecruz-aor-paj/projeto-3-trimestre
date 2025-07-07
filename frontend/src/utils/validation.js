import { useState } from 'react';

/**
 * Validates a value against a set of validation rules
 * @param {*} value - The value to validate
 * @param {Object} rules - The validation rules
 * @returns {string|null} Error message or null if valid
 */
export const validateField = (value, rules) => {
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
    return rules.custom(value);
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
  const errors = {};

  Object.keys(validationRules).forEach(field => {
    const error = validateField(formData[field], validationRules[field]);
    if (error) {
      errors[field] = error;
    }
  });

  return errors;
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
 * Hook for form validation
 * @param {Object} initialData - Initial form data
 * @param {Object} validationRules - Validation rules
 * @returns {Object} Form state and handlers
 */
export const useFormValidation = (initialData, validationRules) => {
  const [formData, setFormData] = useState(initialData);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  const handleChange = (name, value) => {
    setFormData(prev => ({ ...prev, [name]: value }));
    setTouched(prev => ({ ...prev, [name]: true }));

    // Validate field on change
    const error = validateField(value, validationRules[name]);
    setErrors(prev => ({ ...prev, [name]: error }));
  };

  const validateAll = () => {
    const newErrors = validateForm(formData, validationRules);
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const reset = () => {
    setFormData(initialData);
    setErrors({});
    setTouched({});
  };

  return {
    formData,
    errors,
    touched,
    handleChange,
    validateAll,
    reset,
    setFormData,
  };
};

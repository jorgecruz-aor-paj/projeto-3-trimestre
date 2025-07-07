// src/components/forms/FormInput.jsx
import React from 'react';
import { Form } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

/**
 * A reusable form input with validation feedback
 * @param {Object} props
 * @param {string} props.name - Input name (matches formData key)
 * @param {string} props.label - Field label
 * @param {string} props.type - Input type (text, password, email, etc.)
 * @param {string} props.value - Current value
 * @param {function} props.onChange - Input change handler
 * @param {string} [props.placeholder] - Input placeholder
 * @param {string|Object} [props.error] - Validation error message or object
 * @param {boolean} [props.disabled] - Whether the input is disabled
 * @param {number} [props.maxLength] - Maximum length for the input
 * @param {number} [props.minLength] - Minimum length for the input
 */
export const FormInput = ({
  name,
  label,
  type,
  value,
  onChange,
  placeholder,
  error,
  disabled = false,
  maxLength,
  minLength,
}) => {
  const { t } = useTranslation();
  const isTextarea = type === 'textarea';
  const currentLength = value?.length || 0;
  const showLength =
    (maxLength || minLength) && (isTextarea || type === 'text');

  const getLengthText = () => {
    const parts = [];
    parts.push(`${currentLength}`);

    if (minLength && maxLength) {
      parts.push(`(${minLength}-${maxLength})`);
    } else if (maxLength) {
      parts.push(`/${maxLength}`);
    } else if (minLength) {
      parts.push(`(min: ${minLength})`);
    }

    parts.push(t('forms.characters'));
    return parts.join(' ');
  };

  return (
    <Form.Group className="mb-3">
      <Form.Label
        htmlFor={name}
        className="d-flex justify-content-between align-items-center"
      >
        {label}
        {showLength && (
          <small
            className={`text-${currentLength < minLength ? 'danger' : 'muted'}`}
          >
            {getLengthText()}
          </small>
        )}
      </Form.Label>
      {isTextarea ? (
        <Form.Control
          as="textarea"
          rows={4}
          id={name}
          name={name}
          value={value}
          placeholder={placeholder}
          onChange={onChange}
          isInvalid={!!error}
          disabled={disabled}
          maxLength={maxLength}
          minLength={minLength}
        />
      ) : (
        <Form.Control
          id={name}
          type={type}
          name={name}
          value={value}
          placeholder={placeholder}
          onChange={onChange}
          isInvalid={!!error}
          disabled={disabled}
          maxLength={maxLength}
          minLength={minLength}
        />
      )}
      <Form.Control.Feedback type="invalid">
        {error &&
          (typeof error === 'object' && error.key
            ? t(error.key, error.options)
            : typeof error === 'string'
            ? t(error)
            : error)}
      </Form.Control.Feedback>
    </Form.Group>
  );
};

// src/components/forms/FormInput.jsx
import React from 'react';
import { Form } from 'react-bootstrap';

/**
 * A reusable form input with validation feedback
 * @param {Object} props
 * @param {string} props.name - Input name (matches formData key)
 * @param {string} props.label - Field label
 * @param {string} props.type - Input type (text, password, email, etc.)
 * @param {string} props.value - Current value
 * @param {function} props.onChange - Input change handler
 * @param {string} [props.placeholder] - Input placeholder
 * @param {string} [props.error] - Validation error message
 * @param {boolean} [props.disabled] - Whether the input is disabled
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
}) => {
  const isTextarea = type === 'textarea';

  return (
    <Form.Group className="mb-3">
      <Form.Label htmlFor={name}>{label}</Form.Label>
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
        />
      )}
      <Form.Control.Feedback type="invalid">{error}</Form.Control.Feedback>
    </Form.Group>
  );
};

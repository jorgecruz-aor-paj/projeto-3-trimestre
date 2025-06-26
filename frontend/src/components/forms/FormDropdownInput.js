import React from 'react';
import { Form } from 'react-bootstrap';

/**
 * Reusable dropdown/select input for forms
 * @param {Object} props
 * @param {string} props.name
 * @param {string} props.label
 * @param {string|number} props.value
 * @param {function} props.onChange
 * @param {Array} props.options - Array of { value, label }
 * @param {string} [props.error]
 * @param {boolean} [props.disabled]
 * @param {string} [props.placeholder]
 */
export const FormDropdownInput = ({
  name,
  label,
  value,
  onChange,
  options,
  error,
  disabled,
  placeholder,
}) => (
  <Form.Group className="mb-3">
    {label && <Form.Label htmlFor={name}>{label}</Form.Label>}
    <Form.Select
      id={name}
      name={name}
      value={value}
      onChange={onChange}
      isInvalid={!!error}
      disabled={disabled}
    >
      <option value="">{placeholder || label}</option>
      {options.map(opt => (
        <option key={opt.value} value={opt.value}>
          {opt.label}
        </option>
      ))}
    </Form.Select>
    <Form.Control.Feedback type="invalid">{error}</Form.Control.Feedback>
  </Form.Group>
);

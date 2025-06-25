// src/components/forms/SubmitButton.jsx
import React from 'react';
import { Button, Spinner } from 'react-bootstrap';

/**
 * A submit button with optional loading spinner
 * @param {Object} props
 * @param {boolean} props.isLoading - Whether the button is in a loading state
 * @param {string} props.label - Button label text
 * @param {string} [props.variant] - Bootstrap variant
 * @param {string} [props.className] - Additional class names
 * @param {function} [props.onClick] - Optional click handler
 */
export const SubmitButton = ({
  isLoading,
  label,
  variant = 'primary',
  className = '',
  onClick,
}) => {
  return (
    <Button
      type="submit"
      variant={variant}
      className={`w-100 py-2 ${className}`}
      disabled={isLoading}
      onClick={onClick}
    >
      {isLoading ? (
        <>
          <Spinner
            as="span"
            animation="border"
            size="sm"
            role="status"
            aria-hidden="true"
            className="me-2"
          />
          {label}...
        </>
      ) : (
        label
      )}
    </Button>
  );
};

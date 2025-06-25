
// src/components/forms/LoginForm.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import { Form } from 'react-bootstrap';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';

/**
 * Login form component (presentational)
 * @param {Object} props
 */
export const LoginForm = ({
  formData,
  onChange,
  onSubmit,
  errors,
  loading,
}) => {
  return (
    <Form onSubmit={onSubmit}>
      <FormInput
        name="email"
        type="email"
        label="Email"
        value={formData.email}
        onChange={onChange}
        error={errors.email}
        placeholder="your@email.com"
        disabled={loading}
      />

      <FormInput
        name="password"
        type="password"
        label="Password"
        value={formData.password}
        onChange={onChange}
        error={errors.password}
        placeholder="Enter your password"
        disabled={loading}
      />

      <div className="d-flex justify-content-end mb-3">
        <Link to="/auth/reset-password" className="small text-decoration-none">
          Forgot Password?
        </Link>
      </div>

      <SubmitButton
        isLoading={loading}
        label="Login"
        variant="primary"
      />
    </Form>
  );
};


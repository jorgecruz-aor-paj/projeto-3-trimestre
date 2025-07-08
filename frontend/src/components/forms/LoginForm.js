// src/components/forms/LoginForm.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import { Form } from 'react-bootstrap';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';
import { useTranslation } from 'react-i18next';
import { loginValidationRules } from '../../utils/validation';

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
  getErrorMessage,
  isSubmitted,
}) => {
  const { t } = useTranslation();

  // Only show errors if the form has been submitted
  const showError = error => {
    return isSubmitted ? getErrorMessage(error) : null;
  };

  return (
    <Form onSubmit={onSubmit}>
      <FormInput
        name="email"
        type="email"
        label={t('auth.email')}
        value={formData.email}
        onChange={onChange}
        error={showError(errors.email)}
        placeholder={t('auth.emailPlaceholder')}
        disabled={loading}
        maxLength={loginValidationRules.email.maxLength}
      />

      <FormInput
        name="password"
        type="password"
        label={t('auth.password')}
        value={formData.password}
        onChange={onChange}
        error={showError(errors.password)}
        placeholder={t('auth.passwordPlaceholder')}
        disabled={loading}
        minLength={loginValidationRules.password.minLength}
      />

      <div className="d-flex justify-content-end mb-3">
        <Link to="/reset-password" className="small text-decoration-none">
          {t('auth.forgotPassword')}
        </Link>
      </div>

      <SubmitButton
        isLoading={loading}
        label={t('auth.login')}
        variant="primary"
        className="w-100 py-2 mt-3 mb-4"
      />
    </Form>
  );
};

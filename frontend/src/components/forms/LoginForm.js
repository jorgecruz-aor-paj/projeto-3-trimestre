// src/components/forms/LoginForm.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import { Form } from 'react-bootstrap';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';
import { useTranslation } from 'react-i18next';

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
  const { t } = useTranslation();
  return (
    <Form onSubmit={onSubmit}>
      <FormInput
        name="email"
        type="email"
        label={t('auth.email')}
        value={formData.email}
        onChange={onChange}
        error={errors.email}
        placeholder={t('auth.emailPlaceholder')}
        disabled={loading}
      />

      <FormInput
        name="password"
        type="password"
        label={t('auth.password')}
        value={formData.password}
        onChange={onChange}
        error={errors.password}
        placeholder={t('auth.passwordPlaceholder')}
        disabled={loading}
      />

      <div className="d-flex justify-content-end mb-3">
        <Link to="/auth/reset-password" className="small text-decoration-none">
          {t('auth.forgotPassword')}
        </Link>
      </div>

      <SubmitButton
        isLoading={loading}
        label={t('auth.login')}
        variant="primary"
      />
    </Form>
  );
};

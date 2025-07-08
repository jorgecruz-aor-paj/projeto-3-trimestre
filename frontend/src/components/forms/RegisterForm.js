import React from 'react';
import { Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';
import { registerValidationRules } from '../../utils/validation';
import { PasswordStrengthMeter } from './PasswordStrengthMeter';

/**
 * RegisterForm component for user registration
 * @param {Object} props
 * @param {Object} props.formData - The form data (email, password, confirmPassword)
 * @param {Function} props.onChange - Input change handler
 * @param {Function} props.onSubmit - Form submit handler
 * @param {Object} props.errors - Validation errors
 * @param {boolean} props.loading - Loading state
 */
export const RegisterForm = ({
  formData,
  onChange,
  onSubmit,
  errors,
  loading,
  getErrorMessage,
}) => {
  const { t } = useTranslation();
  return (
    <Form onSubmit={onSubmit}>
      <FormInput
        label={t('auth.email')}
        type="email"
        name="email"
        value={formData.email}
        onChange={onChange}
        placeholder={t('auth.emailPlaceholder')}
        error={getErrorMessage(errors.email)}
        disabled={loading}
        maxLength={registerValidationRules.email.maxLength}
        autoFocus
      />
      <FormInput
        label={t('auth.password')}
        type="password"
        name="password"
        value={formData.password}
        onChange={onChange}
        placeholder={t('auth.passwordPlaceholder')}
        error={getErrorMessage(errors.password)}
        disabled={loading}
        minLength={registerValidationRules.password.minLength}
      />

      {/* Password Strength Indicator */}
      <PasswordStrengthMeter password={formData.password} />

      <FormInput
        label={t('auth.confirmPassword')}
        type="password"
        name="confirmPassword"
        value={formData.confirmPassword}
        onChange={onChange}
        placeholder={t('auth.confirmPasswordPlaceholder')}
        error={getErrorMessage(errors.confirmPassword)}
        disabled={loading}
        minLength={registerValidationRules.confirmPassword.minLength}
      />
      <SubmitButton
        isLoading={loading}
        label={t('auth.register')}
        loadingText={t('auth.registering')}
        className="w-100 py-2 mt-3 mb-4"
      />
      <div className="text-center">
        <p className="mb-0">
          {t('auth.alreadyHaveAccount')}{' '}
          <Link to="/login" className="text-decoration-none fw-semibold">
            {t('auth.login')}
          </Link>
        </p>
      </div>
    </Form>
  );
};

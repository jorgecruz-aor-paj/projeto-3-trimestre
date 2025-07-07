import React from 'react';
import { Form } from 'react-bootstrap';
import { FormInput } from 'components/forms/FormInput';
import { SubmitButton } from 'components/forms/SubmitButton';
import { useTranslation } from 'react-i18next';
import { setNewPasswordValidationRules } from 'utils/validation';

/**
 * Set New Password form component (presentational)
 * @param {Object} props
 */
export const ResetPasswordForm = ({
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
        name="password"
        type="password"
        label={t('auth.password')}
        value={formData.password}
        onChange={onChange}
        error={getErrorMessage(errors.password)}
        placeholder={t('auth.passwordPlaceholder')}
        disabled={loading}
        minLength={setNewPasswordValidationRules.password.minLength}
        maxLength={setNewPasswordValidationRules.password.maxLength}
        autoFocus
      />

      <FormInput
        name="confirmPassword"
        type="password"
        label={t('auth.confirmPassword')}
        value={formData.confirmPassword}
        onChange={onChange}
        error={getErrorMessage(errors.confirmPassword)}
        placeholder={t('auth.confirmPasswordPlaceholder')}
        disabled={loading}
        minLength={setNewPasswordValidationRules.confirmPassword.minLength}
        maxLength={setNewPasswordValidationRules.confirmPassword.maxLength}
      />

      <SubmitButton
        isLoading={loading}
        label={t('auth.setNewPassword')}
        className="w-100 py-2 mt-3 mb-4"
      />
    </Form>
  );
};

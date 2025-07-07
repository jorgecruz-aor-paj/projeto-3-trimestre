import React from 'react';
import { Form } from 'react-bootstrap';
import { FormInput } from 'components/forms/FormInput';
import { SubmitButton } from 'components/forms/SubmitButton';
import { useTranslation } from 'react-i18next';
import { resetPasswordRequestValidationRules } from 'utils/validation';

/**
 * Reset Password Request form component (presentational)
 * @param {Object} props
 */
export const ResetPasswordRequestForm = ({
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
        name="email"
        type="email"
        label={t('auth.email')}
        value={formData.email}
        onChange={onChange}
        error={getErrorMessage(errors.email)}
        placeholder={t('auth.emailPlaceholder')}
        disabled={loading}
        maxLength={resetPasswordRequestValidationRules.email.maxLength}
        autoFocus
      />

      <SubmitButton
        isLoading={loading}
        label={t('auth.sendResetLink')}
        className="w-100 py-2 mt-3 mb-4"
      />
    </Form>
  );
};

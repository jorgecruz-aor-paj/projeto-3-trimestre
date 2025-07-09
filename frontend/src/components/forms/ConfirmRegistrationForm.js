import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';
import { FormDropdownInput } from './FormDropdownInput';
import { useTranslation } from 'react-i18next';
import { useErrorMessage } from '../../hooks/useErrorMessage';
import { profileValidationRules } from '../../utils/validation';

/**
 * Confirm Registration form component (presentational)
 * @param {Object} props
 */
export const ConfirmRegistrationForm = ({
  formData,
  validationErrors,
  workplaces,
  workplacesLoading,
  loading,
  submitting,
  handleInputChange,
  handleSubmit,
}) => {
  const { t } = useTranslation();
  const { getErrorMessage } = useErrorMessage();

  const safeGetErrorMessage = error => {
    if (!error) return null;
    if (typeof error === 'string') {
      return t(error);
    }
    if (error.key && error.options) {
      return t(error.key, error.options);
    }
    return t(error);
  };

  return (
    <Form onSubmit={handleSubmit} encType="multipart/form-data">
      <FormInput
        name="firstName"
        label={`${t('user.firstName')} *`}
        value={formData.firstName}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.firstName)}
        disabled={submitting}
        minLength={profileValidationRules.firstName.minLength}
        maxLength={profileValidationRules.firstName.maxLength}
        autoFocus
        placeholder={t('user.firstNamePlaceholder')}
      />
      <FormInput
        name="lastName"
        label={`${t('user.lastName')} *`}
        value={formData.lastName}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.lastName)}
        disabled={submitting}
        minLength={profileValidationRules.lastName.minLength}
        maxLength={profileValidationRules.lastName.maxLength}
        placeholder={t('user.lastNamePlaceholder')}
      />
      <FormInput
        name="address"
        label={`${t('user.address')} *`}
        value={formData.address}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.address)}
        disabled={submitting}
        maxLength={profileValidationRules.address.maxLength}
        placeholder={t('user.addressPlaceholder')}
      />
      <FormInput
        name="phoneNumber"
        label={`${t('user.phoneNumber')} *`}
        value={formData.phoneNumber}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.phoneNumber)}
        disabled={submitting}
        placeholder={t('user.phoneNumberPlaceholder')}
      />
      <FormInput
        name="birthdate"
        type="date"
        label={`${t('user.birthdate')} *`}
        value={formData.birthdate}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.birthdate)}
        disabled={submitting}
      />
      <FormDropdownInput
        name="workplaceId"
        label={`${t('user.workplace')} *`}
        value={formData.workplaceId}
        onChange={handleInputChange}
        options={workplaces?.map(w => ({ value: w.id, label: w.name })) || []}
        error={safeGetErrorMessage(validationErrors.workplaceId)}
        disabled={submitting || workplacesLoading}
        placeholder={
          workplacesLoading ? t('common.loading') : t('user.selectWorkplace')
        }
      />
      <FormInput
        name="biography"
        type="textarea"
        label={t('user.biography')}
        value={formData.biography}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.biography)}
        disabled={submitting}
        maxLength={profileValidationRules.biography.maxLength}
        rows={3}
        placeholder={t('user.biographyPlaceholder')}
      />
      <SubmitButton
        isLoading={submitting}
        label={t('auth.confirmRegistration')}
        className="w-100 py-2 mt-3 mb-4"
        disabled={workplacesLoading}
      />
    </Form>
  );
};

ConfirmRegistrationForm.propTypes = {
  formData: PropTypes.shape({
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    address: PropTypes.string,
    phoneNumber: PropTypes.string,
    birthdate: PropTypes.string,
    workplaceId: PropTypes.string,
    biography: PropTypes.string,
  }).isRequired,
  validationErrors: PropTypes.shape({
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    address: PropTypes.string,
    phoneNumber: PropTypes.string,
    birthdate: PropTypes.string,
    workplaceId: PropTypes.string,
    biography: PropTypes.string,
  }),
  workplaces: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      name: PropTypes.string.isRequired,
    })
  ),
  workplacesLoading: PropTypes.bool,
  loading: PropTypes.bool,
  submitting: PropTypes.bool,
  handleInputChange: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
};

ConfirmRegistrationForm.defaultProps = {
  validationErrors: {},
  workplaces: [],
  workplacesLoading: false,
  loading: false,
  submitting: false,
};

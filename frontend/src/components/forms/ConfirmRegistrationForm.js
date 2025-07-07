import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';
import { FormDropdownInput } from './FormDropdownInput';
import { useTranslation } from 'react-i18next';
import { useErrorMessage } from '../../hooks/useErrorMessage';
import { confirmRegistrationValidationRules } from '../../utils/validation';

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
    return getErrorMessage(error);
  };

  return (
    <Form onSubmit={handleSubmit} encType="multipart/form-data">
      <FormInput
        name="firstName"
        label={t('user.firstName')}
        value={formData.firstName}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.firstName)}
        disabled={submitting}
        minLength={confirmRegistrationValidationRules.firstName.minLength}
        maxLength={confirmRegistrationValidationRules.firstName.maxLength}
        autoFocus
      />
      <FormInput
        name="lastName"
        label={t('user.lastName')}
        value={formData.lastName}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.lastName)}
        disabled={submitting}
        minLength={confirmRegistrationValidationRules.lastName.minLength}
        maxLength={confirmRegistrationValidationRules.lastName.maxLength}
      />
      <FormInput
        name="address"
        label={t('user.address')}
        value={formData.address}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.address)}
        disabled={submitting}
        minLength={confirmRegistrationValidationRules.address.minLength}
        maxLength={confirmRegistrationValidationRules.address.maxLength}
      />
      <FormInput
        name="phoneNumber"
        label={t('user.phoneNumber')}
        value={formData.phoneNumber}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.phoneNumber)}
        disabled={submitting}
      />
      <FormInput
        name="birthdate"
        type="date"
        label={t('user.birthdate')}
        value={formData.birthdate}
        onChange={handleInputChange}
        error={safeGetErrorMessage(validationErrors.birthdate)}
        disabled={submitting}
      />
      <FormDropdownInput
        name="workplaceId"
        label={t('user.workplace')}
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
        maxLength={confirmRegistrationValidationRules.biography.maxLength}
        rows={3}
      />
      <FormInput
        name="photograph"
        type="file"
        label={t('user.photograph')}
        onChange={handleInputChange}
        disabled={submitting}
        accept="image/*"
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
    photograph: PropTypes.oneOfType([PropTypes.object, PropTypes.string]),
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

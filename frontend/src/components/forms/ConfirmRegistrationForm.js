import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { FormInput } from './FormInput';
import { SubmitButton } from './SubmitButton';
import { FormDropdownInput } from './FormDropdownInput';
import { useTranslation } from 'react-i18next';

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

  return (
    <Form onSubmit={handleSubmit} encType="multipart/form-data">
      <FormInput
        name="firstName"
        label={t('user.firstName')}
        value={formData.firstName}
        onChange={handleInputChange}
        error={validationErrors.firstName}
        disabled={submitting}
        autoFocus
      />
      <FormInput
        name="lastName"
        label={t('user.lastName')}
        value={formData.lastName}
        onChange={handleInputChange}
        error={validationErrors.lastName}
        disabled={submitting}
      />
      <FormInput
        name="address"
        label={t('user.address')}
        value={formData.address}
        onChange={handleInputChange}
        error={validationErrors.address}
        disabled={submitting}
      />
      <FormInput
        name="phoneNumber"
        label={t('user.phoneNumber')}
        value={formData.phoneNumber}
        onChange={handleInputChange}
        error={validationErrors.phoneNumber}
        disabled={submitting}
      />
      <FormInput
        name="birthdate"
        type="date"
        label={t('user.birthdate')}
        value={formData.birthdate}
        onChange={handleInputChange}
        error={validationErrors.birthdate}
        disabled={submitting}
      />
      <FormDropdownInput
        name="workplaceId"
        label={t('user.workplace')}
        value={formData.workplaceId}
        onChange={handleInputChange}
        options={workplaces?.map(w => ({ value: w.id, label: w.name })) || []}
        error={validationErrors.workplaceId}
        disabled={submitting || workplacesLoading}
        placeholder={
          workplacesLoading ? t('common.loading') : t('user.selectWorkplace')
        }
      />
      <FormInput
        name="biography"
        as="textarea"
        label={t('user.biography')}
        value={formData.biography}
        onChange={handleInputChange}
        error={validationErrors.biography}
        disabled={submitting}
        rows={3}
      />
      <Form.Group className="mb-3">
        <Form.Label>{t('user.photograph')}</Form.Label>
        <Form.Control
          type="file"
          name="photograph"
          accept="image/*"
          onChange={handleInputChange}
          disabled={submitting}
        />
      </Form.Group>
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

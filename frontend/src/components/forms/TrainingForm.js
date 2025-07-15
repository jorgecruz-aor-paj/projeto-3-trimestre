import React, { useState, useEffect } from 'react';
import { Form, Button, Row, Col, Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import { FormInput } from './FormInput';
import { FormDropdownInput } from './FormDropdownInput';
import { useFormValidation } from 'utils/validation';
import { trainingValidationRules } from 'utils/validation';
import { useErrorMessage } from 'hooks/useErrorMessage';

/**
 * Training form component for creating and editing trainings
 * @param {Object} props
 * @param {Object} props.training - Training data for editing (optional)
 * @param {Function} props.onSubmit - Form submission handler
 * @param {Function} props.onCancel - Cancel handler
 * @param {Array} props.trainingAreas - Available training areas
 * @param {boolean} props.loading - Loading state
 * @param {boolean} props.isEditing - Whether this is an edit form
 * @returns {React.Component} Training form
 */
export const TrainingForm = ({
  training = null,
  onSubmit,
  onCancel,
  trainingAreas = [],
  loading = false,
  isEditing = false,
  readOnly = false,
}) => {
  const { t } = useTranslation();
  const { getErrorMessage } = useErrorMessage();

  const { formData, errors, handleChange, validateAll, reset, setFormData } =
    useFormValidation(
      {
        title: '',
        description: '',
        duration: '',
        language: '',
        link: '',
        trainingArea: null,
      },
      trainingValidationRules
    );

  // Populate form when editing
  useEffect(() => {
    if (training) {
      setFormData({
        id: training.id,
        title: training.title || '',
        description: training.description || '',
        duration: training.duration || '',
        language: training.language || '',
        link: training.link || '',
        trainingArea: training.trainingArea || null,
      });
    }
  }, [training, setFormData]);

  const handleInputChange = e => {
    const { name, value } = e.target;
    handleChange(name, value);
  };

  const handleSubmit = e => {
    e.preventDefault();

    if (!validateAll()) {
      return;
    }
    const submitData = {
      ...formData,
      duration: parseInt(formData.duration, 10),
      // Add trainingAreaId for API compatibility, but keep the original trainingArea object too
      trainingAreaId: formData.trainingArea?.id,
    };

    onSubmit(submitData);
  };

  const commonLanguages = [
    'ENGLISH',
    'PORTUGUESE',
    'SPANISH',
    'FRENCH',
    'GERMAN',
    'ITALIAN',
    'DUTCH',
  ];

  return (
    <Form onSubmit={handleSubmit}>
      <Row>
        <Col md={6}>
          <FormInput
            name="title"
            label={`${t('trainings.fields.title')} *`}
            type="text"
            value={formData.title}
            onChange={handleInputChange}
            placeholder={t('trainings.form.titlePlaceholder')}
            error={getErrorMessage(errors.title)}
            disabled={readOnly}
          />
        </Col>

        <Col md={6}>
          <FormDropdownInput
            name="trainingArea"
            label={`${t('trainings.fields.area')} *`}
            value={formData.trainingArea?.id || ''}
            onChange={e => {
              const selectedArea = trainingAreas.find(
                area => area.id === Number(e.target.value)
              );
              handleChange('trainingArea', selectedArea);
            }}
            options={trainingAreas.map(area => ({
              value: area.id,
              label: area.name,
            }))}
            error={getErrorMessage(errors.trainingArea)}
            placeholder={t('trainings.form.selectArea')}
            disabled={readOnly}
          />
        </Col>
      </Row>

      <Row>
        <Col md={6}>
          <FormInput
            name="duration"
            label={`${t('trainings.fields.duration')} *`}
            type="number"
            value={formData.duration}
            onChange={handleInputChange}
            placeholder={t('trainings.form.durationPlaceholder')}
            error={getErrorMessage(errors.duration)}
            disabled={readOnly}
          />
        </Col>

        <Col md={6}>
          <FormDropdownInput
            name="language"
            label={`${t('trainings.fields.language')} *`}
            value={formData.language}
            onChange={handleInputChange}
            options={commonLanguages.map(lang => ({
              value: lang,
              label: t(`languages.${lang}`),
            }))}
            error={getErrorMessage(errors.language)}
            placeholder={t('trainings.form.selectLanguage')}
            disabled={readOnly}
          />
        </Col>
      </Row>

      <FormInput
        name="description"
        label={`${t('trainings.fields.description')} *`}
        type="textarea"
        value={formData.description}
        onChange={handleInputChange}
        placeholder={t('trainings.form.descriptionPlaceholder')}
        error={getErrorMessage(errors.description)}
        disabled={readOnly}
      />
      <Form.Text className="text-muted d-block mb-3">
        {t('trainings.form.descriptionHint')} ({formData.description.length}
        /2000)
      </Form.Text>

      <FormInput
        name="link"
        label={t('trainings.fields.link')}
        type="url"
        value={formData.link}
        onChange={handleInputChange}
        placeholder={t('trainings.form.linkPlaceholder')}
        error={getErrorMessage(errors.link)}
        disabled={readOnly}
      />
      <Form.Text className="text-muted d-block mb-4">
        {t('trainings.form.linkHint')}
      </Form.Text>

      <div className="d-flex justify-content-end gap-2">
        <Button variant="secondary" onClick={onCancel} disabled={loading}>
          {t('common.cancel')}
        </Button>
        {!readOnly && (
          <Button variant="primary" type="submit" disabled={loading}>
            {loading && (
              <Spinner animation="border" size="sm" className="me-2" />
            )}
            {isEditing ? t('common.update') : t('common.create')}
          </Button>
        )}
      </div>
    </Form>
  );
};

TrainingForm.propTypes = {
  training: PropTypes.object,
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  trainingAreas: PropTypes.array.isRequired,
  loading: PropTypes.bool,
  isEditing: PropTypes.bool,
};

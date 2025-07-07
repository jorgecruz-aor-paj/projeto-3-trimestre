import React, { useState, useEffect } from 'react';
import { Form, Button, Row, Col, Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import { FormInput } from './FormInput';
import { FormDropdownInput } from './FormDropdownInput';

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
}) => {
  const { t } = useTranslation();

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    duration: '',
    language: '',
    link: '',
    trainingArea: null,
  });

  const [errors, setErrors] = useState({});

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
  }, [training]);

  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.title.trim()) {
      newErrors.title = t('validation.required');
    } else if (formData.title.length > 100) {
      newErrors.title = t('trainings.validation.titleTooLong');
    }

    if (!formData.description.trim()) {
      newErrors.description = t('validation.required');
    } else if (formData.description.length < 10) {
      newErrors.description = t('trainings.validation.descriptionTooShort');
    } else if (formData.description.length > 1000) {
      newErrors.description = t('trainings.validation.descriptionTooLong');
    }

    if (!formData.duration) {
      newErrors.duration = t('validation.required');
    } else if (formData.duration <= 0) {
      newErrors.duration = t('trainings.validation.durationInvalid');
    }

    if (!formData.language.trim()) {
      newErrors.language = t('validation.required');
    }

    if (!formData.trainingArea) {
      newErrors.trainingArea = t('validation.required');
    }

    // Validate URL if provided
    if (formData.link && formData.link.trim()) {
      try {
        new URL(formData.link);
      } catch {
        newErrors.link = t('trainings.validation.invalidUrl');
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = e => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    const submitData = {
      ...formData,
      duration: parseInt(formData.duration, 10),
    };

    onSubmit(submitData);
  };

  const commonLanguages = [
    'English',
    'Portuguese',
    'Spanish',
    'French',
    'German',
    'Italian',
    'Dutch',
    'Other',
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
            error={errors.title}
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
              setFormData(prev => ({ ...prev, trainingArea: selectedArea }));
            }}
            options={trainingAreas.map(area => ({
              value: area.id,
              label: area.name,
            }))}
            error={errors.trainingArea}
            placeholder={t('trainings.form.selectArea')}
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
            error={errors.duration}
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
              label: lang,
            }))}
            error={errors.language}
            placeholder={t('trainings.form.selectLanguage')}
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
        error={errors.description}
      />
      <Form.Text className="text-muted d-block mb-3">
        {t('trainings.form.descriptionHint')} ({formData.description.length}
        /1000)
      </Form.Text>

      <FormInput
        name="link"
        label={t('trainings.fields.link')}
        type="url"
        value={formData.link}
        onChange={handleInputChange}
        placeholder={t('trainings.form.linkPlaceholder')}
        error={errors.link}
      />
      <Form.Text className="text-muted d-block mb-4">
        {t('trainings.form.linkHint')}
      </Form.Text>

      <div className="d-flex justify-content-end gap-2">
        <Button variant="secondary" onClick={onCancel} disabled={loading}>
          {t('common.cancel')}
        </Button>
        <Button variant="primary" type="submit" disabled={loading}>
          {loading && <Spinner animation="border" size="sm" className="me-2" />}
          {isEditing ? t('common.update') : t('common.create')}
        </Button>
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

import React, { useState, useEffect } from 'react';
import { Form, Button, Row, Col, Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';

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
          <Form.Group className="mb-3">
            <Form.Label>
              {t('trainings.fields.title')}{' '}
              <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              type="text"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              placeholder={t('trainings.form.titlePlaceholder')}
              isInvalid={!!errors.title}
            />
            <Form.Control.Feedback type="invalid">
              {errors.title}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>

        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>
              {t('trainings.fields.area')}{' '}
              <span className="text-danger">*</span>
            </Form.Label>
            <Form.Select
              name="trainingArea"
              value={formData.trainingArea?.id || ''}
              onChange={e => {
                const selectedArea = trainingAreas.find(
                  area => area.id === Number(e.target.value)
                );
                setFormData(prev => ({ ...prev, trainingArea: selectedArea }));
              }}
              isInvalid={!!errors.trainingArea}
            >
              <option value="">{t('trainings.form.selectArea')}</option>
              {trainingAreas.map(area => (
                <option key={area.id} value={area.id}>
                  {area.name}
                </option>
              ))}
            </Form.Select>
            <Form.Control.Feedback type="invalid">
              {errors.trainingArea}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>
      </Row>

      <Row>
        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>
              {t('trainings.fields.duration')}{' '}
              <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              type="number"
              name="duration"
              value={formData.duration}
              onChange={handleInputChange}
              placeholder={t('trainings.form.durationPlaceholder')}
              min="1"
              max="1000"
              isInvalid={!!errors.duration}
            />
            <Form.Text className="text-muted">
              {t('trainings.form.durationHint')}
            </Form.Text>
            <Form.Control.Feedback type="invalid">
              {errors.duration}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>

        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>
              {t('trainings.fields.language')}{' '}
              <span className="text-danger">*</span>
            </Form.Label>
            <Form.Select
              name="language"
              value={formData.language}
              onChange={handleInputChange}
              isInvalid={!!errors.language}
            >
              <option value="">{t('trainings.form.selectLanguage')}</option>
              {commonLanguages.map(lang => (
                <option key={lang} value={lang}>
                  {lang}
                </option>
              ))}
            </Form.Select>
            <Form.Control.Feedback type="invalid">
              {errors.language}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>
      </Row>

      <Form.Group className="mb-3">
        <Form.Label>
          {t('trainings.fields.description')}{' '}
          <span className="text-danger">*</span>
        </Form.Label>
        <Form.Control
          as="textarea"
          rows={4}
          name="description"
          value={formData.description}
          onChange={handleInputChange}
          placeholder={t('trainings.form.descriptionPlaceholder')}
          isInvalid={!!errors.description}
        />
        <Form.Text className="text-muted">
          {t('trainings.form.descriptionHint')} ({formData.description.length}
          /1000)
        </Form.Text>
        <Form.Control.Feedback type="invalid">
          {errors.description}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-4">
        <Form.Label>{t('trainings.fields.link')}</Form.Label>
        <Form.Control
          type="url"
          name="link"
          value={formData.link}
          onChange={handleInputChange}
          placeholder={t('trainings.form.linkPlaceholder')}
          isInvalid={!!errors.link}
        />
        <Form.Text className="text-muted">
          {t('trainings.form.linkHint')}
        </Form.Text>
        <Form.Control.Feedback type="invalid">
          {errors.link}
        </Form.Control.Feedback>
      </Form.Group>

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

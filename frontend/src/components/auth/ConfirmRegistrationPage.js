import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Container, Row, Col, Card, Alert, Spinner } from 'react-bootstrap';
import { authService } from 'services/authService';
import { ConfirmRegistrationForm } from 'components/forms/ConfirmRegistrationForm';
import { useAlert } from 'contexts/AlertContext';
import { useWorkplaces } from 'hooks/useWorkplaces';
import {
  useFormValidation,
  profileValidationRules,
} from '../../utils/validation';
import { useErrorMessage } from '../../hooks/useErrorMessage';

export const ConfirmRegistrationPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { showAlert } = useAlert();
  const [searchParams] = useSearchParams();
  const confirmationToken = searchParams.get('confirmationToken');
  const { getErrorMessage } = useErrorMessage();

  const { workplaces, workplacesLoading } = useWorkplaces();
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const initialFormData = {
    firstName: '',
    lastName: '',
    address: '',
    phoneNumber: '',
    birthdate: '',
    workplaceId: '',
    biography: '',
    photograph: null,
  };

  const {
    formData,
    errors = {},
    handleChange,
    validateAll,
    setFormData,
  } = useFormValidation(initialFormData, profileValidationRules);

  useEffect(() => {
    // Reset loading state once workplaces are loaded
    if (!workplacesLoading) {
      setLoading(false);
    }
  }, [workplacesLoading]);

  const handleInputChange = e => {
    const { name, value, type, files } = e.target;
    if (type === 'file') {
      // Handle file input separately since it's not a simple value
      const file = files[0];
      setFormData(prev => ({
        ...prev,
        [name]: file,
      }));
      // Validate the file if needed
      handleChange(name, file);
    } else {
      handleChange(name, value);
    }
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    // Validate all fields
    if (!validateAll()) {
      setError(t('validation.fixErrors'));
      return;
    }

    setSubmitting(true);
    try {
      const submitData = new FormData();

      // Add all non-empty values to form data
      Object.entries(formData).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          // Special handling for workplace ID to ensure it's sent as a number
          if (key === 'workplaceId') {
            submitData.append(key, Number(value));
          } else {
            submitData.append(key, value);
          }
        }
      });

      await authService.confirmUser(confirmationToken, submitData);
      setSuccess(t('auth.confirmSuccess'));
      showAlert({
        title: t('auth.confirmTitle'),
        message: t('auth.confirmSuccess'),
        confirmLabel: t('auth.goToLogin'),
        onConfirm: () => navigate('/login'),
      });
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || err.message || t('auth.confirmFailed');
      setError(errorMessage);
      console.error('Registration confirmation error:', err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Container fluid="md" className="py-5 login-page">
      <Row className="justify-content-center">
        <Col xs={12} sm={10} md={8} lg={6} xl={5}>
          <Card className="shadow-sm border-0">
            <Card.Body className="p-4 p-md-5">
              <h2 className="fw-bold mb-3 text-center">
                {t('auth.confirmTitle')}
              </h2>
              <p className="text-muted text-center mb-4">
                {t('auth.confirmSubtitle')}
              </p>
              {error && <Alert variant="danger">{error}</Alert>}
              <ConfirmRegistrationForm
                formData={formData}
                validationErrors={errors}
                workplacesLoading={workplacesLoading}
                workplaces={workplaces}
                loading={loading}
                submitting={submitting}
                handleInputChange={handleInputChange}
                handleSubmit={handleSubmit}
              />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

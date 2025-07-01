import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Container, Row, Col, Card, Alert, Spinner } from 'react-bootstrap';
import { authService } from 'services/authService';
import { ConfirmRegistrationForm } from 'components/forms/ConfirmRegistrationForm';
import { useAlert } from 'contexts/AlertContext';
import { useWorkplaces } from 'hooks/useWorkplaces';

export const ConfirmRegistrationPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { showAlert } = useAlert();
  const [searchParams] = useSearchParams();
  const confirmationToken = searchParams.get('confirmationToken');

  const { workplaces, workplacesLoading } = useWorkplaces();
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    address: '',
    phoneNumber: '',
    birthdate: '',
    workplaceId: '',
    biography: '',
    photograph: null,
  });

  const handleInputChange = e => {
    const { name, value, type, files } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'file' ? files[0] : value,
    }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.firstName) errors.firstName = t('validation.fieldRequired');
    if (!formData.lastName) errors.lastName = t('validation.fieldRequired');
    if (!formData.address) errors.address = t('validation.fieldRequired');
    if (!formData.phoneNumber)
      errors.phoneNumber = t('validation.fieldRequired');
    if (!formData.birthdate) errors.birthdate = t('validation.fieldRequired');
    if (!formData.workplaceId)
      errors.workplaceId = t('validation.fieldRequired');
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    if (!validateForm()) return;
    setSubmitting(true);
    try {
      const submitData = new FormData();
      Object.entries(formData).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '') {
          submitData.append(key, value);
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
      setError(err.message || t('auth.confirmFailed'));
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
                validationErrors={validationErrors}
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

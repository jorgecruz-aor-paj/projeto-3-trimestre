import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Alert, Card, Container, Row, Col } from 'react-bootstrap';
import { authService } from 'services/authService';
import { ResetPasswordRequestForm } from 'components/forms/ResetPasswordRequestForm';
import {
  useFormValidation,
  resetPasswordRequestValidationRules,
} from 'utils/validation';
import { useErrorMessage } from 'hooks/useErrorMessage';

export const ResetPasswordRequestPage = () => {
  const { t } = useTranslation();
  const { getErrorMessage } = useErrorMessage();

  const {
    formData,
    errors = {},
    handleChange,
    validateAll,
  } = useFormValidation({ email: '' }, resetPasswordRequestValidationRules);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleInputChange = e => {
    const { name, value } = e.target;
    handleChange(name, value);
  };

  const safeGetErrorMessage = error => {
    if (!error) return null;
    return getErrorMessage(error);
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    if (!validateAll()) return;
    setLoading(true);
    try {
      await authService.requestPasswordReset(formData.email);
      setSuccess(t('auth.resetPasswordEmailSent'));
    } catch (err) {
      setError(err.message || t('auth.resetPasswordFailed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container fluid="md" className="py-5 login-page">
      <Row className="justify-content-center">
        <Col xs={12} sm={10} md={8} lg={6} xl={5}>
          <Card className="shadow-sm border-0">
            <Card.Body className="p-4 p-md-5">
              <h2 className="fw-bold mb-3 text-center">
                {t('auth.resetPasswordTitle')}
              </h2>
              <p className="text-muted text-center mb-4">
                {t('auth.resetPasswordSubtitle')}
              </p>
              {success && <Alert variant="success">{success}</Alert>}
              {error && <Alert variant="danger">{error}</Alert>}
              <ResetPasswordRequestForm
                formData={formData}
                onChange={handleInputChange}
                onSubmit={handleSubmit}
                errors={errors}
                loading={loading}
                getErrorMessage={safeGetErrorMessage}
              />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Form, Alert, Card, Container, Row, Col } from 'react-bootstrap';
import { FormInput } from 'components/forms/FormInput';
import { SubmitButton } from 'components/forms/SubmitButton';
import { authService } from 'services/authService';

export const ResetPasswordPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const resetPasswordToken = searchParams.get('resetPasswordToken');

  const [formData, setFormData] = useState({
    password: '',
    confirmPassword: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validate = () => {
    const errors = {};
    if (!formData.password) {
      errors.password = t('validation.passwordRequired');
    } else if (formData.password.length < 6) {
      errors.password = t('validation.passwordMinLength');
    }
    if (!formData.confirmPassword) {
      errors.confirmPassword = t('validation.confirmPasswordRequired');
    } else if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = t('validation.passwordsDontMatch');
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    if (!validate()) return;
    setLoading(true);
    try {
      await authService.resetPassword(resetPasswordToken, {
        email: searchParams.get('email'),
        password: formData.password,
      });
      setSuccess(t('auth.resetPasswordSuccess'));
      setTimeout(() => navigate('/login'), 2000);
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
                {t('auth.setNewPasswordTitle')}
              </h2>
              <p className="text-muted text-center mb-4">
                {t('auth.setNewPasswordSubtitle')}
              </p>
              {success && <Alert variant="success">{success}</Alert>}
              {error && <Alert variant="danger">{error}</Alert>}
              <Form onSubmit={handleSubmit}>
                <FormInput
                  label={t('auth.password')}
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleInputChange}
                  placeholder={t('auth.passwordPlaceholder')}
                  error={validationErrors.password}
                  disabled={loading}
                  autoFocus
                />
                <FormInput
                  label={t('auth.confirmPassword')}
                  type="password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  placeholder={t('auth.confirmPasswordPlaceholder')}
                  error={validationErrors.confirmPassword}
                  disabled={loading}
                />
                <SubmitButton
                  isLoading={loading}
                  label={t('auth.setNewPassword')}
                  className="w-100 py-2 mt-3 mb-4"
                />
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

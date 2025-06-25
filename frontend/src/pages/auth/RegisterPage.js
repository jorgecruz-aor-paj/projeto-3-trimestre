import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { useAuthStore } from 'stores/AuthStore';
import { RegisterForm } from 'components/forms/RegisterForm';
import { Container, Row, Col, Card, Alert } from 'react-bootstrap';

import 'styles/pages/auth/LoginPage.css';

/**
 * RegisterPage component that handles user registration
 *
 * This component provides a form for users to register a new account
 * and handles the registration process, including validation and error handling.
 *
 * @returns {React.Component} Register page component
 */
export const RegisterPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  // Form state
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
  });

  // UI state
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  // Handle input changes
  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: null,
      }));
    }
  };

  // Validate form inputs
  const validateForm = () => {
    const errors = {};
    if (!formData.email) {
      errors.email = t('validation.emailRequired');
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = t('validation.emailInvalid');
    }
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

  // Handle form submission
  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    if (!validateForm()) {
      return;
    }
    setLoading(true);
    try {
      // Call register API
      const response = await authService.register({
        email: formData.email,
        password: formData.password,
      });
      console.log('Registration successful:', response);
      navigate('/auth/login', { state: { registered: true } });
    } catch (err) {
      setError(err.message || t('auth.registerFailed'));
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
                {t('auth.registerTitle')}
              </h2>
              <p className="text-muted text-center mb-4">
                {t('auth.registerSubtitle')}
              </p>
              {error && <Alert variant="danger">{error}</Alert>}
              <RegisterForm
                formData={formData}
                onChange={handleInputChange}
                onSubmit={handleSubmit}
                errors={validationErrors}
                loading={loading}
              />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

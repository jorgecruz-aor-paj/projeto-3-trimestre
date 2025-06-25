import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { userService } from 'services/userService';
import { useAuthStore } from 'stores/AuthStore';
import { LoginForm } from 'components/forms/LoginForm';
import {
  Container,
  Row,
  Col,
  Card,
  Alert,
} from 'react-bootstrap';

import 'styles/pages/auth/LoginPage.css';

/**
 * LoginPage component that handles user authentication
 *
 * This component provides a form for users to log in to the application
 * and handles the authentication process, including validation and error handling.
 *
 * @returns {React.Component} Login page component
 */
export const LoginPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { setToken, setUserInfo } = useAuthStore();

  // Form state
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  // UI state
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  /**
   * Handles input changes in the form
   * @param {Event} e - The input change event
   */
  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));

    // Clear validation error when user types
    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: null,
      }));
    }
  };

  /**
   * Validates the form inputs
   * @returns {boolean} True if validation passes, false otherwise
   */
  const validateForm = () => {
    const errors = {};

    // Email validation
    if (!formData.email) {
      errors.email = t('validation.emailRequired');
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = t('validation.emailInvalid');
    }

    // Password validation
    if (!formData.password) {
      errors.password = t('validation.passwordRequired');
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  /**
   * Handles form submission
   * @param {Event} e - The form submission event
   */
  const handleSubmit = async e => {
    e.preventDefault();

    // Clear previous errors
    setError(null);

    // Validate form
    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      // Call login API
      const response = await authService.login(formData);

      // Store token in state and session storage
      setToken(response);

      // Get user info using the token
      try {
        const userResponse = await userService.getMe();
        setUserInfo(userResponse);
      } catch (userError) {
        console.error('Error fetching user data:', userError);
        // Continue with login even if user info fetch fails
      }

      // Redirect to dashboard or intended destination
      navigate('/dashboard');
    } catch (err) {
      console.error('Login error:', err);
      setError(err.response?.data?.message || t('auth.loginFailed'));
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
              <h2 className="fw-bold mb-3 text-center">{t('auth.loginTitle')}</h2>
              <p className="text-muted text-center mb-4">{t('auth.loginSubtitle')}</p>

              {error && <Alert variant="danger">{error}</Alert>}

              <LoginForm
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

import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { userService } from 'services/userService';
import useAuthStore from 'stores/AuthStore';
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Card,
  Alert,
  Spinner,
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
    <Container className="login-page py-5">
      <Row className="justify-content-center">
        <Col xs={12} sm={10} md={8} lg={6} xl={5}>
          <Card className="login-card shadow-sm border-0">
            <Card.Body className="p-4 p-md-5">
              <div className="text-center mb-4">
                <h2 className="fw-bold">{t('auth.loginTitle')}</h2>
                <p className="text-muted">{t('auth.loginSubtitle')}</p>
              </div>

              {error && (
                <Alert variant="danger" className="mb-4">
                  {error}
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>{t('auth.email')}</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder={t('auth.emailPlaceholder')}
                    isInvalid={!!validationErrors.email}
                    disabled={loading}
                    autoFocus
                  />
                  <Form.Control.Feedback type="invalid">
                    {validationErrors.email}
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-4">
                  <div className="d-flex justify-content-between">
                    <Form.Label>{t('auth.password')}</Form.Label>
                    <Link
                      to="/auth/reset-password"
                      className="small text-decoration-none"
                    >
                      {t('auth.forgotPassword')}
                    </Link>
                  </div>
                  <Form.Control
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleInputChange}
                    placeholder={t('auth.passwordPlaceholder')}
                    isInvalid={!!validationErrors.password}
                    disabled={loading}
                  />
                  <Form.Control.Feedback type="invalid">
                    {validationErrors.password}
                  </Form.Control.Feedback>
                </Form.Group>

                <Button
                  variant="primary"
                  type="submit"
                  className="w-100 py-2 mt-3 mb-4"
                  disabled={loading}
                >
                  {loading ? (
                    <>
                      <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                        className="me-2"
                      />
                      {t('auth.loggingIn')}
                    </>
                  ) : (
                    t('auth.login')
                  )}
                </Button>

                <div className="text-center">
                  <p className="mb-0">
                    {t('auth.noAccount')}{' '}
                    <Link
                      to="/auth/register"
                      className="text-decoration-none fw-semibold"
                    >
                      {t('auth.signUp')}
                    </Link>
                  </p>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

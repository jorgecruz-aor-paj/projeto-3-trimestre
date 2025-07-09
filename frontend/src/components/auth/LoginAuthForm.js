import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { userService } from 'services/userService';
import { useAuthStore } from 'stores/AuthStore';
import { LoginForm } from 'components/forms/LoginForm';
import { Alert } from 'react-bootstrap';
import { useFormValidation, loginValidationRules } from 'utils/validation';
import { useErrorMessage } from 'hooks/useErrorMessage';
import { useAlert } from 'contexts/AlertContext';

export const LoginAuthForm = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { setToken, setUserInfo } = useAuthStore();
  const { getErrorMessage } = useErrorMessage();
  const { showAlert } = useAlert();

  // Check for expired session message
  useEffect(() => {
    const sessionExpired = sessionStorage.getItem('sessionExpired');
    if (sessionExpired === 'true') {
      // Small delay to ensure the AlertContext is fully initialized
      setTimeout(() => {
        // Get the translated strings
        const title = t('auth.sessionExpired');
        const message = t('auth.sessionExpiredMessage');

        console.log('Showing session expired alert with translations:', {
          title,
          message,
        });

        // Show alert to user explaining why they were redirected
        showAlert({
          title,
          message,
          variant: 'warning',
        });

        // Clear the flag
        sessionStorage.removeItem('sessionExpired');
      }, 300); // Increased delay for better reliability
    }
  }, [showAlert, t]);

  const {
    formData,
    errors = {},
    handleChange,
    validateAll,
    setErrors,
  } = useFormValidation({ email: '', password: '' }, loginValidationRules);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleInputChange = e => {
    const { name, value } = e.target;
    // Only update the form data without validation
    handleChange(name, value);

    // If the form has been submitted once, we validate again to provide feedback
    if (isSubmitted) {
      validateAll();
    }
  };

  const safeGetErrorMessage = errorMessage => {
    // Only show errors if the form has been submitted
    if (!isSubmitted || !errorMessage) return null;
    return getErrorMessage(errorMessage);
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setIsSubmitted(true);

    // Validate all fields
    const isValid = validateAll();
    if (!isValid) return;

    setLoading(true);
    try {
      const response = await authService.login(formData);
      setToken(response);
      const userResponse = await userService.getMe();
      setUserInfo(userResponse);

      navigate('/dashboard');
    } catch (err) {
      console.error('Login error:', err);
      setError(t('auth.loginFailed'));
      showAlert({
        title: t('auth.loginErrorTitle'),
        message: t('auth.loginFailed'),
        variant: 'danger',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <h2 className="fw-bold mb-3 text-center">{t('auth.loginTitle')}</h2>
      <p className="text-muted text-center mb-4">{t('auth.loginSubtitle')}</p>
      {error && <Alert variant="danger">{error}</Alert>}
      <LoginForm
        formData={formData}
        onChange={handleInputChange}
        onSubmit={handleSubmit}
        errors={errors}
        loading={loading}
        getErrorMessage={safeGetErrorMessage}
        isSubmitted={isSubmitted}
      />
    </>
  );
};

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { userService } from 'services/userService';
import { useAuthStore } from 'stores/AuthStore';
import { LoginForm } from 'components/forms/LoginForm';
import { Alert } from 'react-bootstrap';
import {
  useFormValidation,
  loginValidationRules,
} from '../../utils/validation';
import { useErrorMessage } from '../../hooks/useErrorMessage';

export const LoginAuthForm = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { setToken, setUserInfo } = useAuthStore();
  const { getErrorMessage } = useErrorMessage();

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

  const safeGetErrorMessage = error => {
    // Only show errors if the form has been submitted
    if (!isSubmitted || !error) return null;
    return getErrorMessage(error);
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
      setError(err.message || t('auth.loginFailed'));
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

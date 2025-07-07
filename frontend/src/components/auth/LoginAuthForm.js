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
  } = useFormValidation({ email: '', password: '' }, loginValidationRules);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

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
    if (!validateAll()) return;
    setLoading(true);
    try {
      const response = await authService.login(formData);
      setToken(response);
      try {
        const userResponse = await userService.getMe();
        setUserInfo(userResponse);
      } catch {
        /* empty */
      }
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
      />
    </>
  );
};

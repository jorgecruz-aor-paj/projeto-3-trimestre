import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { userService } from 'services/userService';
import { useAuthStore } from 'stores/AuthStore';
import { LoginForm } from 'components/forms/LoginForm';
import { Alert } from 'react-bootstrap';

export const LoginAuthForm = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { setToken, setUserInfo } = useAuthStore();

  const [formData, setFormData] = useState({ email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.email) {
      errors.email = t('validation.emailRequired');
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = t('validation.emailInvalid');
    }
    if (!formData.password) {
      errors.password = t('validation.passwordRequired');
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    if (!validateForm()) return;
    setLoading(true);
    try {
      const response = await authService.login(formData);
      setToken(response);
      try {
        const userResponse = await userService.getMe();
        setUserInfo(userResponse);
      } catch {}
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
        errors={validationErrors}
        loading={loading}
      />
    </>
  );
};

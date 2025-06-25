import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { RegisterForm } from 'components/forms/RegisterForm';
import { Alert } from 'react-bootstrap';

export const RegisterAuthForm = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
  });
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
    if (!validateForm()) return;
    setLoading(true);
    try {
      await authService.register({
        email: formData.email,
        password: formData.password,
      });
      navigate('/auth/login', { state: { registered: true } });
    } catch (err) {
      setError(err.message || t('auth.registerFailed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <h2 className="fw-bold mb-3 text-center">{t('auth.registerTitle')}</h2>
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
    </>
  );
};

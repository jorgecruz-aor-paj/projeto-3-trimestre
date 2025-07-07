import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService } from 'services/authService';
import { RegisterForm } from 'components/forms/RegisterForm';
import { Alert } from 'react-bootstrap';
import { useAlert } from 'contexts/AlertContext';
import {
  useFormValidation,
  registerValidationRules,
} from '../../utils/validation';
import { useErrorMessage } from '../../hooks/useErrorMessage';

export const RegisterAuthForm = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { showAlert } = useAlert();
  const { getErrorMessage } = useErrorMessage();

  const {
    formData,
    errors = {},
    handleChange,
    validateAll,
  } = useFormValidation(
    {
      email: '',
      password: '',
      confirmPassword: '',
    },
    registerValidationRules
  );

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
      await authService.register({
        email: formData.email,
        password: formData.password,
      });
      showAlert({
        title: t('auth.registerTitle'),
        message: t('auth.registerSuccess'),
        confirmLabel: t('auth.goToHome'),
        onConfirm: () => navigate('/'),
      });
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
        errors={errors}
        loading={loading}
        getErrorMessage={safeGetErrorMessage}
      />
    </>
  );
};

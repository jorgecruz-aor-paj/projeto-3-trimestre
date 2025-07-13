import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { authService, twoFactorService } from 'services';
import { userService } from 'services/userService';
import { useAuthStore } from 'stores/AuthStore';
import { LoginForm } from 'components/forms/LoginForm';
import { TwoFactorVerificationModal } from 'components/auth/TwoFactorVerificationModal';
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
  } = useFormValidation({ email: '', password: '' }, loginValidationRules);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);

  // 2FA states
  const [showTwoFactorModal, setShowTwoFactorModal] = useState(false);
  const [twoFactorLoading, setTwoFactorLoading] = useState(false);
  const [twoFactorError, setTwoFactorError] = useState(null);

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

  /**
   * Complete the login process by fetching user info and redirecting
   */
  const completeLogin = async () => {
    try {
      const userResponse = await userService.getMe();
      setUserInfo(userResponse);
      navigate('/dashboard');
    } catch (err) {
      console.error('Failed to fetch user info:', err);
      setError(t('auth.loginFailed'));
      showAlert({
        title: t('auth.loginErrorTitle'),
        message: t('auth.userInfoFailed'),
        variant: 'danger',
      });
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle 2FA verification
   */
  const handleTwoFactorVerification = async code => {
    setTwoFactorLoading(true);
    setTwoFactorError(null);

    try {
      // Verify the 2FA code
      await twoFactorService.verifyLogin(code);

      // Hide modal and complete login
      setShowTwoFactorModal(false);
      await completeLogin();
    } catch (err) {
      console.error('2FA verification error:', err);
      const errorMessage = getErrorMessage(
        err.message || 'Invalid verification code'
      );
      setTwoFactorError(errorMessage);
    } finally {
      setTwoFactorLoading(false);
    }
  };

  /**
   * Handle 2FA modal close
   */
  const handleTwoFactorClose = () => {
    setShowTwoFactorModal(false);
    setTwoFactorError(null);
    setToken(null); // Clear the temporary token
    setLoading(false);
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
      // Step 1: Login with credentials
      const response = await authService.login(formData);

      // Step 2: Set temporary token to check 2FA status
      setToken(response);

      // Step 3: Check if user has 2FA enabled
      try {
        const twoFactorStatus = await twoFactorService.getStatus();

        if (twoFactorStatus.enabled) {
          // User has 2FA enabled - show verification modal
          setShowTwoFactorModal(true);
          setLoading(false);
          return;
        }
      } catch (twoFactorCheckError) {
        // If 2FA status check fails, assume 2FA is not enabled and continue
        console.warn(
          'Failed to check 2FA status, continuing login:',
          twoFactorCheckError
        );
      }

      // Step 4: Complete login (no 2FA required)
      await completeLogin();
    } catch (err) {
      console.error('Login error:', err);
      setError(t('auth.loginFailed'));
      showAlert({
        title: t('auth.loginErrorTitle'),
        message: t('auth.loginFailed'),
        variant: 'danger',
      });
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

      {/* Two-Factor Authentication Verification Modal */}
      <TwoFactorVerificationModal
        show={showTwoFactorModal}
        onHide={handleTwoFactorClose}
        onVerify={handleTwoFactorVerification}
        loading={twoFactorLoading}
        error={twoFactorError}
      />
    </>
  );
};

import React, { useState, useEffect } from 'react';
import { Alert, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { twoFactorService } from 'services';

/**
 * Two-Factor Authentication Recommendation Banner
 * Shows a subtle prompt encouraging users to enable 2FA
 */
export const TwoFactorRecommendationBanner = () => {
  const { t } = useTranslation();
  const [showBanner, setShowBanner] = useState(false);
  const [loading, setLoading] = useState(true);
  const [dismissed, setDismissed] = useState(false);

  const checkTwoFactorStatus = async () => {
    try {
      setLoading(true);

      // Check if user has already dismissed the banner
      const bannerDismissed = localStorage.getItem('2fa-banner-dismissed');
      if (bannerDismissed === 'true') {
        setDismissed(true);
        setLoading(false);
        return;
      }

      // Check 2FA status
      const response = await twoFactorService.getStatus();

      // Show banner only if 2FA is not enabled
      setShowBanner(!response.enabled);
    } catch (error) {
      // If API call fails, don't show the banner
      console.warn('Failed to check 2FA status for banner:', error);
      setShowBanner(false);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    checkTwoFactorStatus();
  }, []);

  const handleDismiss = () => {
    setShowBanner(false);
    setDismissed(true);
    // Remember user's preference
    localStorage.setItem('2fa-banner-dismissed', 'true');
  };

  const handleSetupClick = () => {
    // Redirect to profile security section
    window.location.href = '/profile#security';
  };

  // Don't render anything if loading, dismissed, or shouldn't show
  if (loading || dismissed || !showBanner) {
    return null;
  }

  return (
    <Alert
      variant="info"
      dismissible
      onClose={handleDismiss}
      className="mb-3 d-flex align-items-center"
    >
      <div className="flex-grow-1">
        <Alert.Heading as="h6" className="mb-1">
          <i className="bi bi-shield-plus me-2"></i>
          {t('security.2fa.recommendationTitle')}
        </Alert.Heading>
        <p className="mb-2 small">
          {t(
            'security.2fa.bannerMessage',
            'Secure your account with two-factor authentication for enhanced protection.'
          )}
        </p>
        <Button
          variant="outline-primary"
          size="sm"
          onClick={handleSetupClick}
          className="me-2"
        >
          {t('security.2fa.setupNow', 'Set Up Now')}
        </Button>
        <Button
          variant="link"
          size="sm"
          onClick={handleDismiss}
          className="p-0 text-muted"
        >
          {t('common.dismiss', 'Dismiss')}
        </Button>
      </div>
    </Alert>
  );
};

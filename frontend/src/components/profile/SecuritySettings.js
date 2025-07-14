import React, { useState } from 'react';
import { Container, Row, Col, Card, Button, Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { TwoFactorSecuritySettings } from 'components/security/TwoFactorSecuritySettings';
import { useAlert } from 'contexts/AlertContext';
import { authService } from 'services/authService';

/**
 * Security Settings Component for Profile Page
 * Contains all security-related settings including 2FA
 */
export const SecuritySettings = () => {
  const { t } = useTranslation();
  const [requestingPasswordReset, setRequestingPasswordReset] = useState(false);
  React.useState(false);
  const { showAlert } = useAlert();
  const user = require('stores/AuthStore').useAuthStore(state => state.user);

  const handlePasswordReset = async () => {
    try {
      setRequestingPasswordReset(true);
      await authService.requestPasswordReset(user.email);

      showAlert({
        title: t('profile.success'),
        message: t(
          'profile.passwordResetEmailSent',
          `A password reset link has been sent to ${user.email}`
        ),
        variant: 'success',
      });
    } catch (error) {
      console.error('Failed to request password reset:', error);
      showAlert({
        title: t('profile.error'),
        message: t(
          'profile.passwordResetError',
          'Failed to send password reset email. Please try again.'
        ),
        variant: 'danger',
      });
    } finally {
      setRequestingPasswordReset(false);
    }
  };

  return (
    <Container>
      <Row>
        <Col>
          <div className="page-header mb-4">
            <h3 className="mb-2">{t('security.title')}</h3>
            <p className="text-muted mb-0">{t('security.subtitle')}</p>
          </div>
        </Col>
      </Row>

      <Row>
        <Col lg={8}>
          <div className="mb-4">
            <TwoFactorSecuritySettings />
          </div>

          {/* Future security settings can be added here */}
          <Card className="mb-4">
            <Card.Header>
              <h5 className="mb-0">{t('security.passwordSettings.title')}</h5>
            </Card.Header>
            <Card.Body>
              <p className="text-muted mb-3">
                {t('security.passwordSettings.description')}
              </p>
              {/* Password change functionality */}
              <Button
                variant="outline-secondary"
                onClick={handlePasswordReset}
                disabled={requestingPasswordReset}
              >
                {requestingPasswordReset ? (
                  <>
                    <Spinner
                      as="span"
                      animation="border"
                      size="sm"
                      role="status"
                      aria-hidden="true"
                      className="me-1"
                    />
                    {t('profile.sending', 'Sending...')}
                  </>
                ) : (
                  t('profile.changePassword', 'Change Password')
                )}
              </Button>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={4}>
          <Card className="mb-4">
            <Card.Header>
              <h6 className="mb-0">{t('security.tips.title')}</h6>
            </Card.Header>
            <Card.Body>
              <ul className="list-unstyled mb-0">
                <li className="mb-2">
                  <small className="text-muted">
                    {t('security.tips.tip1')}
                  </small>
                </li>
                <li className="mb-2">
                  <small className="text-muted">
                    {t('security.tips.tip2')}
                  </small>
                </li>
                <li className="mb-0">
                  <small className="text-muted">
                    {t('security.tips.tip3')}
                  </small>
                </li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

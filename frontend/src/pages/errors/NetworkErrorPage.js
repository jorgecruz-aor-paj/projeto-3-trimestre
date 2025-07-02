import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/ErrorPages.css';

/**
 * Network Error page component
 * Displays when there's a connection or network issue
 * @returns {React.Component} Network error page
 */
export const NetworkErrorPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleRetry = () => {
    window.location.reload();
  };

  const handleGoHome = () => {
    navigate('/');
  };

  const handleGoOffline = () => {
    // Navigate to offline mode or cached content
    navigate('/offline');
  };

  return (
    <div className="error-page network-error-page">
      <Container>
        <Row className="min-vh-100 align-items-center justify-content-center">
          <Col lg={6} className="text-center">
            <div className="error-content">
              <div className="error-icon mb-4">
                <i className="bi bi-wifi-off error-network-icon"></i>
              </div>

              <div className="error-illustration mb-4">
                <i
                  className="bi bi-wifi-off text-warning"
                  style={{ fontSize: '10rem' }}
                ></i>
              </div>

              <h1 className="error-title mb-3">
                {t('errors.networkPage.title', 'Connection Problem')}
              </h1>

              <p className="error-description mb-4">
                {t(
                  'errors.networkPage.description',
                  'Unable to connect to our servers. Please check your internet connection and try again.'
                )}
              </p>

              <div className="error-actions d-flex flex-wrap gap-3 justify-content-center">
                <Button variant="primary" size="lg" onClick={handleRetry}>
                  <i className="bi bi-arrow-repeat me-2"></i>
                  {t('errors.networkPage.retry', 'Retry Connection')}
                </Button>

                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={handleGoHome}
                >
                  <i className="bi bi-house-door me-2"></i>
                  {t('errors.networkPage.goHome', 'Go to Homepage')}
                </Button>

                <Button
                  variant="outline-secondary"
                  size="lg"
                  onClick={handleGoOffline}
                >
                  <i className="bi bi-cloud-slash me-2"></i>
                  {t('errors.networkPage.goOffline', 'Continue Offline')}
                </Button>
              </div>

              <div className="error-suggestions mt-5">
                <h5 className="mb-3">
                  {t(
                    'errors.networkPage.suggestions.title',
                    'Troubleshooting steps:'
                  )}
                </h5>
                <ul className="list-unstyled text-start">
                  <li className="mb-2">
                    <i className="bi bi-wifi text-primary me-2"></i>
                    {t(
                      'errors.networkPage.suggestions.checkConnection',
                      'Check your internet connection'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-router text-primary me-2"></i>
                    {t(
                      'errors.networkPage.suggestions.restartRouter',
                      'Restart your router or modem'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-shield-check text-primary me-2"></i>
                    {t(
                      'errors.networkPage.suggestions.checkFirewall',
                      'Check firewall or proxy settings'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-clock text-primary me-2"></i>
                    {t(
                      'errors.networkPage.suggestions.waitAndRetry',
                      'Wait a moment and try again'
                    )}
                  </li>
                </ul>
              </div>

              <div className="connection-status mt-4">
                <div className="d-flex align-items-center justify-content-center">
                  <div className="connection-indicator offline me-2"></div>
                  <small className="text-muted">
                    {t(
                      'errors.networkPage.status',
                      'Connection Status: Offline'
                    )}
                  </small>
                </div>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

import React, { useState } from 'react';
import { Container, Row, Col, Button, Card } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/ErrorPages.css';

/**
 * Offline page component
 * Displays when the application is in offline mode
 * @returns {React.Component} Offline page
 */
export const OfflinePage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [showOfflineMessage, setShowOfflineMessage] = useState(false);

  const handleRetryConnection = () => {
    // Check if online and reload
    if (navigator.onLine) {
      window.location.reload();
    } else {
      // Show feedback that still offline
      setShowOfflineMessage(true);
      setTimeout(() => setShowOfflineMessage(false), 3000);
    }
  };

  const handleGoToCachedContent = () => {
    // Navigate to cached/offline content
    navigate('/offline/cached');
  };

  const handleGoHome = () => {
    navigate('/');
  };

  return (
    <div className="error-page offline-page">
      <Container>
        <Row className="min-vh-100 align-items-center justify-content-center">
          <Col lg={8}>
            <div className="error-content">
              <div className="text-center mb-5">
                <div className="error-icon mb-4">
                  <i className="bi bi-cloud-slash offline-icon"></i>
                </div>

                <h1 className="error-title mb-3">
                  {t('offline.title', "You're Offline")}
                </h1>

                <p className="error-description mb-4">
                  {t(
                    'offline.description',
                    'No internet connection detected. Some features may not be available, but you can still access cached content.'
                  )}
                </p>

                <div className="error-actions d-flex flex-wrap gap-3 justify-content-center mb-5">
                  <Button
                    variant="primary"
                    size="lg"
                    onClick={handleRetryConnection}
                  >
                    <i className="bi bi-arrow-repeat me-2"></i>
                    {t('offline.retry', 'Try Again')}
                  </Button>

                  <Button
                    variant="outline-primary"
                    size="lg"
                    onClick={handleGoToCachedContent}
                  >
                    <i className="bi bi-archive me-2"></i>
                    {t('offline.viewCached', 'View Cached Content')}
                  </Button>

                  <Button
                    variant="outline-secondary"
                    size="lg"
                    onClick={handleGoHome}
                  >
                    <i className="bi bi-house-door me-2"></i>
                    {t('offline.goHome', 'Go Home')}
                  </Button>
                </div>
              </div>

              {/* Offline message toast */}
              {showOfflineMessage && (
                <div
                  className="position-fixed top-0 start-50 translate-middle-x mt-3"
                  style={{ zIndex: 1050 }}
                >
                  <div
                    className="alert alert-warning alert-dismissible fade show"
                    role="alert"
                  >
                    <i className="bi bi-exclamation-triangle me-2"></i>
                    {t(
                      'offline.stillOffline',
                      'Still offline. Please check your connection.'
                    )}
                    <button
                      type="button"
                      className="btn-close"
                      onClick={() => setShowOfflineMessage(false)}
                      aria-label="Close"
                    ></button>
                  </div>
                </div>
              )}

              {/* Available offline features */}
              <Row>
                <Col md={6} className="mb-4">
                  <Card className="h-100 offline-feature-card">
                    <Card.Body className="text-center">
                      <div className="feature-icon mb-3">
                        <i className="bi bi-journal-bookmark text-success"></i>
                      </div>
                      <Card.Title>
                        {t('offline.features.cached.title', 'Cached Content')}
                      </Card.Title>
                      <Card.Text>
                        {t(
                          'offline.features.cached.description',
                          "Access previously loaded content and data that's stored locally."
                        )}
                      </Card.Text>
                      <Button variant="outline-success" size="sm">
                        {t('offline.features.cached.action', 'Browse Cached')}
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>

                <Col md={6} className="mb-4">
                  <Card className="h-100 offline-feature-card">
                    <Card.Body className="text-center">
                      <div className="feature-icon mb-3">
                        <i className="bi bi-pencil-square text-info"></i>
                      </div>
                      <Card.Title>
                        {t('offline.features.drafts.title', 'Work Offline')}
                      </Card.Title>
                      <Card.Text>
                        {t(
                          'offline.features.drafts.description',
                          'Continue working with drafts and local data. Changes will sync when reconnected.'
                        )}
                      </Card.Text>
                      <Button variant="outline-info" size="sm">
                        {t('offline.features.drafts.action', 'View Drafts')}
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              </Row>

              <div className="offline-tips mt-5">
                <h5 className="mb-3 text-center">
                  {t('offline.tips.title', "While you're offline:")}
                </h5>
                <Row>
                  <Col md={4} className="mb-3">
                    <div className="tip-item text-center">
                      <i className="bi bi-check-circle-fill text-success mb-2 d-block"></i>
                      <small>
                        {t('offline.tips.browse', 'Browse cached pages')}
                      </small>
                    </div>
                  </Col>
                  <Col md={4} className="mb-3">
                    <div className="tip-item text-center">
                      <i className="bi bi-check-circle-fill text-success mb-2 d-block"></i>
                      <small>
                        {t('offline.tips.edit', 'Edit draft content')}
                      </small>
                    </div>
                  </Col>
                  <Col md={4} className="mb-3">
                    <div className="tip-item text-center">
                      <i className="bi bi-check-circle-fill text-success mb-2 d-block"></i>
                      <small>
                        {t('offline.tips.sync', 'Auto-sync when online')}
                      </small>
                    </div>
                  </Col>
                </Row>
              </div>

              <div className="connection-status-detailed mt-4 text-center">
                <div className="d-flex align-items-center justify-content-center mb-2">
                  <div className="connection-indicator offline me-2"></div>
                  <small className="text-muted">
                    {t('offline.status', 'Status: Offline Mode')}
                  </small>
                </div>
                <small className="text-muted">
                  {t(
                    'offline.autoCheck',
                    'Automatically checking for connection...'
                  )}
                </small>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

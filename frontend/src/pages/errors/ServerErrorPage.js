import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/ErrorPages.css';

/**
 * 500 Internal Server Error page component
 * Displays when there's a server-side error
 * @returns {React.Component} 500 error page
 */
export const ServerErrorPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleRefresh = () => {
    window.location.reload();
  };

  const handleGoHome = () => {
    navigate('/');
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="error-page server-error-page">
      <Container>
        <Row className="min-vh-100 align-items-center justify-content-center">
          <Col lg={6} className="text-center">
            <div className="error-content">
              <div className="error-code mb-4">
                <span className="error-number">500</span>
              </div>

              <div className="error-illustration mb-4">
                <div className="server-error-icon-container mb-4">
                  <i
                    className="bi bi-exclamation-triangle-fill text-danger"
                    style={{ fontSize: '8rem' }}
                  ></i>
                </div>
              </div>

              <h1 className="error-title mb-3">
                {t('errors.serverErrorPage.title', 'Something Went Wrong')}
              </h1>

              <p className="error-description mb-4">
                {t(
                  'errors.serverErrorPage.description',
                  "We're experiencing some technical difficulties. Our team has been notified and is working to resolve the issue."
                )}
              </p>

              <div className="error-actions d-flex flex-wrap gap-3 justify-content-center">
                <Button variant="primary" size="lg" onClick={handleRefresh}>
                  <i className="bi bi-arrow-clockwise me-2"></i>
                  {t('errors.serverErrorPage.refresh', 'Try Again')}
                </Button>

                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={handleGoHome}
                >
                  <i className="bi bi-house-door me-2"></i>
                  {t('errors.serverErrorPage.goHome', 'Go to Homepage')}
                </Button>

                <Button
                  variant="outline-secondary"
                  size="lg"
                  onClick={handleGoBack}
                >
                  <i className="bi bi-arrow-left me-2"></i>
                  {t('errors.serverErrorPage.goBack', 'Go Back')}
                </Button>
              </div>

              <div className="error-suggestions mt-5">
                <h5 className="mb-3">
                  {t(
                    'errors.serverErrorPage.suggestions.title',
                    'What happened?'
                  )}
                </h5>
                <ul className="list-unstyled text-start">
                  <li className="mb-2">
                    <i className="bi bi-exclamation-triangle-fill text-warning me-2"></i>
                    {t(
                      'errors.serverErrorPage.suggestions.temporary',
                      'This is likely a temporary issue'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-exclamation-triangle-fill text-warning me-2"></i>
                    {t(
                      'errors.serverErrorPage.suggestions.maintenance',
                      'Server might be under maintenance'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-exclamation-triangle-fill text-warning me-2"></i>
                    {t(
                      'errors.serverErrorPage.suggestions.tryLater',
                      'Please try again in a few minutes'
                    )}
                  </li>
                </ul>
              </div>

              <div className="error-status mt-4">
                <small className="text-muted">
                  {t('errors.serverErrorPage.errorId', 'Error ID')}:{' '}
                  {Date.now()}
                </small>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

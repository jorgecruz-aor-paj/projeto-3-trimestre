import React from 'react';
import PropTypes from 'prop-types';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/ErrorPages.css';

/**
 * Generic Error page component
 * Displays when an unexpected error occurs
 * @param {Object} props - Component props
 * @param {string} props.errorCode - Error code to display
 * @param {string} props.errorMessage - Custom error message
 * @param {Function} props.onRetry - Retry callback function
 * @returns {React.Component} Generic error page
 */
export const GenericErrorPage = ({
  errorCode = 'ERROR',
  errorMessage = null,
  onRetry = null,
}) => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleRetry = () => {
    if (onRetry) {
      onRetry();
    } else {
      window.location.reload();
    }
  };

  const handleGoHome = () => {
    navigate('/');
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleContactSupport = () => {
    // You can implement this to open a contact form or mailto link
    window.open('mailto:support@company.com', '_blank');
  };

  return (
    <div className="error-page generic-error-page">
      <Container>
        <Row className="min-vh-100 align-items-center justify-content-center">
          <Col lg={6} className="text-center">
            <div className="error-content">
              <div className="error-code mb-4">
                <span className="error-number">{errorCode}</span>
              </div>

              <div className="error-illustration mb-4">
                <img
                  src="/images/generic-error-illustration.svg"
                  alt="Error occurred"
                  className="img-fluid error-image"
                  onError={e => {
                    e.target.onerror = null;
                    e.target.src =
                      'https://via.placeholder.com/400x300?text=Unexpected+Error';
                  }}
                />
              </div>

              <h1 className="error-title mb-3">
                {t('errors.genericPage.title', 'Oops! Something went wrong')}
              </h1>

              <p className="error-description mb-4">
                {errorMessage ||
                  t(
                    'errors.genericPage.description',
                    'An unexpected error has occurred. We apologize for the inconvenience and are working to fix this issue.'
                  )}
              </p>

              <div className="error-actions d-flex flex-wrap gap-3 justify-content-center">
                {(onRetry || !errorMessage) && (
                  <Button variant="primary" size="lg" onClick={handleRetry}>
                    <i className="bi bi-arrow-clockwise me-2"></i>
                    {t('errors.genericPage.retry', 'Try Again')}
                  </Button>
                )}

                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={handleGoHome}
                >
                  <i className="bi bi-house-door me-2"></i>
                  {t('errors.genericPage.goHome', 'Go to Homepage')}
                </Button>

                <Button
                  variant="outline-secondary"
                  size="lg"
                  onClick={handleGoBack}
                >
                  <i className="bi bi-arrow-left me-2"></i>
                  {t('errors.genericPage.goBack', 'Go Back')}
                </Button>
              </div>

              <div className="error-suggestions mt-5">
                <h5 className="mb-3">
                  {t('errors.genericPage.suggestions.title', 'What can you try?')}
                </h5>
                <ul className="list-unstyled text-start">
                  <li className="mb-2">
                    <i className="bi bi-arrow-clockwise text-info me-2"></i>
                    {t(
                      'errors.genericPage.suggestions.refresh',
                      'Refresh the page and try again'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-clock text-info me-2"></i>
                    {t(
                      'errors.genericPage.suggestions.waitAndRetry',
                      'Wait a few minutes and try again'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-arrow-left text-info me-2"></i>
                    {t(
                      'errors.genericPage.suggestions.goBack',
                      'Go back to the previous page'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-envelope text-info me-2"></i>
                    {t(
                      'errors.genericPage.suggestions.contactSupport',
                      'Contact support if the problem persists'
                    )}
                  </li>
                </ul>
              </div>

              <div className="error-support mt-4">
                <Button
                  variant="link"
                  onClick={handleContactSupport}
                  className="text-decoration-none"
                >
                  <i className="bi bi-headset me-2"></i>
                  {t('errors.genericPage.contactSupport', 'Contact Support')}
                </Button>
              </div>

              <div className="error-details mt-3">
                <small className="text-muted">
                  {t('errors.genericPage.errorId', 'Error ID')}: {Date.now()}
                  <br />
                  {t('errors.genericPage.timestamp', 'Time')}:{' '}
                  {new Date().toLocaleString()}
                </small>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

// PropTypes validation
GenericErrorPage.propTypes = {
  errorCode: PropTypes.string,
  errorMessage: PropTypes.string,
  onRetry: PropTypes.func,
};

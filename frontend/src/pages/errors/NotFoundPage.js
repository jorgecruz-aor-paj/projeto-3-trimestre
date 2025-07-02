import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/ErrorPages.css';

/**
 * 404 Not Found error page component
 * Displays when a user tries to access a route that doesn't exist
 * @returns {React.Component} 404 error page
 */
export const NotFoundPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate('/');
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="error-page not-found-page">
      <Container>
        <Row className="min-vh-100 align-items-center justify-content-center">
          <Col lg={6} className="text-center">
            <div className="error-content">
              <div className="error-code mb-4">
                <span className="error-number">404</span>
              </div>
              <div className="error-illustration mb-4">
                <i
                  className="bi bi-question-circle-fill text-primary"
                  style={{ fontSize: '8rem' }}
                ></i>
              </div>
              <h1 className="error-title mb-3">
                {t('errors.notFoundPage.title', 'Page Not Found')}
              </h1>
              <p className="error-description mb-4">
                {t(
                  'errors.notFoundPage.description',
                  'Sorry, the page you are looking for could not be found. It might have been moved, deleted, or you entered the wrong URL.'
                )}
              </p>
              <div className="error-actions d-flex flex-wrap gap-3 justify-content-center">
                <Button variant="primary" size="lg" onClick={handleGoHome}>
                  <i className="bi bi-house-door me-2"></i>
                  {t('errors.notFoundPage.goHome', 'Go to Homepage')}
                </Button>

                <Button
                  variant="outline-secondary"
                  size="lg"
                  onClick={handleGoBack}
                >
                  <i className="bi bi-arrow-left me-2"></i>
                  {t('errors.notFoundPage.goBack', 'Go Back')}
                </Button>
              </div>
              <div className="error-suggestions mt-5">
                <h5 className="mb-3">
                  {t(
                    'errors.notFoundPage.suggestions.title',
                    'What can you do?'
                  )}
                </h5>
                <ul className="list-unstyled text-start">
                  <li className="mb-2">
                    <i className="bi bi-check-circle-fill text-success me-2"></i>
                    {t(
                      'errors.notFoundPage.suggestions.checkUrl',
                      'Check the URL for any typos'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-check-circle-fill text-success me-2"></i>
                    {t(
                      'errors.notFoundPage.suggestions.useNavigation',
                      'Use the navigation menu to find what you need'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-check-circle-fill text-success me-2"></i>
                    {t(
                      'errors.notFoundPage.suggestions.contact',
                      'Contact support if you believe this is an error'
                    )}
                  </li>
                </ul>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/ErrorPages.css';

/**
 * 403 Forbidden error page component
 * Displays when a user tries to access a resource they don't have permission to view
 * @returns {React.Component} 403 error page
 */
export const ForbiddenPage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate('/');
  };

  const handleGoToLogin = () => {
    navigate('/login');
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="error-page forbidden-page">
      <Container>
        <Row className="min-vh-100 align-items-center justify-content-center">
          <Col lg={6} className="text-center">
            <div className="error-content">
              <div className="error-code mb-4">
                <span className="error-number">403</span>
              </div>

              <div className="error-illustration mb-4">
                <i
                  className="bi bi-shield-lock-fill text-danger"
                  style={{ fontSize: '10rem' }}
                ></i>
              </div>

              <h1 className="error-title mb-3">
                {t('errors.forbiddenPage.title', 'Access Forbidden')}
              </h1>

              <p className="error-description mb-4">
                {t(
                  'errors.forbiddenPage.description',
                  "You don't have permission to access this resource. Please contact your administrator if you believe you should have access."
                )}
              </p>

              <div className="error-actions d-flex flex-wrap gap-3 justify-content-center">
                <Button variant="primary" size="lg" onClick={handleGoToLogin}>
                  <i className="bi bi-speedometer2 me-2"></i>
                  {t('errors.forbiddenPage.goToLogin', 'Ir para o Login')}
                </Button>

                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={handleGoHome}
                >
                  <i className="bi bi-house-door me-2"></i>
                  {t('errors.forbiddenPage.goHome', 'Go to Homepage')}
                </Button>

                <Button
                  variant="outline-secondary"
                  size="lg"
                  onClick={handleGoBack}
                >
                  <i className="bi bi-arrow-left me-2"></i>
                  {t('errors.forbiddenPage.goBack', 'Go Back')}
                </Button>
              </div>

              <div className="error-suggestions mt-5">
                <h5 className="mb-3">
                  {t('errors.forbiddenPage.suggestions.title', 'Need access?')}
                </h5>
                <ul className="list-unstyled text-start">
                  <li className="mb-2">
                    <i className="bi bi-info-circle-fill text-info me-2"></i>
                    {t(
                      'errors.forbiddenPage.suggestions.checkRole',
                      'Verify your account role and permissions'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-info-circle-fill text-info me-2"></i>
                    {t(
                      'errors.forbiddenPage.suggestions.contactAdmin',
                      'Contact your administrator for access'
                    )}
                  </li>
                  <li className="mb-2">
                    <i className="bi bi-info-circle-fill text-info me-2"></i>
                    {t(
                      'errors.forbiddenPage.suggestions.relogin',
                      'Try logging out and logging back in'
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

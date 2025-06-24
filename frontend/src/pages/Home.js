import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import 'styles/pages/Home.css';

/**
 * Home page component
 * @returns {React.Component} Home page
 */
export const Home = () => {
  const { t } = useTranslation();

  return (
    <div className="home-page">
      {/* Hero section */}
      <section className="hero-section">
        <Container>
          <Row className="align-items-center">
            <Col lg={6} className="mb-5 mb-lg-0">
              <h1 className="display-4 fw-bold mb-4">{t('app.title')}</h1>
              <p className="lead mb-4">{t('app.subtitle')}</p>
              <div className="d-flex flex-wrap gap-3">
                <Button variant="primary" size="lg">
                  {t('home.getStarted')}
                </Button>
                <Button variant="outline-primary" size="lg">
                  {t('home.learnMore')}
                </Button>
              </div>
            </Col>
            <Col lg={6}>
              <img
                src="/images/hero-image.svg"
                alt="Performance Management"
                className="img-fluid hero-image"
                onError={e => {
                  e.target.onerror = null;
                  e.target.src =
                    'https://via.placeholder.com/600x400?text=Performance+Management';
                }}
              />
            </Col>
          </Row>
        </Container>
      </section>

      {/* Features section */}
      <section className="features-section py-5">
        <Container>
          <h2 className="text-center mb-5">
            {t('home.features.title', 'Key Features')}
          </h2>
          <Row>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-graph-up"></i>
                  </div>
                  <Card.Title>
                    {t('home.features.tracking.title', 'Performance Tracking')}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'home.features.tracking.description',
                      'Track performance metrics in real-time with intuitive dashboards and reports.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-people"></i>
                  </div>
                  <Card.Title>
                    {t('home.features.feedback.title', '360° Feedback')}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'home.features.feedback.description',
                      'Collect and analyze feedback from peers, managers, and direct reports.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-trophy"></i>
                  </div>
                  <Card.Title>
                    {t('home.features.goals.title', 'Goal Management')}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'home.features.goals.description',
                      'Set, track, and achieve individual and team goals aligned with organizational objectives.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Call to action section */}
      <section className="cta-section py-5 text-center text-white">
        <Container>
          <h2 className="mb-4">
            {t(
              'home.cta.title',
              'Ready to transform your performance management?'
            )}
          </h2>
          <p className="lead mb-4">
            {t(
              'home.cta.description',
              'Join thousands of companies already using our platform to improve team performance.'
            )}
          </p>
          <Button variant="light" size="lg">
            {t('home.cta.button', 'Start Free Trial')}
          </Button>
        </Container>
      </section>
    </div>
  );
};

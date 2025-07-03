import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/TrainingsPage.css';

/**
 * Training teaser component for non-authenticated users
 * Showcases training capabilities and benefits
 * @returns {React.Component} Training teaser page
 */
export const TrainingTeaser = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleGetStarted = () => {
    navigate('/auth');
  };

  const handleLearnMore = () => {
    // Could scroll to features section or redirect to documentation
    document.getElementById('features-section')?.scrollIntoView({
      behavior: 'smooth',
    });
  };

  return (
    <div className="trainings-page">
      {/* Hero section */}
      <section className="hero-section">
        <Container>
          <Row className="align-items-center">
            <Col lg={6} className="mb-5 mb-lg-0">
              <h1 className="display-4 fw-bold mb-4">
                {t('trainings.title', 'Professional Training Programs')}
              </h1>
              <p className="lead mb-4">
                {t(
                  'trainings.hero.subtitle',
                  'Unlock your potential with our comprehensive training programs designed to enhance skills and accelerate career growth.'
                )}
              </p>
              <div className="d-flex flex-wrap gap-3">
                <Button variant="primary" size="lg" onClick={handleGetStarted}>
                  {t('trainings.hero.getStarted', 'Get Started')}
                </Button>
                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={handleLearnMore}
                >
                  {t('trainings.hero.learnMore', 'Learn More')}
                </Button>
              </div>
            </Col>
            <Col lg={6}>
              <div className="d-flex justify-content-center">
                <div className="hero-icon text-primary">
                  <i
                    className="bi bi-book-half"
                    style={{ fontSize: '12rem' }}
                  ></i>
                </div>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Features section */}
      <section id="features-section" className="features-section py-5">
        <Container>
          <h2 className="text-center mb-5">
            {t('trainings.features.title', 'Training Features')}
          </h2>
          <Row>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-book"></i>
                  </div>
                  <Card.Title>
                    {t('trainings.features.diverse.title', 'Diverse Programs')}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'trainings.features.diverse.description',
                      'Access a wide range of training programs covering technical skills, leadership, and professional development.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-graph-up-arrow"></i>
                  </div>
                  <Card.Title>
                    {t(
                      'trainings.features.progress.title',
                      'Progress Tracking'
                    )}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'trainings.features.progress.description',
                      'Monitor your learning journey with detailed progress tracking and completion certificates.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-people-fill"></i>
                  </div>
                  <Card.Title>
                    {t('trainings.features.expert.title', 'Expert Instructors')}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'trainings.features.expert.description',
                      'Learn from industry experts and experienced professionals with interactive sessions and mentoring.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Benefits section */}
      <section className="benefits-section py-5">
        <Container>
          <Row className="align-items-center">
            <Col lg={6} className="mb-5 mb-lg-0">
              <div className="d-flex justify-content-center">
                <div className="feature-icon-large text-primary">
                  <i
                    className="bi bi-mortarboard-fill"
                    style={{ fontSize: '8rem' }}
                  ></i>
                </div>
              </div>
            </Col>
            <Col lg={6}>
              <h3 className="mb-4">
                {t(
                  'trainings.benefits.title',
                  'Why Choose Our Training Programs?'
                )}
              </h3>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'trainings.benefits.flexible',
                    'Flexible scheduling to fit your work-life balance'
                  )}
                </span>
              </div>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'trainings.benefits.personalized',
                    'Personalized learning paths based on your role and goals'
                  )}
                </span>
              </div>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'trainings.benefits.certificates',
                    'Industry-recognized certificates upon completion'
                  )}
                </span>
              </div>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'trainings.benefits.support',
                    '24/7 learning support and community access'
                  )}
                </span>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Call to action section */}
      <section className="cta-section py-5 text-center text-white">
        <Container>
          <h2 className="mb-4">
            {t(
              'trainings.cta.title',
              'Ready to advance your career with professional training?'
            )}
          </h2>
          <p className="lead mb-4">
            {t(
              'trainings.cta.description',
              'Join thousands of professionals who have enhanced their skills and accelerated their careers through our training programs.'
            )}
          </p>
          <Button variant="light" size="lg" onClick={handleGetStarted}>
            {t('trainings.cta.button', 'Start Learning Today')}
          </Button>
        </Container>
      </section>
    </div>
  );
};

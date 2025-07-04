import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import 'styles/pages/EvaluationsPage.css';

/**
 * Evaluation teaser component for non-authenticated users
 * Showcases evaluation capabilities and benefits
 * @returns {React.Component} Evaluation teaser page
 */
export const EvaluationTeaser = () => {
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
    <div className="evaluations-page">
      {/* Hero section */}
      <section className="hero-section">
        <Container>
          <Row className="align-items-center">
            <Col lg={6} className="mb-5 mb-lg-0">
              <h1 className="display-4 fw-bold mb-4">
                {t('evaluations.title', 'Performance Evaluation System')}
              </h1>
              <p className="lead mb-4">
                {t(
                  'evaluations.hero.subtitle',
                  'Comprehensive performance evaluation tools that drive meaningful feedback, growth, and organizational success.'
                )}
              </p>
              <div className="d-flex flex-wrap gap-3">
                <Button variant="primary" size="lg" onClick={handleGetStarted}>
                  {t('evaluations.hero.viewEvaluations', 'View Evaluations')}
                </Button>
                <Button
                  variant="outline-primary"
                  size="lg"
                  onClick={handleLearnMore}
                >
                  {t('evaluations.hero.learnProcess', 'Learn the Process')}
                </Button>
              </div>
            </Col>
            <Col lg={6}>
              <div className="d-flex justify-content-center">
                <div className="hero-icon text-primary">
                  <i
                    className="bi bi-clipboard-check"
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
            {t('evaluations.features.title', 'Evaluation Features')}
          </h2>
          <Row>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-clipboard-check"></i>
                  </div>
                  <Card.Title>
                    {t(
                      'evaluations.features.comprehensive.title',
                      '360° Assessment'
                    )}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'evaluations.features.comprehensive.description',
                      'Multi-dimensional evaluations including self-assessment, peer feedback, and manager reviews for complete insights.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-bar-chart"></i>
                  </div>
                  <Card.Title>
                    {t(
                      'evaluations.features.analytics.title',
                      'Performance Analytics'
                    )}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'evaluations.features.analytics.description',
                      'Detailed analytics and reporting to identify strengths, areas for improvement, and performance trends.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4} className="mb-4">
              <Card className="h-100 feature-card">
                <Card.Body className="text-center">
                  <div className="feature-icon mb-3">
                    <i className="bi bi-target"></i>
                  </div>
                  <Card.Title>
                    {t('evaluations.features.goals.title', 'Goal-Oriented')}
                  </Card.Title>
                  <Card.Text>
                    {t(
                      'evaluations.features.goals.description',
                      'Link evaluations to specific goals and objectives, ensuring alignment with organizational priorities.'
                    )}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Process section */}
      <section className="process-section py-5">
        <Container>
          <h2 className="text-center mb-5">
            {t('evaluations.process.title', 'Evaluation Process')}
          </h2>
          <Row>
            <Col md={3} className="mb-4">
              <div className="process-step text-center">
                <div className="step-number mb-3">1</div>
                <h5>
                  {t('evaluations.process.step1.title', 'Set Objectives')}
                </h5>
                <p className="text-muted">
                  {t(
                    'evaluations.process.step1.description',
                    'Define clear performance objectives and key results for the evaluation period.'
                  )}
                </p>
              </div>
            </Col>
            <Col md={3} className="mb-4">
              <div className="process-step text-center">
                <div className="step-number mb-3">2</div>
                <h5>
                  {t('evaluations.process.step2.title', 'Continuous Feedback')}
                </h5>
                <p className="text-muted">
                  {t(
                    'evaluations.process.step2.description',
                    'Provide ongoing feedback and support throughout the evaluation period.'
                  )}
                </p>
              </div>
            </Col>
            <Col md={3} className="mb-4">
              <div className="process-step text-center">
                <div className="step-number mb-3">3</div>
                <h5>{t('evaluations.process.step3.title', 'Formal Review')}</h5>
                <p className="text-muted">
                  {t(
                    'evaluations.process.step3.description',
                    'Conduct comprehensive evaluation sessions with structured assessments.'
                  )}
                </p>
              </div>
            </Col>
            <Col md={3} className="mb-4">
              <div className="process-step text-center">
                <div className="step-number mb-3">4</div>
                <h5>
                  {t('evaluations.process.step4.title', 'Development Plan')}
                </h5>
                <p className="text-muted">
                  {t(
                    'evaluations.process.step4.description',
                    'Create personalized development plans based on evaluation results.'
                  )}
                </p>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Benefits section */}
      <section className="benefits-section py-5">
        <Container>
          <Row className="align-items-center">
            <Col lg={6}>
              <h3 className="mb-4">
                {t(
                  'evaluations.benefits.title',
                  'Benefits of Regular Evaluations'
                )}
              </h3>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'evaluations.benefits.growth',
                    'Accelerated professional growth and skill development'
                  )}
                </span>
              </div>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'evaluations.benefits.clarity',
                    'Clear understanding of expectations and performance standards'
                  )}
                </span>
              </div>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'evaluations.benefits.recognition',
                    'Recognition of achievements and contributions'
                  )}
                </span>
              </div>
              <div className="benefit-item mb-3">
                <i className="bi bi-check-circle-fill text-success me-3"></i>
                <span>
                  {t(
                    'evaluations.benefits.alignment',
                    'Better alignment between individual and organizational goals'
                  )}
                </span>
              </div>
            </Col>
            <Col lg={6} className="mb-5 mb-lg-0">
              <div className="d-flex justify-content-center">
                <div className="benefits-icon text-primary">
                  <i
                    className="bi bi-graph-up-arrow"
                    style={{ fontSize: '8rem' }}
                  ></i>
                </div>
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
              'evaluations.cta.title',
              'Ready to transform your performance evaluation process?'
            )}
          </h2>
          <p className="lead mb-4">
            {t(
              'evaluations.cta.description',
              'Join organizations worldwide using our evaluation system to build high-performing teams and drive success.'
            )}
          </p>
          <Button variant="light" size="lg" onClick={handleGetStarted}>
            {t('evaluations.cta.button', 'Get Started')}
          </Button>
        </Container>
      </section>
    </div>
  );
};

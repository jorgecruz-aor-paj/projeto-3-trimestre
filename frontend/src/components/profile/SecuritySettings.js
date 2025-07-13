import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { TwoFactorSecuritySettings } from 'components/security/TwoFactorSecuritySettings';

/**
 * Security Settings Component for Profile Page
 * Contains all security-related settings including 2FA
 */
export const SecuritySettings = () => {
  const { t } = useTranslation();

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
              <p className="text-muted mb-0">
                {t('security.passwordSettings.description')}
              </p>
              {/* Password change functionality can be added here later */}
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

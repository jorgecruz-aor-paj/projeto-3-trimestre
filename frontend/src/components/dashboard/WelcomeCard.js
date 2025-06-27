import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { Card, Row, Col } from 'react-bootstrap';

/**
 * Welcome card component for dashboard
 * @param {Object} props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Welcome card
 */
export const WelcomeCard = ({ user }) => {
  const { t } = useTranslation();

  const getWelcomeMessage = () => {
    const hour = new Date().getHours();
    if (hour < 12) {
      return t('dashboard.goodMorning');
    } else if (hour < 20) {
      return t('dashboard.goodAfternoon');
    } else {
      return t('dashboard.goodEvening');
    }
  };

  const getRoleSpecificMessage = role => {
    switch (role?.toUpperCase()) {
      case 'ADMIN':
        return t('dashboard.welcome.admin');
      case 'MANAGER':
        return t('dashboard.welcome.manager');
      case 'USER':
        return t('dashboard.welcome.user');
      default:
        return t('dashboard.welcome.default');
    }
  };

  return (
    <Card className="welcome-card bg-primary text-white">
      <Card.Body>
        <Row className="align-items-center">
          <Col md={8}>
            <h3 className="mb-2">
              {getWelcomeMessage()}, {user?.firstName}! 👋
            </h3>
            <p className="mb-0 lead">{getRoleSpecificMessage(user?.role)}</p>
          </Col>
          <Col md={4} className="text-end d-none d-md-block">
            <div className="welcome-icon">
              <i
                className="bi bi-speedometer2"
                style={{ fontSize: '3rem' }}
              ></i>
            </div>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

WelcomeCard.propTypes = {
  user: PropTypes.shape({
    firstName: PropTypes.string,
    role: PropTypes.string,
  }),
};

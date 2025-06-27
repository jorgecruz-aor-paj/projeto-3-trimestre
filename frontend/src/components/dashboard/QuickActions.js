import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Card, Row, Col, Button } from 'react-bootstrap';

/**
 * Quick actions component for dashboard shortcuts
 * @param {Object} props
 * @param {string} props.title - Section title
 * @param {Array} props.actions - Array of action objects
 * @returns {React.Component} Quick actions section
 */
export const QuickActions = ({ title, actions }) => {
  return (
    <Card className="quick-actions-card">
      <Card.Header>
        <h5 className="mb-0">
          <i className="bi bi-lightning me-2"></i>
          {title}
        </h5>
      </Card.Header>
      <Card.Body>
        <Row>
          {actions.map((action, index) => (
            <Col md={6} lg={3} key={index} className="mb-3">
              <Button
                as={Link}
                to={action.link}
                variant={`outline-${action.variant}`}
                className="quick-action-btn w-100 h-100 text-start"
              >
                <div className="d-flex flex-column align-items-start">
                  <div className="mb-2">
                    <i className={`${action.icon} fs-4`}></i>
                  </div>
                  <h6 className="mb-1">{action.title}</h6>
                  <small className="text-muted">{action.description}</small>
                </div>
              </Button>
            </Col>
          ))}
        </Row>
      </Card.Body>
    </Card>
  );
};

QuickActions.propTypes = {
  title: PropTypes.string.isRequired,
  actions: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      icon: PropTypes.string.isRequired,
      link: PropTypes.string.isRequired,
      variant: PropTypes.string.isRequired,
    })
  ).isRequired,
};

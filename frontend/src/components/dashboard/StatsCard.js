import React from 'react';
import PropTypes from 'prop-types';
import { Card } from 'react-bootstrap';

/**
 * Statistics card component for displaying metrics
 * @param {Object} props
 * @param {string} props.title - Card title
 * @param {number|string} props.value - Statistical value
 * @param {string} props.icon - Bootstrap icon class
 * @param {string} props.variant - Bootstrap color variant
 * @param {string} [props.trend] - Trend indicator
 * @param {boolean} [props.trendPositive] - Whether trend is positive
 * @returns {React.Component} Stats card
 */
export const StatsCard = ({
  title,
  value,
  icon,
  variant = 'primary',
  trend,
  trendPositive,
}) => {
  return (
    <Card className="h-100 border-0">
      <Card.Body>
        <div className="d-flex justify-content-between align-items-start">
          <div>
            <h6 className="text-muted mb-2">{title}</h6>
            <h3 className="mb-1">{value}</h3>
            {trend && (
              <small
                className={`trend ${
                  trendPositive ? 'text-success' : 'text-danger'
                }`}
              >
                <i
                  className={`bi ${
                    trendPositive ? 'bi-arrow-up' : 'bi-arrow-down'
                  } me-1`}
                ></i>
                {trend}
              </small>
            )}
          </div>
          <div className={`stats-icon bg-${variant}`}>
            <i className={icon}></i>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

StatsCard.propTypes = {
  title: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
  icon: PropTypes.string.isRequired,
  variant: PropTypes.string,
  trend: PropTypes.string,
  trendPositive: PropTypes.bool,
};

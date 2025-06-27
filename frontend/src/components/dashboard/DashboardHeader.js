import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { Row, Col, Badge } from 'react-bootstrap';

/**
 * Dashboard header component with user info and role badge
 * @param {Object} props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Dashboard header
 */
export const DashboardHeader = ({ user }) => {
  const { t } = useTranslation();

  const getRoleBadgeVariant = role => {
    switch (role?.toUpperCase()) {
      case 'ADMIN':
        return 'danger';
      case 'MANAGER':
        return 'warning';
      case 'USER':
        return 'primary';
      default:
        return 'secondary';
    }
  };

  const getRoleDisplayName = role => {
    switch (role?.toUpperCase()) {
      case 'ADMIN':
        return t('roles.admin');
      case 'MANAGER':
        return t('roles.manager');
      case 'USER':
        return t('roles.user');
      default:
        return role || t('roles.unknown');
    }
  };

  return (
    <Row className="mb-4">
      <Col>
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <h1 className="dashboard-title mb-2">{t('dashboard.title')}</h1>
            <p className="text-muted mb-0">{t('dashboard.subtitle')}</p>
          </div>
          <div className="text-end">
            <div className="user-info">
              <h5 className="mb-1">
                {user?.firstName} {user?.lastName}
              </h5>
              <Badge
                bg={getRoleBadgeVariant(user?.role)}
                className="role-badge"
              >
                <i className="bi bi-person-badge me-1"></i>
                {getRoleDisplayName(user?.role)}
              </Badge>
            </div>
          </div>
        </div>
      </Col>
    </Row>
  );
};

DashboardHeader.propTypes = {
  user: PropTypes.shape({
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    role: PropTypes.string,
  }),
};

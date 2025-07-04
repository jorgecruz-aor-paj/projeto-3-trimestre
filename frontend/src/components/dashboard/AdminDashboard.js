import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { Row, Col, Alert, Spinner } from 'react-bootstrap';
import { statsService } from 'services/statsService';
import { AdminStatsGrid } from './AdminStatsGrid';
import { QuickActions } from './QuickActions';

/**
 * Admin dashboard component with admin-specific widgets
 * @param {Object} props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Admin dashboard
 */
export const AdminDashboard = () => {
  const { t } = useTranslation();

  const adminQuickActions = [
    {
      title: t('dashboard.admin.actions.userManagement'),
      description: t('dashboard.admin.actions.userManagementDesc'),
      icon: 'bi-people',
      link: '/admin/users',
      variant: 'primary',
    },
    {
      title: t('dashboard.admin.actions.systemSettings'),
      description: t('dashboard.admin.actions.systemSettingsDesc'),
      icon: 'bi-gear',
      link: '/admin/settings',
      variant: 'secondary',
    },
    {
      title: t('dashboard.admin.actions.reports'),
      description: t('dashboard.admin.actions.reportsDesc'),
      icon: 'bi-graph-up',
      link: '/admin/reports',
      variant: 'success',
    },
    {
      title: t('dashboard.admin.actions.exportData'),
      description: t('dashboard.admin.actions.exportDataDesc'),
      icon: 'bi-download',
      link: '/admin/export',
      variant: 'info',
    },
  ];

  return (
    <div className="admin-dashboard">
      {/* Statistics Overview */}
      <Row className="mb-4">
        <Col>
          <h4 className="mb-3">
            <i className="bi bi-bar-chart me-2"></i>
            {t('dashboard.admin.systemOverview')}
          </h4>
        </Col>
      </Row>

      <AdminStatsGrid />

      {/* Quick Actions */}
      <Row className="mb-4">
        <Col>
          <QuickActions
            title={t('dashboard.admin.quickActions')}
            actions={adminQuickActions}
          />
        </Col>
      </Row>
    </div>
  );
};

AdminDashboard.propTypes = {
  user: PropTypes.shape({
    id: PropTypes.number,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    role: PropTypes.string,
  }).isRequired,
};

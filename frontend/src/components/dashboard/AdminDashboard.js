import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { Row, Col, Alert, Spinner } from 'react-bootstrap';
import { statsService } from 'services/statsService';
import { StatsCard } from './StatsCard';
import { QuickActions } from './QuickActions';

/**
 * Admin dashboard component with admin-specific widgets
 * @param {Object} props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Admin dashboard
 */
export const AdminDashboard = () => {
  const { t } = useTranslation();
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);

        // Fetch manager and evaluation stats in parallel
        const [managerStats, evaluationStats] = await Promise.all([
          statsService.getManagerStats(),
          statsService.getEvaluationStats(),
        ]);

        setStats({
          managers: managerStats,
          evaluations: evaluationStats,
        });
      } catch (err) {
        console.error('Failed to fetch admin stats:', err);
        setError(t('dashboard.admin.failedToLoadStats'));
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [t]);

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('dashboard.admin.loadingStats')}</p>
      </div>
    );
  }

  if (error) {
    return (
      <Alert variant="danger">
        <Alert.Heading>{t('dashboard.admin.errorTitle')}</Alert.Heading>
        <p>{error}</p>
      </Alert>
    );
  }

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

      <Row className="mb-4">
        <Col md={3}>
          <StatsCard
            title={t('dashboard.admin.stats.totalUsers')}
            value={stats.managers?.total || 0}
            icon="bi-people"
            variant="primary"
            trend="+5.2%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.admin.stats.unmanagedUsers')}
            value={stats.managers?.unmanaged || 0}
            icon="bi-person-x"
            variant="warning"
            trend={`${(
              ((stats.managers?.unmanaged || 0) /
                (stats.managers?.total || 1)) *
              100
            ).toFixed(1)}%`}
            trendPositive={false}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.admin.stats.completedEvaluations')}
            value={stats.evaluations?.completed || 0}
            icon="bi-check-circle"
            variant="success"
            trend="+12.3%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.admin.stats.pendingEvaluations')}
            value={stats.evaluations?.pending || 0}
            icon="bi-clock"
            variant="info"
            trend="-8.1%"
            trendPositive={true}
          />
        </Col>
      </Row>

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

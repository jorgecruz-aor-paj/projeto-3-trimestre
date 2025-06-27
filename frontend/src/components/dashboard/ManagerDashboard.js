import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Row, Col, Alert, Spinner } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { statsService } from 'services/statsService';
import { StatsCard } from './StatsCard';
import { QuickActions } from './QuickActions';

/**
 * Manager Dashboard Component
 * Displays manager-specific dashboard with team statistics, team actions, and recent activity
 * @param {Object} props - Component props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Manager dashboard
 */
export const ManagerDashboard = ({ user }) => {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState({
    team: null,
  });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        setError(null);

        // Fetch manager-specific statistics for user's team
        const teamStats = await statsService.getTeamStats(user.id);

        setStats({
          team: teamStats,
        });
      } catch (err) {
        console.error('Failed to fetch manager stats:', err);
        setError(t('dashboard.manager.failedToLoadStats'));
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
        <p>{t('dashboard.manager.loadingStats')}</p>
      </div>
    );
  }

  if (error) {
    return (
      <Alert variant="danger">
        <Alert.Heading>{t('dashboard.manager.errorTitle')}</Alert.Heading>
        <p>{error}</p>
      </Alert>
    );
  }

  const managerQuickActions = [
    {
      title: t('dashboard.manager.actions.teamManagement'),
      description: t('dashboard.manager.actions.teamManagementDesc'),
      icon: 'bi-people',
      href: '/team',
      variant: 'primary',
    },
    {
      title: t('dashboard.manager.actions.evaluateTeam'),
      description: t('dashboard.manager.actions.evaluateTeamDesc'),
      icon: 'bi-clipboard-check',
      href: '/evaluations/team',
      variant: 'success',
    },
    {
      title: t('dashboard.manager.actions.assignTraining'),
      description: t('dashboard.manager.actions.assignTrainingDesc'),
      icon: 'bi-mortarboard',
      href: '/trainings/assign',
      variant: 'info',
    },
    {
      title: t('dashboard.manager.actions.teamReports'),
      description: t('dashboard.manager.actions.teamReportsDesc'),
      icon: 'bi-graph-up',
      href: '/reports/team',
      variant: 'warning',
    },
  ];

  return (
    <div className="manager-dashboard">
      {/* Team Statistics */}
      <Row className="mb-4">
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.teamMembers')}
            value={stats.team?.totalMembers || 0}
            icon="bi-people"
            variant="primary"
            trend="+5.2%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.activeTrainings')}
            value={stats.team?.activeTrainings || 0}
            icon="bi-mortarboard"
            variant="success"
            trend="+15.3%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.pendingEvaluations')}
            value={stats.team?.pendingEvaluations || 0}
            icon="bi-clock"
            variant="warning"
            trend="-8.1%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.teamPerformance')}
            value={`${stats.team?.averagePerformance || 0}%`}
            icon="bi-trophy"
            variant="info"
            trend="+3.7%"
            trendPositive={true}
          />
        </Col>
      </Row>

      {/* Quick Actions */}
      <Row className="mb-4">
        <Col>
          <QuickActions
            title={t('dashboard.manager.quickActions')}
            actions={managerQuickActions}
          />
        </Col>
      </Row>
    </div>
  );
};

ManagerDashboard.propTypes = {
  user: PropTypes.shape({
    id: PropTypes.number,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    role: PropTypes.string,
  }).isRequired,
};

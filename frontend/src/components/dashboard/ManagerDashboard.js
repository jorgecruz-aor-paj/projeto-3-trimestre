import React from 'react';
import { useTranslation } from 'react-i18next';
import { Row, Col } from 'react-bootstrap';
import { StatsCard } from './StatsCard';
import { QuickActions } from './QuickActions';
import { TeamPerformanceChart } from './TeamPerformanceChart';
import { useAuthStore } from 'stores/AuthStore';
import { useManagerStats } from 'hooks/useManagerStats';

/**
 * Manager Dashboard Component
 * Displays manager-specific dashboard with team statistics, team actions, and recent activity
 * @param {Object} props - Component props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Manager dashboard
 */
export const ManagerDashboard = () => {
  const { t } = useTranslation();
  const { user } = useAuthStore();
  const { stats, loading, error } = useManagerStats(user?.id);

  const managerQuickActions = [
    {
      title: t('dashboard.manager.actions.teamManagement'),
      description: t('dashboard.manager.actions.teamManagementDesc'),
      icon: 'bi-people',
      link: '/users',
      variant: 'primary',
    },
    {
      title: t('dashboard.manager.actions.evaluateTeam'),
      description: t('dashboard.manager.actions.evaluateTeamDesc'),
      icon: 'bi-clipboard-check',
      link: '/evaluations',
      variant: 'success',
    },
    {
      title: t('dashboard.manager.actions.assignTraining'),
      description: t('dashboard.manager.actions.assignTrainingDesc'),
      icon: 'bi-mortarboard',
      link: '/users',
      variant: 'info',
    },
    {
      title: t('dashboard.manager.actions.teamReports'),
      description: t('dashboard.manager.actions.teamReportsDesc'),
      icon: 'bi-graph-up',
      link: '/not-implemented',
      variant: 'warning',
    },
  ];

  return (
    <div className="manager-dashboard">
      {/* Error display */}
      {error && (
        <Row className="mb-3">
          <Col>
            <div className="alert alert-warning" role="alert">
              {error}
            </div>
          </Col>
        </Row>
      )}

      {/* Team Statistics */}
      <Row className="mb-4">
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.teamMembers')}
            value={loading ? '...' : stats.teamMembers}
            icon="bi-people"
            variant="primary"
            trend=""
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.activeTrainings')}
            value={loading ? '...' : stats.activeTrainings}
            icon="bi-mortarboard"
            variant="success"
            trend=""
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.pendingEvaluations')}
            value={loading ? '...' : stats.pendingEvaluations}
            icon="bi-clock"
            variant="warning"
            trend=""
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.teamPerformance')}
            value={
              loading
                ? '...'
                : stats.teamPerformance > 0
                ? `${stats.teamPerformance}/4`
                : '--'
            }
            icon="bi-trophy"
            variant="info"
            trend=""
            trendPositive={true}
          />
        </Col>
      </Row>

      {/* Quick Actions */}
      <Row className="mb-4">
        <Col md={8}>
          <QuickActions
            title={t('dashboard.manager.quickActions')}
            actions={managerQuickActions}
          />
        </Col>
        <Col md={4}>
          <TeamPerformanceChart stats={stats} loading={loading} />
        </Col>
      </Row>
    </div>
  );
};

// TODO: Add PropTypes back when user prop is needed for backend integration

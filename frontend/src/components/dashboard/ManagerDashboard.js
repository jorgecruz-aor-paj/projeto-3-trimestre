import React from 'react';
import { useTranslation } from 'react-i18next';
import { Row, Col } from 'react-bootstrap';
import { StatsCard } from './StatsCard';
import { QuickActions } from './QuickActions';

/**
 * Manager Dashboard Component
 * Displays manager-specific dashboard with team statistics, team actions, and recent activity
 * @param {Object} props - Component props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} Manager dashboard
 */
export const ManagerDashboard = () => {
  const { t } = useTranslation();

  // TODO: Backend endpoints needed for manager dashboard statistics:
  // - Team member count for this manager
  // - Active trainings count for team members
  // - Pending evaluations count for team members
  // - Average team performance metrics
  //
  // Current available endpoints:
  // - GET /users (with filters) - could potentially filter by manager
  // - GET /trainings (with filters)
  // - GET /evaluations (with filters)
  // - GET /trainings/unparticipated/{userId}
  // - GET /trainings/participation/{userId}

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
      {/* Team Statistics - TODO: Implement when backend endpoints are available */}
      <Row className="mb-4">
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.teamMembers')}
            value="--"
            icon="bi-people"
            variant="primary"
            trend=""
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.activeTrainings')}
            value="--"
            icon="bi-mortarboard"
            variant="success"
            trend=""
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.pendingEvaluations')}
            value="--"
            icon="bi-clock"
            variant="warning"
            trend=""
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.manager.stats.teamPerformance')}
            value="--"
            icon="bi-trophy"
            variant="info"
            trend=""
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

// TODO: Add PropTypes back when user prop is needed for backend integration

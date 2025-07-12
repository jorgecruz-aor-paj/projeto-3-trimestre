import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Row, Col, Alert, Spinner } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { statsService } from 'services/statsService';
import { StatsCard } from './StatsCard';
import { QuickActions } from './QuickActions';
import { UserTrainingProgressChart } from './UserTrainingProgressChart';
import { UserPerformanceTrendChart } from './UserPerformanceTrendChart';
import { UserActivityOverviewChart } from './UserActivityOverviewChart';

/**
 * User Dashboard Component
 * Displays user-specific dashboard with personal statistics, quick actions, and recent activity
 * @param {Object} props - Component props
 * @param {Object} props.user - Current user object
 * @returns {React.Component} User dashboard
 */
export const UserDashboard = ({ user }) => {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState({
    personal: null,
  });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        setError(null);

        // Fetch user-specific statistics
        const personalStats = await statsService.getPersonalStats(user.id);

        setStats({
          personal: personalStats,
        });
      } catch (err) {
        console.error('Failed to fetch user stats:', err);
        setError(t('dashboard.user.failedToLoadStats'));
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [t, user.id]);

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('dashboard.user.loadingStats')}</p>
      </div>
    );
  }

  if (error) {
    return (
      <Alert variant="danger">
        <Alert.Heading>{t('dashboard.user.errorTitle')}</Alert.Heading>
        <p>{error}</p>
      </Alert>
    );
  }

  const userQuickActions = [
    {
      title: t('dashboard.user.actions.viewProfile'),
      description: t('dashboard.user.actions.viewProfileDesc'),
      icon: 'bi-person',
      link: '/profile',
      variant: 'primary',
    },
    {
      title: t('dashboard.user.actions.myEvaluations'),
      description: t('dashboard.user.actions.myEvaluationsDesc'),
      icon: 'bi-clipboard-check',
      link: '/evaluations',
      variant: 'success',
    },
    {
      title: t('dashboard.user.actions.myTrainings'),
      description: t('dashboard.user.actions.myTrainingsDesc'),
      icon: 'bi-mortarboard',
      link: '/trainings',
      variant: 'info',
    },
    {
      title: t('dashboard.user.actions.goals'),
      description: t('dashboard.user.actions.goalsDesc'),
      icon: 'bi-target',
      link: '/not-implemented',
      variant: 'warning',
    },
  ];

  return (
    <div className="user-dashboard">
      {/* Personal Statistics */}
      <Row className="mb-4">
        <Col md={3}>
          <StatsCard
            title={t('dashboard.user.stats.completedTrainings')}
            value={stats.personal?.completedTrainings || 0}
            icon="bi-mortarboard"
            variant="success"
            trend="+12.5%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.user.stats.pendingEvaluations')}
            value={stats.personal?.pendingEvaluations || 0}
            icon="bi-clock"
            variant="warning"
            trend="-25.0%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.user.stats.performanceScore')}
            value={`${stats.personal?.performanceScore || 0}%`}
            icon="bi-trophy"
            variant="info"
            trend="+8.3%"
            trendPositive={true}
          />
        </Col>
        <Col md={3}>
          <StatsCard
            title={t('dashboard.user.stats.goalsAchieved')}
            value={`${stats.personal?.goalsAchieved || 0}/${
              stats.personal?.totalGoals || 0
            }`}
            icon="bi-target"
            variant="primary"
            trend="+15.2%"
            trendPositive={true}
          />
        </Col>
      </Row>

      {/* Quick Actions and Charts */}
      <Row className="mb-4">
        <Col md={8}>
          <QuickActions
            title={t('dashboard.user.quickActions')}
            actions={userQuickActions}
          />
        </Col>
        <Col md={4}>
          <UserActivityOverviewChart stats={stats} loading={loading} />
        </Col>
      </Row>

      {/* Additional Charts */}
      <Row className="mb-4">
        <Col md={6}>
          <UserTrainingProgressChart stats={stats} loading={loading} />
        </Col>
        <Col md={6}>
          <UserPerformanceTrendChart stats={stats} loading={loading} />
        </Col>
      </Row>
    </div>
  );
};

UserDashboard.propTypes = {
  user: PropTypes.shape({
    id: PropTypes.number,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    role: PropTypes.string,
  }).isRequired,
};

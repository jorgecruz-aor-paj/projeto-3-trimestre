import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Row, Col, Alert, Spinner, Card } from 'react-bootstrap';
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

  // Helper function to calculate performance trend from historical data
  const calculatePerformanceTrend = () => {
    const performanceHistory = stats.personal?.performanceHistory || [];
    if (performanceHistory.length < 2) return null;

    const latest =
      performanceHistory[performanceHistory.length - 1]?.score || 0;
    const previous =
      performanceHistory[performanceHistory.length - 2]?.score || 0;

    if (previous === 0) return null;

    const change = ((latest - previous) / previous) * 100;
    return {
      value: `${change > 0 ? '+' : ''}${change.toFixed(1)}%`,
      positive: change >= 0,
    };
  };

  // Helper function to calculate training trend from monthly data
  const calculateTrainingTrend = () => {
    const trainingHistory = stats.personal?.trainingHistory || [];
    if (trainingHistory.length < 2) return null;

    // Get last 2 months with data
    const recentMonths = trainingHistory
      .filter(month => month.minutes > 0)
      .slice(-2);

    if (recentMonths.length < 2) return null;

    const latest = recentMonths[recentMonths.length - 1]?.minutes || 0;
    const previous = recentMonths[recentMonths.length - 2]?.minutes || 0;

    if (previous === 0) return null;

    const change = ((latest - previous) / previous) * 100;
    return {
      value: `${change > 0 ? '+' : ''}${change.toFixed(1)}%`,
      positive: change >= 0,
    };
  };

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
      icon: 'bi-bullseye',
      link: '/not-implemented',
      variant: 'warning',
    },
  ];

  // Calculate trends from real data
  const performanceTrend = calculatePerformanceTrend();
  const trainingTrend = calculateTrainingTrend();

  return (
    <div className="user-dashboard">
      {/* Personal Statistics */}
      <Card className="mb-4">
        <Card.Header>
          <h5 className="mb-0">
            <i className="bi bi-person me-2"></i>
            {t('dashboard.user.personalStats')}
          </h5>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.completedTrainings')}
                value={stats.personal?.completedTrainings || 0}
                icon="bi-mortarboard"
                variant="success"
                trend={trainingTrend?.value}
                trendPositive={trainingTrend?.positive}
              />
            </Col>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.pendingEvaluations')}
                value={stats.personal?.pendingEvaluations || 0}
                icon="bi-clock"
                variant="warning"
              />
            </Col>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.performanceScore')}
                value={`${(stats.personal?.performanceScore || 0).toFixed(
                  1
                )}/4`}
                icon="bi-trophy"
                variant="info"
                trend={performanceTrend?.value}
                trendPositive={performanceTrend?.positive}
              />
            </Col>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.goalsAchieved')}
                value={`${stats.personal?.goalsAchieved || 0}/${
                  stats.personal?.totalGoals || 0
                }`}
                icon="bi-bullseye"
                variant="primary"
              />
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {/* Quick Actions and Charts */}
      <Card className="mb-4">
        <Card.Header>
          <h5 className="mb-0">
            <i className="bi bi-lightning me-2"></i>
            {t('dashboard.user.quickActionsAndActivity')}
          </h5>
        </Card.Header>
        <Card.Body>
          <Row>
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
        </Card.Body>
      </Card>

      {/* Analytics Charts */}
      <Card className="mb-4">
        <Card.Header>
          <h5 className="mb-0">
            <i className="bi bi-graph-up me-2"></i>
            {t('dashboard.user.performanceAnalytics')}
          </h5>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md={6}>
              <UserTrainingProgressChart stats={stats} loading={loading} />
            </Col>
            <Col md={6}>
              <UserPerformanceTrendChart stats={stats} loading={loading} />
            </Col>
          </Row>
        </Card.Body>
      </Card>
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

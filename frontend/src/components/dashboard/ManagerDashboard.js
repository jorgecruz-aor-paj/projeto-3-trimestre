import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Row, Col, Alert, Card } from 'react-bootstrap';
import { StatsCard } from './StatsCard';
import { QuickActions } from './QuickActions';
import { TeamPerformanceChart } from './TeamPerformanceChart';
import { useAuthStore } from 'stores/AuthStore';
import { useManagerStats } from 'hooks/useManagerStats';
import { statsService } from 'services/statsService';

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

  // Personal statistics state
  const [personalStats, setPersonalStats] = useState({
    personal: null,
  });
  const [personalLoading, setPersonalLoading] = useState(true);
  const [personalError, setPersonalError] = useState(null);

  // Fetch personal statistics
  useEffect(() => {
    const fetchPersonalStats = async () => {
      if (!user?.id) return;

      try {
        setPersonalLoading(true);
        setPersonalError(null);

        const personal = await statsService.getPersonalStats(user.id);
        setPersonalStats({ personal });
      } catch (err) {
        console.error('Failed to fetch personal stats:', err);
        setPersonalError(t('dashboard.user.failedToLoadStats'));
      } finally {
        setPersonalLoading(false);
      }
    };

    fetchPersonalStats();
  }, [t, user?.id]);

  // Helper function to calculate performance trend from historical data
  const calculatePerformanceTrend = () => {
    const performanceHistory = personalStats.personal?.performanceHistory || [];
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
    const trainingHistory = personalStats.personal?.trainingHistory || [];
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

  // Calculate trends from real data
  const performanceTrend = calculatePerformanceTrend();
  const trainingTrend = calculateTrainingTrend();

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

      {/* Personal Error display */}
      {personalError && (
        <Row className="mb-3">
          <Col>
            <Alert variant="danger">
              <Alert.Heading>{t('dashboard.user.errorTitle')}</Alert.Heading>
              <p>{personalError}</p>
            </Alert>
          </Col>
        </Row>
      )}

      {/* Personal Statistics Section */}
      <Card className="mb-4">
        <Card.Header>
          <h5 className="mb-0">
            <i className="bi bi-person me-2"></i>
            {t('dashboard.manager.personalStats')}
          </h5>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.completedTrainings')}
                value={
                  personalLoading
                    ? '...'
                    : personalStats.personal?.completedTrainings || 0
                }
                icon="bi-mortarboard"
                variant="success"
                trend={trainingTrend?.value}
                trendPositive={trainingTrend?.positive}
              />
            </Col>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.pendingEvaluations')}
                value={
                  personalLoading
                    ? '...'
                    : personalStats.personal?.pendingEvaluations || 0
                }
                icon="bi-clock"
                variant="warning"
              />
            </Col>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.performanceScore')}
                value={
                  personalLoading
                    ? '...'
                    : `${(
                        personalStats.personal?.performanceScore || 0
                      ).toFixed(1)}/4`
                }
                icon="bi-trophy"
                variant="info"
                trend={performanceTrend?.value}
                trendPositive={performanceTrend?.positive}
              />
            </Col>
            <Col md={3}>
              <StatsCard
                title={t('dashboard.user.stats.goalsAchieved')}
                value={
                  personalLoading
                    ? '...'
                    : `${personalStats.personal?.goalsAchieved || 0}/${
                        personalStats.personal?.totalGoals || 0
                      }`
                }
                icon="bi-bullseye"
                variant="primary"
              />
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {/* Team Statistics Section */}
      <Card className="mb-4">
        <Card.Header>
          <h5 className="mb-0">
            <i className="bi bi-people me-2"></i>
            {t('dashboard.manager.teamStats')}
          </h5>
        </Card.Header>
        <Card.Body>
          <Row>
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
        </Card.Body>
      </Card>

      {/* Quick Actions and Chart Section */}
      <Card className="mb-4">
        <Card.Header>
          <h5 className="mb-0">
            <i className="bi bi-lightning me-2"></i>
            {t('dashboard.manager.quickActions')} & Team Performance
          </h5>
        </Card.Header>
        <Card.Body>
          <Row>
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
        </Card.Body>
      </Card>
    </div>
  );
};

// TODO: Add PropTypes back when user prop is needed for backend integration

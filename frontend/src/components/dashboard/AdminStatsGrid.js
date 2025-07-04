import React, { useEffect, useState } from 'react';
import { Row, Col, Spinner, Alert } from 'react-bootstrap';
import { StatsCard } from './StatsCard';
import { statsService } from 'services/statsService';
import { useTranslation } from 'react-i18next';

export const AdminStatsGrid = () => {
  const { t } = useTranslation();
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        setError(null);
        const [managerStats, evaluationStats] = await Promise.all([
          statsService.getManagerStats(),
          statsService.getEvaluationStats(),
        ]);
        setStats({
          managers: managerStats,
          evaluations: evaluationStats,
        });
      } catch (err) {
        setError(t('dashboard.admin.failedToLoadStats'));
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, [t]);

  // Calculate completed evaluations if not provided
  const completedEvaluations =
    typeof stats.evaluations?.completed === 'number'
      ? stats.evaluations.completed
      : typeof stats.evaluations?.total === 'number' &&
        typeof stats.evaluations?.ongoing === 'number'
      ? stats.evaluations.total - stats.evaluations.ongoing
      : 0;

  if (loading) {
    return (
      <div className="text-center py-3">
        <Spinner animation="border" variant="primary" className="mb-2" />
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

  return (
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
            ((stats.managers?.unmanaged || 0) / (stats.managers?.total || 1)) *
            100
          ).toFixed(1)}%`}
          trendPositive={false}
        />
      </Col>
      <Col md={3}>
        <StatsCard
          title={t('dashboard.admin.stats.completedEvaluations')}
          value={`${completedEvaluations}/${stats.evaluations?.total ?? 0}`}
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
  );
};

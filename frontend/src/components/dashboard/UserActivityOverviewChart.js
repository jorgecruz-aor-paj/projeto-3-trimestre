import React from 'react';
import PropTypes from 'prop-types';
import { Card, Row, Col } from 'react-bootstrap';
import { Doughnut } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { useTranslation } from 'react-i18next';

// Register Chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

/**
 * User Activity Overview Chart Component
 * Displays user activity overview in a donut chart format
 * @param {Object} props
 * @param {Object} props.stats - Statistics object
 * @param {boolean} props.loading - Loading state
 * @returns {React.Component} Activity overview chart
 */
export const UserActivityOverviewChart = ({ stats, loading }) => {
  const { t } = useTranslation();

  if (loading) {
    return (
      <Card className="h-100 dashboard-chart-card">
        <Card.Header>
          <h6 className="mb-0">
            <i className="bi bi-pie-chart me-2"></i>
            {t('dashboard.user.charts.activityOverview')}
          </h6>
        </Card.Header>
        <Card.Body className="d-flex align-items-center justify-content-center">
          <div className="text-muted">{t('common.loading')}</div>
        </Card.Body>
      </Card>
    );
  }

  const completedTrainings = stats.personal?.completedTrainings || 0;
  const pendingEvaluations = stats.personal?.pendingEvaluations || 0;
  const goalsAchieved = stats.personal?.goalsAchieved || 0;

  const chartData = {
    labels: [
      t('dashboard.user.charts.completedTrainings'),
      t('dashboard.user.charts.pendingEvaluations'),
      t('dashboard.user.charts.achievedGoals'),
    ],
    datasets: [
      {
        data: [completedTrainings, pendingEvaluations, goalsAchieved],
        backgroundColor: [
          '#28a745', // Success green for completed trainings
          '#ffc107', // Warning yellow for pending evaluations
          '#007bff', // Primary blue for achieved goals
        ],
        borderWidth: 2,
        borderColor: '#fff',
        hoverBorderWidth: 3,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 10,
          font: {
            size: 11,
          },
          boxWidth: 12,
          usePointStyle: true,
        },
      },
      tooltip: {
        callbacks: {
          label: function (context) {
            const label = context.label || '';
            const value = context.parsed || 0;
            return `${label}: ${value}`;
          },
        },
      },
    },
    cutout: '60%',
  };

  const totalActivity = completedTrainings + pendingEvaluations + goalsAchieved;

  return (
    <Card className="h-100 dashboard-chart-card">
      <Card.Header>
        <h6 className="mb-0">
          <i className="bi bi-pie-chart me-2"></i>
          {t('dashboard.user.charts.activityOverview')}
        </h6>
      </Card.Header>
      <Card.Body>
        <div style={{ height: '180px', position: 'relative' }}>
          <Doughnut data={chartData} options={chartOptions} />
          <div
            style={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              textAlign: 'center',
            }}
          >
            <div
              className="fw-bold text-primary"
              style={{ fontSize: '1.5rem' }}
            >
              {totalActivity}
            </div>
            <small className="text-muted">
              {t('dashboard.user.charts.totalActivities')}
            </small>
          </div>
        </div>

        {/* Quick stats */}
        <Row className="mt-3 text-center">
          <Col xs={4}>
            <div className="border-end">
              <div className="fw-semibold text-success">
                {completedTrainings}
              </div>
              <small className="text-muted d-block">
                {t('dashboard.user.charts.trainings')}
              </small>
            </div>
          </Col>
          <Col xs={4}>
            <div className="border-end">
              <div className="fw-semibold text-warning">
                {pendingEvaluations}
              </div>
              <small className="text-muted d-block">
                {t('dashboard.user.charts.evaluations')}
              </small>
            </div>
          </Col>
          <Col xs={4}>
            <div>
              <div className="fw-semibold text-primary">{goalsAchieved}</div>
              <small className="text-muted d-block">
                {t('dashboard.user.charts.goals')}
              </small>
            </div>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

UserActivityOverviewChart.propTypes = {
  stats: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
};

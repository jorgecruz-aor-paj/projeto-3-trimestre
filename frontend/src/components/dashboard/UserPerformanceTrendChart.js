import React from 'react';
import PropTypes from 'prop-types';
import { Card, ProgressBar } from 'react-bootstrap';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { useTranslation } from 'react-i18next';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

/**
 * User Performance Trend Chart Component
 * Displays user performance evaluation trend over time
 * @param {Object} props
 * @param {Object} props.stats - Statistics object
 * @param {boolean} props.loading - Loading state
 * @returns {React.Component} Performance trend chart
 */
export const UserPerformanceTrendChart = ({ stats, loading }) => {
  const { t } = useTranslation();

  if (loading) {
    return (
      <Card className="h-100 dashboard-chart-card">
        <Card.Header>
          <h6 className="mb-0">
            <i className="bi bi-graph-up me-2"></i>
            {t('dashboard.user.charts.performanceTrend')}
          </h6>
        </Card.Header>
        <Card.Body className="d-flex align-items-center justify-content-center">
          <div className="text-muted">{t('common.loading')}</div>
        </Card.Body>
      </Card>
    );
  }

  // Simulate performance trend data - in a real app this would come from backend
  const performanceHistory = stats.performanceHistory || [
    { period: 'Q1 2024', score: 3.2 },
    { period: 'Q2 2024', score: 3.5 },
    { period: 'Q3 2024', score: 3.7 },
    { period: 'Q4 2024', score: 3.8 },
  ];

  const labels = performanceHistory.map(item => item.period);
  const scores = performanceHistory.map(item => item.score);

  const chartData = {
    labels,
    datasets: [
      {
        label: t('dashboard.user.charts.performanceScore'),
        data: scores,
        borderColor: 'rgba(0, 123, 255, 1)',
        backgroundColor: 'rgba(0, 123, 255, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        pointBackgroundColor: 'rgba(0, 123, 255, 1)',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 6,
        pointHoverRadius: 8,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        callbacks: {
          label: function (context) {
            return `${t('dashboard.user.charts.score')}: ${context.parsed.y}/4`;
          },
        },
      },
    },
    scales: {
      y: {
        beginAtZero: false,
        min: 1,
        max: 4,
        ticks: {
          stepSize: 0.5,
          callback: function (value) {
            return `${value}/4`;
          },
        },
      },
      x: {
        grid: {
          display: false,
        },
      },
    },
  };

  const currentScore = stats.personal?.performanceScore || 0;
  const progressPercentage = (currentScore / 4) * 100;

  return (
    <Card className="h-100 dashboard-chart-card">
      <Card.Header>
        <h6 className="mb-0">
          <i className="bi bi-graph-up me-2"></i>
          {t('dashboard.user.charts.performanceTrend')}
        </h6>
      </Card.Header>
      <Card.Body>
        <div style={{ height: '160px' }}>
          <Line data={chartData} options={chartOptions} />
        </div>
        <div className="mt-3">
          <div className="d-flex justify-content-between align-items-center mb-2">
            <small className="text-muted">
              {t('dashboard.user.charts.currentScore')}
            </small>
            <small className="fw-bold">{currentScore}/4</small>
          </div>
          <ProgressBar
            now={progressPercentage}
            variant={
              progressPercentage >= 75
                ? 'success'
                : progressPercentage >= 50
                ? 'info'
                : progressPercentage >= 25
                ? 'warning'
                : 'danger'
            }
            style={{ height: '8px' }}
          />
        </div>
      </Card.Body>
    </Card>
  );
};

UserPerformanceTrendChart.propTypes = {
  stats: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
};

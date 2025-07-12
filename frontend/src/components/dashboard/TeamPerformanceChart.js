import React from 'react';
import PropTypes from 'prop-types';
import { Card } from 'react-bootstrap';
import { Doughnut } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { useTranslation } from 'react-i18next';

// Register Chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

/**
 * Team Performance Chart Component
 * Displays team performance metrics in a donut chart
 * @param {Object} props
 * @param {Object} props.stats - Statistics object
 * @param {boolean} props.loading - Loading state
 * @returns {React.Component} Performance chart
 */
export const TeamPerformanceChart = ({ stats, loading }) => {
  const { t } = useTranslation();

  if (loading) {
    return (
      <Card className="h-100">
        <Card.Header>
          <h6 className="mb-0">{t('dashboard.manager.charts.teamOverview')}</h6>
        </Card.Header>
        <Card.Body className="d-flex align-items-center justify-content-center">
          <div className="text-muted">{t('common.loading')}</div>
        </Card.Body>
      </Card>
    );
  }

  const chartData = {
    labels: [
      t('dashboard.manager.charts.completedEvaluations'),
      t('dashboard.manager.charts.pendingEvaluations'),
      t('dashboard.manager.charts.teamMembers'),
    ],
    datasets: [
      {
        data: [
          stats.completedEvaluations || 0,
          stats.pendingEvaluations || 0,
          stats.teamMembers || 0,
        ],
        backgroundColor: [
          '#28a745', // Success green for completed
          '#ffc107', // Warning yellow for pending
          '#007bff', // Primary blue for team members
        ],
        borderWidth: 2,
        borderColor: '#fff',
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
          padding: 15,
          font: {
            size: 12,
          },
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
  };

  return (
    <Card className="h-100">
      <Card.Header>
        <h6 className="mb-0">{t('dashboard.manager.charts.teamOverview')}</h6>
      </Card.Header>
      <Card.Body>
        <div style={{ height: '200px' }}>
          <Doughnut data={chartData} options={chartOptions} />
        </div>
        {stats.avgPerformanceRating > 0 && (
          <div className="text-center mt-3">
            <small className="text-muted">
              {t('dashboard.manager.charts.avgPerformance')}:{' '}
              <strong>{stats.avgPerformanceRating.toFixed(1)}/4</strong>
            </small>
          </div>
        )}
      </Card.Body>
    </Card>
  );
};

TeamPerformanceChart.propTypes = {
  stats: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
};

import React from 'react';
import PropTypes from 'prop-types';
import { Card } from 'react-bootstrap';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { useTranslation } from 'react-i18next';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

/**
 * User Training Progress Chart Component
 * Displays user training progress over time in a bar chart
 * @param {Object} props
 * @param {Object} props.stats - Statistics object
 * @param {boolean} props.loading - Loading state
 * @returns {React.Component} Training progress chart
 */
export const UserTrainingProgressChart = ({ stats, loading }) => {
  const { t } = useTranslation();

  if (loading) {
    return (
      <Card className="h-100 dashboard-chart-card">
        <Card.Header>
          <h6 className="mb-0">
            <i className="bi bi-mortarboard me-2"></i>
            {t('dashboard.user.charts.trainingProgress')}
          </h6>
        </Card.Header>
        <Card.Body className="d-flex align-items-center justify-content-center">
          <div className="text-muted">{t('common.loading')}</div>
        </Card.Body>
      </Card>
    );
  }

  // Prepare data for the last 6 months
  const months = [];
  const trainingHours = [];
  const currentDate = new Date();

  for (let i = 5; i >= 0; i--) {
    const date = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth() - i,
      1
    );
    months.push(
      date.toLocaleDateString('pt-PT', { month: 'short', year: '2-digit' })
    );

    // Simulate training data - in a real app this would come from the backend
    const monthData = stats.trainingHistory?.find(
      h => h.year === date.getFullYear() && h.month === date.getMonth() + 1
    );
    trainingHours.push(
      monthData ? Math.round((monthData.minutes / 60) * 10) / 10 : 0
    );
  }

  const chartData = {
    labels: months,
    datasets: [
      {
        label: t('dashboard.user.charts.hoursCompleted'),
        data: trainingHours,
        backgroundColor: 'rgba(40, 167, 69, 0.7)',
        borderColor: 'rgba(40, 167, 69, 1)',
        borderWidth: 1,
        borderRadius: 4,
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
            return `${context.parsed.y}h ${t(
              'dashboard.user.charts.completed'
            )}`;
          },
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          callback: function (value) {
            return `${value}h`;
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

  const totalHours = trainingHours.reduce((sum, hours) => sum + hours, 0);

  return (
    <Card className="h-100 dashboard-chart-card">
      <Card.Header>
        <h6 className="mb-0">
          <i className="bi bi-mortarboard me-2"></i>
          {t('dashboard.user.charts.trainingProgress')}
        </h6>
      </Card.Header>
      <Card.Body>
        <div style={{ height: '200px' }}>
          <Bar data={chartData} options={chartOptions} />
        </div>
        <div className="text-center mt-3">
          <small className="text-muted">
            {t('dashboard.user.charts.totalThisYear')}:{' '}
            <strong>{totalHours}h</strong>
          </small>
        </div>
      </Card.Body>
    </Card>
  );
};

UserTrainingProgressChart.propTypes = {
  stats: PropTypes.object.isRequired,
  loading: PropTypes.bool.isRequired,
};

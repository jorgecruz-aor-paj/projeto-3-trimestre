import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { statsService } from 'services/statsService';

export const TrainingParticipationStatsTable = ({ userId }) => {
  const { t } = useTranslation();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      setLoading(true);
      setError(null);
      try {
        const stats = await statsService.getParticipationStats(userId);
        setStats(stats);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, [userId]);

  if (loading)
    return <div className="mb-2">{t('common.loading') || 'Loading...'}</div>;
  if (error)
    return (
      <div className="mb-2 text-danger">{t('common.error') || 'Error'}</div>
    );
  if (!stats || !Array.isArray(stats) || stats.length === 0) return null;

  // Helper to format minutes as "Xh Ym"
  const formatMinutes = mins => {
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    if (h > 0 && m > 0) return `${h}h ${m}m`;
    if (h > 0) return `${h}h`;
    return `${m}m`;
  };

  return (
    <div className="card h-100 shadow-sm">
      <div className="card-header bg-transparent border-bottom">
        <h6 className="mb-0">
          <i className="bi bi-bar-chart me-2"></i>
          {t('profile.trainingStats', 'Training Statistics')}
        </h6>
      </div>
      <div className="card-body p-0">
        <div className="table-responsive">
          <table className="table table-hover mb-0">
            <thead className="table-light">
              <tr>
                <th className="border-0 fw-semibold">
                  {t('time.year', 'Year')}
                </th>
                <th className="border-0 fw-semibold">
                  {t('trainings.duration', 'Duration')}
                </th>
              </tr>
            </thead>
            <tbody>
              {stats.map(({ year, minutes }) => (
                <tr key={year}>
                  <td className="border-0 py-3">
                    <span className="fw-medium">{year}</span>
                  </td>
                  <td className="border-0 py-3">
                    <span className="badge bg-primary rounded-pill">
                      {formatMinutes(minutes)}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

TrainingParticipationStatsTable.propTypes = {
  userId: PropTypes.number.isRequired,
};

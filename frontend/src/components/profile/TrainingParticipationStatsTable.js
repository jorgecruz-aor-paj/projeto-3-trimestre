import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { statsService } from 'services/statsService';

const TrainingParticipationStatsTable = ({ userId }) => {
  const { t } = useTranslation();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      setLoading(true);
      setError(null);
      try {
        console.log('Fetching training participation stats for user:', userId);
        const stats = await statsService.getParticipationStats(userId);
        console.log(stats);
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
  if (!stats || Object.keys(stats).length === 0) return null;

  // Helper to format minutes as "Xh Ym"
  const formatMinutes = mins => {
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    if (h > 0 && m > 0) return `${h}h ${m}m`;
    if (h > 0) return `${h}h`;
    return `${m}m`;
  };

  // Sort years descending (most recent first)
  const sortedEntries = Object.entries(stats).sort(
    (a, b) => Number(b[0]) - Number(a[0])
  );

  return (
    <div className="training-participation-stats-table mb-3">
      <table
        className="table table-sm table-bordered mb-0"
        style={{ maxWidth: 320 }}
      >
        <thead>
          <tr>
            <th style={{ width: '60%' }}>{t('time.year') || 'Year'}</th>
            <th style={{ width: '40%' }}>
              {t('trainings.duration') || 'Minutes'}
            </th>
          </tr>
        </thead>
        <tbody>
          {sortedEntries.map(([year, minutes]) => (
            <tr key={year}>
              <td>{year}</td>
              <td>{formatMinutes(minutes)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

TrainingParticipationStatsTable.propTypes = {
  userId: PropTypes.number.isRequired,
};

export default TrainingParticipationStatsTable;

import React, { useState, useEffect } from 'react';
import { useEvaluationStore } from 'stores/EvaluationStore';
import ProfileEvaluationsFilter from './ProfileEvaluationsFilter';
import ProfileEvaluationCard from './ProfileEvaluationCard';
import { useTranslation } from 'react-i18next';

const ProfileEvaluationsSection = ({ userId }) => {
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();
  const [filters, setFilters] = useState({ evaluatedId: userId });

  useEffect(() => {
    fetchEvaluations(filters);
    // eslint-disable-next-line
  }, [JSON.stringify(filters)]);

  const { t } = useTranslation();
  return (
    <div className="profile-evaluations-section">
      <ProfileEvaluationsFilter filters={filters} setFilters={setFilters} />
      <div className="profile-evaluations-list">
        {loading && <div>{t('common.loading') || 'Loading...'}</div>}
        {error && <div>{t('common.error') || 'Error loading evaluations'}</div>}
        {evaluations && evaluations.length > 0
          ? evaluations.map(ev => (
              <ProfileEvaluationCard key={ev.id} evaluation={ev} />
            ))
          : !loading && <div>{t('profile.noEvaluations')}</div>}
      </div>
    </div>
  );
};

export default ProfileEvaluationsSection;

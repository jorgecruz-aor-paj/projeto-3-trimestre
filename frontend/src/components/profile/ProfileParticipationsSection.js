import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useParticipationStore } from 'stores/ParticipationStore';
import ProfileParticipationsFilter from './ProfileParticipationsFilter';
import ProfileParticipationCard from './ProfileParticipationCard';
import { useTranslation } from 'react-i18next';

const ProfileParticipationsSection = ({ userId }) => {
  const { participations, fetchParticipations, loading, error } =
    useParticipationStore();
  const [filters, setFilters] = useState({ participantId: userId });

  useEffect(() => {
    fetchParticipations(filters);
  }, [JSON.stringify(filters)]);

  const { t } = useTranslation();
  return (
    <div className="profile-participations-section">
      <ProfileParticipationsFilter filters={filters} setFilters={setFilters} />
      <div className="profile-participations-list">
        {loading && <div>{t('common.loading') || 'Loading...'}</div>}
        {error && (
          <div>{t('common.error') || 'Error loading participations'}</div>
        )}
        {participations && participations.length > 0
          ? participations.map(part => (
              <ProfileParticipationCard key={part.id} participation={part} />
            ))
          : !loading && <div>{t('profile.noParticipations')}</div>}
      </div>
    </div>
  );
};

ProfileParticipationsSection.propTypes = {
  userId: PropTypes.number.isRequired,
};

export default ProfileParticipationsSection;

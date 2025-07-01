import React, { useState } from 'react';
import ProfileEvaluationsSection from '../profile/ProfileEvaluationsSection';
import ProfileParticipationsSection from '../profile/ProfileParticipationsSection';
import { useTranslation } from 'react-i18next';

export const ProfileSectionsToggle = ({ userId }) => {
  const [showEvaluations, setShowEvaluations] = useState(true);
  const { t } = useTranslation();

  return (
    <section className="profile-toggle-section">
      <div className="profile-toggle-buttons">
        <button
          className={showEvaluations ? 'active' : ''}
          onClick={() => setShowEvaluations(true)}
        >
          {t('profile.evaluations')}
        </button>
        <button
          className={!showEvaluations ? 'active' : ''}
          onClick={() => setShowEvaluations(false)}
        >
          {t('profile.trainings')}
        </button>
      </div>
      <div className="profile-toggle-content">
        {showEvaluations ? (
          <ProfileEvaluationsSection userId={userId} />
        ) : (
          <ProfileParticipationsSection userId={userId} />
        )}
      </div>
    </section>
  );
};

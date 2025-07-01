import React, { useEffect, useState } from 'react';
import { UserProfileInfo } from '../components/users/UserProfileInfo';
import ProfileEvaluationsSection from '../components/profile/ProfileEvaluationsSection';
import ProfileParticipationsSection from '../components/profile/ProfileParticipationsSection';
import { useAuthStore } from '../stores/AuthStore';
import { useTranslation } from 'react-i18next';
import '../styles/pages/ProfilePage.css';

const ProfilePage = () => {
  const [showEvaluations, setShowEvaluations] = useState(true);
  const user = useAuthStore(state => state.user);
  const { t } = useTranslation();

  // No need to fetch here, handled in section components
  return (
    <div className="profile-page-container">
      <section className="profile-info-section">
        <UserProfileInfo user={user} />
      </section>
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
            <ProfileEvaluationsSection userId={user.id} />
          ) : (
            <ProfileParticipationsSection userId={user.id} />
          )}
        </div>
      </section>
    </div>
  );
};

export default ProfilePage;

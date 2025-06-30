import React, { useEffect, useState } from 'react';
import { UserProfileInfo } from '../components/users/UserProfileInfo';
import ProfileEvaluationsSection from '../components/profile/ProfileEvaluationsSection';
import ProfileParticipationsSection from '../components/profile/ProfileParticipationsSection';
import { useAuthStore } from '../stores/AuthStore';
import '../styles/pages/ProfilePage.css';

const ProfilePage = () => {
  const [showEvaluations, setShowEvaluations] = useState(true);
  const user = useAuthStore(state => state.user);
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
            Evaluations
          </button>
          <button
            className={!showEvaluations ? 'active' : ''}
            onClick={() => setShowEvaluations(false)}
          >
            Trainings
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

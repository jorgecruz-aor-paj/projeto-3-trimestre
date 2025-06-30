import React, { useEffect, useState } from 'react';
import { UserProfileInfo } from '../components/users/UserProfileInfo';
import { UserTrainings } from '../components/users/UserTrainings';
import { UserEvaluations } from '../components/users/UserEvaluations';
import { useAuthStore } from '../stores/AuthStore';

const ProfilePage = () => {
  const [showEvaluations, setShowEvaluations] = useState(true);
  const user = useAuthStore(state => state.user);

  useEffect(() => {
    // Fetch user data or perform any necessary side effects here
  }, []);

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
          {showEvaluations ? <UserEvaluations /> : <UserTrainings />}
        </div>
      </section>
    </div>
  );
};

export default ProfilePage;

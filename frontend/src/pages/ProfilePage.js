import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { UserProfileInfo } from '../components/users/UserProfileInfo';
import ProfileEvaluationsSection from '../components/profile/ProfileEvaluationsSection';
import ProfileParticipationsSection from '../components/profile/ProfileParticipationsSection';
import { useAuthStore } from '../stores/AuthStore';
import { useTranslation } from 'react-i18next';
import '../styles/pages/ProfilePage.css';
import { userService } from 'services';

const ProfilePage = () => {
  const [showEvaluations, setShowEvaluations] = useState(true);
  const [profileUser, setProfileUser] = useState(null);
  const user = useAuthStore(state => state.user);
  const { t } = useTranslation();
  const location = useLocation();

  // Helper to get query param
  function getUserIdFromQuery() {
    const params = new URLSearchParams(location.search);
    const id = params.get('id');
    return id ? parseInt(id, 10) : null;
  }

  useEffect(() => {
    const fetchData = async () => {
      const paramId = getUserIdFromQuery();
      console.log(paramId);
      if (!paramId || (user && paramId === user.id)) {
        setProfileUser(user);
      } else {
        try {
          const params = { id: paramId };
        const response = await userService.getAll(params);
        if (!response || response.length === 0) {
        //TODO invalid user, handle accordingly
          console.error('User not found');
          setProfileUser(user);
        }
        setProfileUser(response[0]);
      } catch (err) {
        setError(err.message || t('auth.loginFailed'));
      }
    };
  }
    fetchData();
  }, []);

  if (!profileUser) return null; // Or a loading spinner
console.log('Profile User:', profileUser);
console.log(profileUser.workplace);
  return (
    <div className="profile-page-container">
      <section className="profile-info-section">
        <UserProfileInfo user={profileUser} />
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
            <ProfileEvaluationsSection userId={profileUser.id} />
          ) : (
            <ProfileParticipationsSection userId={profileUser.id} />
          )}
        </div>
      </section>
    </div>
  );
};

export default ProfilePage;

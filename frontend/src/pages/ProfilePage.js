import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { UserProfileInfo } from 'components/users/UserProfileInfo';
import { ProfileSectionsToggle } from 'components/profile/ProfileSectionsToggle';
import { useAuthStore } from 'stores/AuthStore';
import { useTranslation } from 'react-i18next';
import 'styles/pages/ProfilePage.css';
import { userService } from 'services';

const ProfilePage = () => {
  // const [showEvaluations, setShowEvaluations] = useState(true); // moved to ProfileSectionsToggle
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
          console.error(
            'Failed to fetch user:',
            err.message || t('auth.loginFailed')
          );
          setProfileUser(user);
        }
      }
    };
    fetchData();
  }, []);

  if (!profileUser) return null; // Or a loading spinner
  // console.log('Profile User:', profileUser);
  // console.log(profileUser.workplace);
  // Permission logic: admin, self, or manager of profileUser
  const isAdmin = user?.role === 'ADMIN';
  const isSelf = user?.id === profileUser.id;
  // Assuming profileUser.managerId or profileUser.manager?.id is available
  const isManager = profileUser.managerId
    ? user?.id === profileUser.managerId
    : profileUser.manager && user?.id === profileUser.manager.id;

  const canViewSections = isAdmin || isSelf || isManager;

  return (
    <div className="profile-page-container">
      <section className="profile-info-section">
        <UserProfileInfo user={profileUser} />
      </section>
      {canViewSections && <ProfileSectionsToggle userId={profileUser.id} />}
    </div>
  );
};

export default ProfilePage;

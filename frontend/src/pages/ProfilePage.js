import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';
import { UserProfileInfo } from 'components/users/UserProfileInfo';
import { ProfileSectionsToggle } from 'components/profile/ProfileSectionsToggle';
import { useAuthStore } from 'stores/AuthStore';
import { useTranslation } from 'react-i18next';
import 'styles/pages/ProfilePage.css';
import { userService } from 'services';

/**
 * Main Profile page component
 * Shows user profile with evaluations and trainings sections
 * - /profile shows authenticated user's own profile
 * - /profile?userId=12 shows another user's profile (if permissions allow)
 * @returns {React.Component} Profile page
 */
export const ProfilePage = () => {
  const [profileUser, setProfileUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const user = useAuthStore(state => state.user);
  const { t } = useTranslation();
  const location = useLocation();

  // Helper to get query param
  function getUserIdFromQuery() {
    const params = new URLSearchParams(location.search);
    const id = params.get('userId');
    return id ? parseInt(id, 10) : null;
  }

  const handleUserUpdate = updatedUser => {
    setProfileUser(updatedUser);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const paramId = getUserIdFromQuery();

        if (!paramId || (user && paramId === user.id)) {
          // Show logged user's profile
          setProfileUser(user);
        } else {
          // Fetch another user's profile
          const params = { id: paramId };
          const response = await userService.getAll(params);
          if (!response || response.length === 0) {
            // Fallback: invalid user param, show logged user
            console.error('User not found');
            setProfileUser(user);
            return;
          }
          setProfileUser(response[0]);
        }
      } catch (err) {
        console.error(
          'Failed to fetch user:',
          err.message || t('auth.loginFailed')
        );
        setProfileUser(user);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [user, location.search]);

  // Permission logic: admin, self, or manager of profileUser
  const isAdmin = user?.role === 'ADMIN';
  const isSelf = user?.id === profileUser?.id;
  const isManager = profileUser?.managerId
    ? user?.id === profileUser.managerId
    : profileUser?.manager && user?.id === profileUser.manager.id;

  const canViewSections = isAdmin || isSelf || isManager;

  if (loading) {
    return (
      <div className="profile-page">
        <Container className="py-4">
          <div
            className="profile-page-spinner-container"
            style={{
              minHeight: 200,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <div
              className="spinner-border text-primary"
              role="status"
              aria-label="Loading"
            >
              <span className="visually-hidden">{t('common.loading')}</span>
            </div>
          </div>
        </Container>
      </div>
    );
  }

  if (!profileUser) {
    return (
      <div className="profile-page">
        <Container className="py-4">
          <div className="text-center">
            <h3>{t('profile.userNotFound', 'User not found')}</h3>
            <p className="text-muted">
              {t(
                'profile.userNotFoundDesc',
                'The requested user profile could not be found.'
              )}
            </p>
          </div>
        </Container>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <Container className="py-4">
        {/* Header section */}
        <Row className="mb-4">
          <Col>
            <div className="page-header">
              <h2 className="mb-2">
                {isSelf
                  ? t('profile.user.title', 'My Profile')
                  : (() => {
                      const titleOther = t('profile.user.titleOther', {
                        name: profileUser.firstName,
                      });
                      // Fallback to manual replacement if interpolation failed
                      return titleOther.includes('{name}')
                        ? titleOther.replace('{name}', profileUser.firstName)
                        : titleOther;
                    })()}
              </h2>
              <p className="text-muted mb-0">
                {isSelf
                  ? t(
                      'profile.user.subtitle',
                      'Manage your profile and view your professional journey'
                    )
                  : t(
                      'profile.user.subtitleOther',
                      'View professional profile and achievements'
                    )}
              </p>
            </div>
          </Col>
        </Row>

        {/* Profile information section */}
        <Row className="mb-4">
          <Col>
            <div className="profile-info-section">
              <UserProfileInfo
                user={profileUser}
                onUserUpdate={handleUserUpdate}
              />
            </div>
          </Col>
        </Row>

        {/* Profile sections (evaluations and trainings) */}
        {canViewSections && (
          <Row>
            <Col>
              <ProfileSectionsToggle userId={profileUser.id} />
            </Col>
          </Row>
        )}
      </Container>
    </div>
  );
};

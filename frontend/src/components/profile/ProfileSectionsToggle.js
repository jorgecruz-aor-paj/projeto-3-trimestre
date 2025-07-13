import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Nav, Tab } from 'react-bootstrap';
import { ProfileEvaluationsSection } from 'components/profile/ProfileEvaluationsSection';
import { ProfileParticipationsSection } from 'components/profile/ProfileParticipationsSection';
import { SecuritySettings } from 'components/profile/SecuritySettings';
import { useTranslation } from 'react-i18next';
import { useAuthStore } from 'stores/AuthStore';

export const ProfileSectionsToggle = ({ userId }) => {
  const [activeTab, setActiveTab] = useState('evaluations');
  const { t } = useTranslation();
  const loggedUser = useAuthStore(state => state.user);

  // Check if this is the current user's profile
  const isSelf = loggedUser?.id === userId;

  return (
    <div className="profile-sections-container">
      {/* Modern tab navigation */}
      <Nav variant="pills" className="mb-4" fill>
        <Nav.Item>
          <Nav.Link
            active={activeTab === 'evaluations'}
            onClick={() => setActiveTab('evaluations')}
            className="d-flex align-items-center justify-content-center"
          >
            <i className="bi bi-clipboard-check me-2"></i>
            {t('profile.evaluations')}
          </Nav.Link>
        </Nav.Item>
        <Nav.Item>
          <Nav.Link
            active={activeTab === 'trainings'}
            onClick={() => setActiveTab('trainings')}
            className="d-flex align-items-center justify-content-center"
          >
            <i className="bi bi-book me-2"></i>
            {t('profile.trainings')}
          </Nav.Link>
        </Nav.Item>
        {/* Show Security tab only for current user's own profile */}
        {isSelf && (
          <Nav.Item>
            <Nav.Link
              active={activeTab === 'security'}
              onClick={() => setActiveTab('security')}
              className="d-flex align-items-center justify-content-center"
            >
              <i className="bi bi-shield-check me-2"></i>
              {t('profile.security')}
            </Nav.Link>
          </Nav.Item>
        )}
      </Nav>

      {/* Tab content */}
      <Tab.Content>
        <Tab.Pane active={activeTab === 'evaluations'}>
          {activeTab === 'evaluations' && (
            <ProfileEvaluationsSection userId={userId} />
          )}
        </Tab.Pane>
        <Tab.Pane active={activeTab === 'trainings'}>
          {activeTab === 'trainings' && (
            <ProfileParticipationsSection userId={userId} />
          )}
        </Tab.Pane>
        {/* Security tab only for current user */}
        {isSelf && (
          <Tab.Pane active={activeTab === 'security'}>
            {activeTab === 'security' && <SecuritySettings />}
          </Tab.Pane>
        )}
      </Tab.Content>
    </div>
  );
};

ProfileSectionsToggle.propTypes = {
  userId: PropTypes.number.isRequired,
};

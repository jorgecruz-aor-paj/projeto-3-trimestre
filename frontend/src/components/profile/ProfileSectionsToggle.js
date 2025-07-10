import React, { useState } from 'react';
import { Nav, Tab } from 'react-bootstrap';
import { ProfileEvaluationsSection } from 'components/profile/ProfileEvaluationsSection';
import { ProfileParticipationsSection } from 'components/profile/ProfileParticipationsSection';
import { useTranslation } from 'react-i18next';

export const ProfileSectionsToggle = ({ userId }) => {
  const [activeTab, setActiveTab] = useState('evaluations');
  const { t } = useTranslation();

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
      </Tab.Content>
    </div>
  );
};

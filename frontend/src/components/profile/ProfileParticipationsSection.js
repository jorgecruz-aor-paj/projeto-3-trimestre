import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useParticipationStore } from 'stores/ParticipationStore';
import { ProfileParticipationsFilter } from './ProfileParticipationsFilter';
import { ProfileParticipationCard } from './ProfileParticipationCard';
import { useTranslation } from 'react-i18next';
import { TrainingParticipationStatsTable } from './TrainingParticipationStatsTable';
import { Button } from 'react-bootstrap';
import { AddParticipationModal } from './AddParticipationModal';
import { trainingParticipationService } from 'services/trainingParticipationService';
import { useAuthStore } from 'stores/AuthStore';

export const ProfileParticipationsSection = ({ userId }) => {
  const { participations, fetchParticipations, loading, error } =
    useParticipationStore();
  const [filters, setFilters] = useState({ participantId: userId });

  // Get logged-in user from auth store
  const currentUser = useAuthStore(state => state.user);

  // Modal state
  const [showModal, setShowModal] = useState(false);
  const handleOpenModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  // Trainings state
  const [trainings, setTrainings] = useState([]);
  const [trainingsLoading, setTrainingsLoading] = useState(false);
  const [trainingsError, setTrainingsError] = useState(null);

  // Only show button if user is MANAGER or ADMIN and is not viewing their own profile
  const isManagerOrAdmin =
    (currentUser.role === 'MANAGER' || currentUser.role === 'ADMIN') &&
    currentUser.id !== userId;

  // Fetch trainings user hasn't participated in
  const fetchTrainings = async () => {
    setTrainingsLoading(true);
    setTrainingsError(null);
    try {
      if (!currentUser) throw new Error('No current user');
      const response =
        await trainingParticipationService.getUnparticipatedTrainingsByUserId(
          userId
        );
      setTrainings(
        (response || []).map(t => ({ value: t.id, label: t.title, ...t }))
      );
    } catch (err) {
      setTrainingsError('Failed to load trainings');
    } finally {
      setTrainingsLoading(false);
    }
  };

  // Handler for modal form submit
  const handleAddParticipation = async participationObj => {
    try {
      // Always use correct managerId (currentUser.id) and participantId (userId)
      const participationToSend = {
        ...participationObj,
        participantId: userId,
        managerId: currentUser.id,
      };
      console.log('Adding participation:', participationToSend);
      const response = await trainingParticipationService.register(participationToSend);
      console.log('Add participation response:', response);
      fetchParticipations(filters);
      handleCloseModal();
    } catch (err) {
      console.error('Error adding participation:', err);
    }
  };

  useEffect(() => {
    fetchParticipations(filters);
  }, [JSON.stringify(filters)]);

  // Fetch trainings when modal opens
  useEffect(() => {
    if (showModal) fetchTrainings();
  }, [showModal]);

  const { t } = useTranslation();
  return (
    <div className="profile-participations-section">
      <ProfileParticipationsFilter filters={filters} setFilters={setFilters} />
      {/* Training participation stats table */}
      <TrainingParticipationStatsTable userId={userId} />
      {isManagerOrAdmin && (
        <div style={{ marginBottom: 16 }}>
          <Button variant="primary" onClick={handleOpenModal}>
            {t('profile.addParticipation') || 'Add Participation'}
          </Button>
        </div>
      )}
      <AddParticipationModal
        show={showModal}
        onHide={handleCloseModal}
        onSubmit={handleAddParticipation}
        trainings={trainings}
        trainingsLoading={trainingsLoading}
        trainingsError={trainingsError}
        participantId={userId}
        managerId={currentUser ? currentUser.id : undefined}
        t={t}
      />
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

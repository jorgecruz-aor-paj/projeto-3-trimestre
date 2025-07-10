import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Row, Col, Button, Spinner } from 'react-bootstrap';
import { useParticipationStore } from 'stores/ParticipationStore';
import { ProfileParticipationCard } from 'components/profile/ProfileParticipationCard';
import { useTranslation } from 'react-i18next';
import { TrainingParticipationStatsTable } from 'components/profile/TrainingParticipationStatsTable';
import { AddParticipationModal } from 'components/profile/AddParticipationModal';
import { trainingParticipationService } from 'services/trainingParticipationService';
import { useAuthStore } from 'stores/AuthStore';

export const ProfileParticipationsSection = ({ userId }) => {
  const { participations, fetchParticipations, loading, error } =
    useParticipationStore();
  const [filters, setFilters] = useState({ participantId: userId });
  const { t } = useTranslation();

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
      const response = await trainingParticipationService.register(
        participationToSend
      );
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

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('common.loading')}</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger">
        {t('common.error') || 'Error loading participations'}
      </div>
    );
  }

  return (
    <div className="profile-participations-section">
      {/* Training participation stats table */}
      <Row className="mb-4">
        <Col>
          <TrainingParticipationStatsTable userId={userId} />
        </Col>
      </Row>

      {/* Add participation button for managers/admins */}
      {isManagerOrAdmin && (
        <Row className="mb-4">
          <Col className="d-flex justify-content-end">
            <Button variant="primary" onClick={handleOpenModal}>
              <i className="bi bi-plus-circle me-2"></i>
              {t('profile.addParticipation')}
            </Button>
          </Col>
        </Row>
      )}

      {/* Participations list */}
      <Row>
        <Col>
          {participations && participations.length > 0 ? (
            <Row className="participation-list">
              {participations.map(participation => (
                <Col key={participation.id} md={6} lg={4} className="mb-4">
                  <div className="h-100">
                    <ProfileParticipationCard participation={participation} />
                  </div>
                </Col>
              ))}
            </Row>
          ) : (
            <div className="text-center py-5">
              <i className="bi bi-book display-4 text-muted"></i>
              <h5 className="mt-3 text-muted">
                {t('profile.noParticipations')}
              </h5>
              <p className="text-muted">
                {t(
                  'profile.noParticipationsDesc',
                  'No training participations match your current criteria'
                )}
              </p>
            </div>
          )}
        </Col>
      </Row>

      {/* Modal for adding participation */}
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
    </div>
  );
};

ProfileParticipationsSection.propTypes = {
  userId: PropTypes.number.isRequired,
};

import React from 'react';
import PropTypes from 'prop-types';
import { Card, Badge, Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { useTranslation } from 'react-i18next';

export const ProfileParticipationCard = ({ participation }) => {
  const { t } = useTranslation();

  const formatDuration = minutes => {
    if (!minutes) return '-';
    if (minutes < 60) return `${minutes}m`;
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return mins > 0 ? `${hours}h ${mins}m` : `${hours}h`;
  };

  const getLanguageBadgeVariant = language => {
    const variants = {
      ENGLISH: 'primary',
      PORTUGUESE: 'success',
      SPANISH: 'info',
      FRENCH: 'warning',
      GERMAN: 'secondary',
      ITALIAN: 'danger',
      DUTCH: 'dark',
    };
    return variants[language] || 'outline-dark';
  };

  return (
    <Card className="h-100 shadow-sm participation-card">
      <Card.Body className="d-flex flex-column">
        {/* Header with language badge */}
        <div className="d-flex justify-content-end mb-2">
          {participation.training?.language && (
            <Badge
              bg={getLanguageBadgeVariant(participation.training.language)}
            >
              {t(`languages.${participation.training.language}`)}
            </Badge>
          )}
        </div>

        {/* Training title */}
        <Card.Title className="mb-3">
          {participation.training?.title || t('profile.trainings')}
        </Card.Title>

        {/* Training description if available */}
        {participation.training?.description && (
          <Card.Text className="text-muted small mb-3">
            {participation.training.description}
          </Card.Text>
        )}

        {/* Training details */}
        <div className="flex-grow-1">
          <div className="mb-2">
            <small className="text-muted d-block">
              <i className="bi bi-calendar3 me-1"></i>
              {t('profile.participatedAt', 'Participation Date')}
            </small>
            <div>{formatDateToDDMMYYYY(participation.createdAt)}</div>
          </div>

          <div className="d-flex justify-content-between align-items-center mb-2">
            <div>
              <small className="text-muted d-block">
                <i className="bi bi-clock me-1"></i>
                {t('profile.duration')}
              </small>
              <div>{formatDuration(participation.training?.duration)}</div>
            </div>
            <div>
              <small className="text-muted d-block">
                <i className="bi bi-folder me-1"></i>
                {t('profile.area')}
              </small>
              <div className="text-end">
                {participation.training?.trainingArea?.name || '—'}
              </div>
            </div>
          </div>
        </div>

        {/* Action button */}
        {participation.training?.link && (
          <div className="mt-3">
            <Button
              variant="primary"
              as="a"
              href={participation.training.link}
              target="_blank"
              rel="noopener noreferrer"
              className="w-100"
            >
              <i className="bi bi-arrow-up-right-square me-2"></i>
              {t('trainings.user.accessTraining', 'Access Training')}
            </Button>
          </div>
        )}
      </Card.Body>

      <Card.Footer className="bg-transparent border-top-0 pt-0">
        <small className="text-muted">
          <i className="bi bi-hash me-1"></i>
          ID: {participation.id}
        </small>
      </Card.Footer>
    </Card>
  );
};

ProfileParticipationCard.propTypes = {
  participation: PropTypes.shape({
    id: PropTypes.number,
    createdAt: PropTypes.string,
    status: PropTypes.string,
    training: PropTypes.shape({
      title: PropTypes.string,
      description: PropTypes.string,
      duration: PropTypes.number,
      language: PropTypes.string,
      link: PropTypes.string,
      trainingArea: PropTypes.shape({
        name: PropTypes.string,
      }),
    }),
  }).isRequired,
};

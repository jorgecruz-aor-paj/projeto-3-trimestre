import React from 'react';
import PropTypes from 'prop-types';
import { Card, Badge, Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';

export const TrainingCard = ({
  training,
  onEdit,
  onView,
  onDeactivate,
  showActions = true,
}) => {
  const { t } = useTranslation();
  const { formatInstant } = useLanguage();

  const formatDuration = minutes => {
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
    <Card className="h-100">
      <Card.Body>
        <div className="d-flex justify-content-between mb-2">
          <Badge
            bg={training.active ? 'success' : 'secondary'}
            className="mb-2"
          >
            {training.active ? t('common.active') : t('common.inactive')}
          </Badge>
          <Badge bg={getLanguageBadgeVariant(training.language)}>
            {t(`languages.${training.language}`)}
          </Badge>
        </div>

        <Card.Title>{training.title}</Card.Title>

        {training.description && (
          <Card.Text className="text-muted small mb-3">
            {training.description}
          </Card.Text>
        )}

        <div className="d-flex justify-content-between align-items-center mb-3">
          <div className="small text-muted">
            <i className="bi bi-clock me-1"></i>
            {formatDuration(training.duration)}
          </div>
          <div className="small text-muted">
            <i className="bi bi-folder me-1"></i>
            {training.trainingArea?.name}
          </div>
        </div>

        {showActions && (
          <div className="d-flex gap-2 justify-content-end mt-3">
            {onEdit && (
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip>{t('common.edit')}</Tooltip>}
              >
                <Button
                  variant="outline-primary"
                  size="sm"
                  onClick={() => training.active && onEdit(training)}
                  disabled={!training.active}
                >
                  <i className="bi bi-pencil"></i>
                </Button>
              </OverlayTrigger>
            )}
            {!training.active && onView && (
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip>{t('common.view')}</Tooltip>}
              >
                <Button
                  variant="outline-secondary"
                  size="sm"
                  onClick={() => onView(training)}
                >
                  <i className="bi bi-eye"></i>
                </Button>
              </OverlayTrigger>
            )}
            {training.active && onDeactivate && (
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip>{t('trainings.admin.deactivate')}</Tooltip>}
              >
                <Button
                  variant="outline-danger"
                  size="sm"
                  onClick={() => onDeactivate(training)}
                >
                  <i className="bi bi-x-circle"></i>
                </Button>
              </OverlayTrigger>
            )}
            {training.link && (
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip>{t('trainings.admin.viewLink')}</Tooltip>}
              >
                <Button
                  variant="outline-info"
                  size="sm"
                  as="a"
                  href={training.link}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <i className="bi bi-link-45deg"></i>
                </Button>
              </OverlayTrigger>
            )}
          </div>
        )}
      </Card.Body>
      <Card.Footer className="text-muted small">
        <i className="bi bi-calendar-event me-1"></i>
        {formatInstant(training.createdAt)}
      </Card.Footer>
    </Card>
  );
};

TrainingCard.propTypes = {
  training: PropTypes.shape({
    id: PropTypes.number.isRequired,
    title: PropTypes.string.isRequired,
    description: PropTypes.string,
    duration: PropTypes.number.isRequired,
    language: PropTypes.string.isRequired,
    active: PropTypes.bool.isRequired,
    createdAt: PropTypes.string.isRequired,
    link: PropTypes.string,
    trainingArea: PropTypes.shape({
      name: PropTypes.string.isRequired,
    }),
  }).isRequired,
  onEdit: PropTypes.func,
  onView: PropTypes.func,
  onDeactivate: PropTypes.func,
  showActions: PropTypes.bool,
};

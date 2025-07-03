import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

/**
 * EvaluationCard displays a single evaluation's details in a card format.
 * Add download, status, and other actions as needed.
 */
export const EvaluationCard = ({ evaluation }) => {
  const { t } = useTranslation();

  // Example fields, adjust as needed
  const { id, evaluator, scale, status, date, comments } = evaluation;

  return (
    <div className="card evaluation-card mb-3">
      <div className="card-body">
        <h5 className="card-title">
          {t('evaluations.evaluation')} #{id}
        </h5>
        <p className="card-text">
          <strong>{t('evaluations.evaluator')}:</strong>{' '}
          {evaluator?.name || '-'}
          <br />
          <strong>{t('evaluations.scale')}:</strong> {scale || '-'}
          <br />
          <strong>{t('evaluations.status')}:</strong>{' '}
          {t(`evaluations.status.${status}`) || status}
          <br />
          <strong>{t('evaluations.date')}:</strong>{' '}
          {date ? new Date(date).toLocaleDateString() : '-'}
          <br />
          <strong>{t('evaluations.comments')}:</strong> {comments || '-'}
        </p>
        {/* Add download button, edit button, etc. here if needed */}
      </div>
    </div>
  );
};

EvaluationCard.propTypes = {
  evaluation: PropTypes.object.isRequired,
};

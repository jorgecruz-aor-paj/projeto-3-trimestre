import React from 'react';
import { formatDateToDDMMYYYY } from '../../utils/dateUtils';
import { translateEvaluationScale } from '../../utils/evaluationScaleTranslations';
import { useTranslation } from 'react-i18next';

const ProfileEvaluationCard = ({ evaluation }) => {
  const { t, i18n } = useTranslation();
  return (
    <div className="profile-evaluation-card">
      <div>
        <strong>
          {t('profile.evaluations')} #{evaluation.id}
        </strong>
      </div>
      <div>
        {t('profile.date')}: {formatDateToDDMMYYYY(evaluation.createdAt)}
      </div>
      <div>
        {t('profile.scale')}:{' '}
        {translateEvaluationScale(evaluation.scale, i18n.language)}
      </div>
      <div>
        {t('profile.evaluator')}:{' '}
        {evaluation.evaluator
          ? `${evaluation.evaluator.firstName} ${evaluation.evaluator.lastName}`
          : '-'}
      </div>
      <button
        className="download-evaluation-btn"
        onClick={() => {
          /* TODO: implement download */
        }}
      >
        {t('profile.downloadEvaluation')}
      </button>
    </div>
  );
};

export default ProfileEvaluationCard;

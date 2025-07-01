import React from 'react';
import PropTypes from 'prop-types';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { useTranslation } from 'react-i18next';

const ProfileParticipationCard = ({ participation }) => {
  const { t } = useTranslation();
  return (
    <div className="profile-participation-card">
      <div>
        <strong>
          {participation.training?.title || t('profile.trainings')}
        </strong>
      </div>
      <div>
        {t('profile.date')}: {formatDateToDDMMYYYY(participation.createdAt)}
      </div>
      <div>
        {t('profile.duration')}: {participation.training?.duration} min
      </div>
      <div>
        {t('profile.area')}: {participation.training?.trainingArea?.name || '—'}
      </div>
      {participation.training?.link && (
        <div>
          <a
            href={participation.training.link}
            target="_blank"
            rel="noopener noreferrer"
            className="training-link"
          >
            {t('profile.viewTraining')}
          </a>
        </div>
      )}
    </div>
  );
};

ProfileParticipationCard.propTypes = {
  participation: PropTypes.shape({
    createdAt: PropTypes.string,
    training: PropTypes.shape({
      title: PropTypes.string,
      duration: PropTypes.number,
      link: PropTypes.string,
      trainingArea: PropTypes.shape({
        name: PropTypes.string,
      }),
    }),
  }).isRequired,
};

export default ProfileParticipationCard;

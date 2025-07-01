import React from 'react';
import { formatDateToDDMMYYYY } from '../../utils/dateUtils';

const ProfileParticipationCard = ({ participation }) => {
  return (
    <div className="profile-participation-card">
      <div>
        <strong>{participation.training?.title || 'Training'}</strong>
      </div>
      <div>Date: {formatDateToDDMMYYYY(participation.createdAt)}</div>
      <div>Duration: {participation.training?.duration} min</div>
      <div>Area: {participation.training?.trainingArea?.name || '—'}</div>
      {participation.training?.link && (
        <div>
          <a
            href={participation.training.link}
            target="_blank"
            rel="noopener noreferrer"
            className="training-link"
          >
            View Training
          </a>
        </div>
      )}
    </div>
  );
};

export default ProfileParticipationCard;

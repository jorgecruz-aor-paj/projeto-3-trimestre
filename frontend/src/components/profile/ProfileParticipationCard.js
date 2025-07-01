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
      {/* Optionally: <div>Description: {participation.training?.description}</div> */}
    </div>
  );
};

export default ProfileParticipationCard;

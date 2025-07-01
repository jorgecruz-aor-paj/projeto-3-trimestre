import React from 'react';
import { useTrainingAreas } from 'hooks/useTrainingAreas';
import { formatDateToDDMMYYYY } from '../../utils/dateUtils';

const ProfileParticipationCard = ({ participation }) => {
  const { trainingAreas, trainingAreasLoading } = useTrainingAreas();

  let areaName = '';
  if (participation.training && trainingAreas && !trainingAreasLoading) {
    const area = trainingAreas.find(
      a => a.id === participation.training.trainingAreaId
    );
    areaName = area ? area.name : '';
  }

  return (
    <div className="profile-participation-card">
      <div>
        <strong>{participation.training?.title || 'Training'}</strong>
      </div>
      <div>Date: {formatDateToDDMMYYYY(participation.createdAt)}</div>
      <div>Duration: {participation.training?.duration} min</div>
      <div>Area: {areaName || '—'}</div>
      {/* Optionally: <div>Description: {participation.training?.description}</div> */}
    </div>
  );
};

export default ProfileParticipationCard;

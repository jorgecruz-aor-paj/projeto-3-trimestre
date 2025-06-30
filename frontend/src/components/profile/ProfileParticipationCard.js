import React from 'react';

const ProfileParticipationCard = ({ participation }) => {
  // Example fields, adjust as needed for your participation object
  return (
    <div className="profile-participation-card">
      <div>
        <strong>Participation #{participation.id}</strong>
      </div>
      <div>Training ID: {participation.trainingId}</div>
      <div>Participant ID: {participation.participantId}</div>
      <div>Status: {participation.status}</div>
      {/* Add more fields as needed */}
    </div>
  );
};

export default ProfileParticipationCard;

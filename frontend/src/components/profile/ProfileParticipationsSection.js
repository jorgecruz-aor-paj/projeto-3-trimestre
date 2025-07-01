import React, { useState, useEffect } from 'react';
import { useParticipationStore } from '../../stores/ParticipationStore';
import ProfileParticipationsFilter from './ProfileParticipationsFilter';
import ProfileParticipationCard from './ProfileParticipationCard';
import { useTrainingAreas } from 'hooks/useTrainingAreas';


const ProfileParticipationsSection = ({ userId }) => {
  const { participations, fetchParticipations, loading, error } =
    useParticipationStore();
  const [filters, setFilters] = useState({ participantId: userId });
 const { trainingAreas, trainingAreasLoading } = useTrainingAreas();

  useEffect(() => {
    fetchParticipations(filters);
  }, [JSON.stringify(filters)]);

  return (
    <div className="profile-participations-section">
      <ProfileParticipationsFilter filters={filters} setFilters={setFilters} />
      <div className="profile-participations-list">
        {loading && <div>Loading...</div>}
        {error && <div>Error loading participations</div>}
        {participations && participations.length > 0
          ? participations.map(part => (
              <ProfileParticipationCard key={part.id} participation={part} />
            ))
          : !loading && <div>No participations found.</div>}
      </div>
    </div>
  );
};

export default ProfileParticipationsSection;

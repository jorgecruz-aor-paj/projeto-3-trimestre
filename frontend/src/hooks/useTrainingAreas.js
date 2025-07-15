import { useState, useEffect } from 'react';
import { trainingService } from 'services/trainingService';

export const useTrainingAreas = () => {
  const [trainingAreas, setTrainingAreas] = useState([]);
  const [trainingAreasLoading, setTrainingAreasLoading] = useState(true);

 useEffect(() => {
  const fetchTrainingAreas = async () => {
    const cached = sessionStorage.getItem('trainingAreas');
    if (cached) {
      setTrainingAreas(JSON.parse(cached));
      setTrainingAreasLoading(false);
    } else {
      try {
        const data = await trainingService.getAllAreas();
        setTrainingAreas(data);
        sessionStorage.setItem('trainingAreas', JSON.stringify(data));
      } catch (err) {
        setTrainingAreas([]);
        console.error('Failed to fetch training areas:', err);
      } finally {
        setTrainingAreasLoading(false);
      }
    }
  };

  fetchTrainingAreas();
}, []);

  return { trainingAreas, trainingAreasLoading };
};

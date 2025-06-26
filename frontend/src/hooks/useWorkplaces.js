import { useState, useEffect } from 'react';
import { workplaceService } from 'services/workplaceService';

export const useWorkplaces = () => {
  const [workplaces, setWorkplaces] = useState([]);
  const [workplacesLoading, setWorkplacesLoading] = useState(true);

 useEffect(() => {
  const fetchWorkplaces = async () => {
    const cached = sessionStorage.getItem('workplaces');
    if (cached) {
      setWorkplaces(JSON.parse(cached));
      setWorkplacesLoading(false);
    } else {
      try {
        const data = await workplaceService.getWorkplaces();
        console.log(data);
        setWorkplaces(data);
        sessionStorage.setItem('workplaces', JSON.stringify(data));
      } catch (err) {
        setWorkplaces([]);
        console.error('Failed to fetch workplaces:', err);
      } finally {
        setWorkplacesLoading(false);
      }
    }
  };

  fetchWorkplaces();
}, []);


  return { workplaces, workplacesLoading };
};

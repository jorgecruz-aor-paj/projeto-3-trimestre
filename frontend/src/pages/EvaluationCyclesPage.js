import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { EvaluationCycleBox } from '../components/evaluationCycles/EvaluationCycleBox';
import { AdminStatsGrid } from '../components/dashboard/AdminStatsGrid';
import { cycleService } from 'services/cycleService';

export const EvaluationCyclesPage = () => {
  const [currentCycle, setCurrentCycle] = useState(null);
  const [closedCycles, setClosedCycles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [fatcycles, setFatCycles] = useState(true);
  // fatcycles is a flag to determine if we want to fetch closed cycles with their evaluations array or not
  useEffect(() => {
    const fetchCycles = async () => {
      setLoading(true);
      setError(null);
      try {
        const currentCycle = await cycleService.getCurrent();
        if (currentCycle.id == null) {
          setCurrentCycle(null);
        } else {
          setCurrentCycle(currentCycle);
        }
        const closedCycles = await cycleService.getAll({ complete: fatcycles });
        setClosedCycles(closedCycles);
      } catch (err) {
        setError('Failed to load evaluation cycles.');
      } finally {
        setLoading(false);
      }
    };
    fetchCycles();
  }, []);

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Evaluation Cycles</h2>
        <Button variant="primary">Start New Cycle</Button>
      </div>
      <AdminStatsGrid />
      {loading ? (
        <div>Loading cycles...</div>
      ) : error ? (
        <div className="text-danger">{error}</div>
      ) : (
        <>
          <EvaluationCycleBox cycle={currentCycle} isClosed={false} />
          <div className="mt-4">
            <h4>Closed Evaluation Cycles</h4>
            {closedCycles.length > 0 ? (
              closedCycles.map(cycle => (
                <EvaluationCycleBox
                  key={cycle.id}
                  cycle={cycle}
                  isClosed={true}
                />
              ))
            ) : (
              <div>No closed cycles.</div>
            )}
          </div>
        </>
      )}
    </div>
  );
};

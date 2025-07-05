import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { EvaluationCycleBox } from 'components/evaluationCycles/EvaluationCycleBox';
import { StatsDisplay } from 'components/dashboard/StatsDisplay';
import { cycleService } from 'services/cycleService';
import { statsService } from 'services/statsService';

export const EvaluationCyclesPage = () => {
  const [currentCycle, setCurrentCycle] = useState(null);
  const [closedCycles, setClosedCycles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [fatcycles, setFatCycles] = useState(true);
  const [stats, setStats] = useState({});
  const [statsLoading, setStatsLoading] = useState(true);
  const [statsError, setStatsError] = useState(null);
  // fatcycles is a flag to determine if we want to fetch closed cycles with their evaluations array or not
  
  const fetchStats = async () => {
    try {
      setStatsLoading(true);
      setStatsError(null);
      const [managerStats, evaluationStats] = await Promise.all([
        statsService.getManagerStats(),
        statsService.getEvaluationStats(),
      ]);
      setStats({
        managers: managerStats,
        evaluations: evaluationStats,
      });
    } catch (err) {
      setStatsError('Failed to load statistics.');
    } finally {
      setStatsLoading(false);
    }
  };

  useEffect(() => {
    const fetchCycles = async () => {
      setLoading(true);
      setError(null);
      try {
        const currentCycle = await cycleService.getCurrent();
        console.log('Current cycle:', currentCycle);
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
    fetchStats();
  }, []);

  const handleEvaluationEdit = () => {
    // Reload stats after an evaluation is successfully edited
    fetchStats();
  };

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Evaluation Cycles</h2>
        <Button variant="primary">Start New Cycle</Button>
      </div>
      <StatsDisplay 
        stats={stats} 
        loading={statsLoading} 
        error={statsError} 
      />
      {loading ? (
        <div>Loading cycles...</div>
      ) : error ? (
        <div className="text-danger">{error}</div>
      ) : (
        <>
          <EvaluationCycleBox 
            cycle={currentCycle} 
            isClosed={false} 
            onEvaluationEdit={handleEvaluationEdit}
          />
          <div className="mt-4">
            <h4>Closed Evaluation Cycles</h4>
            {closedCycles.length > 0 ? (
              closedCycles.map(cycle => (
                <EvaluationCycleBox
                  key={cycle.id}
                  cycle={cycle}
                  isClosed={true}
                  onEvaluationEdit={handleEvaluationEdit}
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

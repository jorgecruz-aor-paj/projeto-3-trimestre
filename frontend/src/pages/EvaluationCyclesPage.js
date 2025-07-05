import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { EvaluationCycleBox } from 'components/evaluationCycles/EvaluationCycleBox';
import { StatsDisplay } from 'components/dashboard/StatsDisplay';
import { cycleService } from 'services/cycleService';
import { statsService } from 'services/statsService';
import { StartCycleButton } from '../components/evaluationCycles/StartCycleButton';

export const EvaluationCyclesPage = () => {
  const [currentCycle, setCurrentCycle] = useState(null);
  const [closedCycles, setClosedCycles] = useState([]);
  const [expandedCycleId, setExpandedCycleId] = useState(null);
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

  const handleEvaluationEdit = async () => {
    // Reload stats and cycles after an evaluation is successfully edited
    await fetchStats();
    // Refetch cycles to update evaluation statuses in UI
    try {
      setLoading(true);
      setError(null);
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
  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Evaluation Cycles</h2>
        {!currentCycle && (
          <StartCycleButton
            disabled={stats.managers && stats.managers.unmanaged > 0}
            unmanagedCount={stats.managers?.unmanaged || 0}
            onClick={() => {
              /* TODO: implement start cycle logic */
            }}
          />
        )}
      </div>
      <StatsDisplay stats={stats} loading={statsLoading} error={statsError} />
      {loading ? (
        <div>Loading cycles...</div>
      ) : error ? (
        <div className="text-danger">{error}</div>
      ) : (
        <>
          {currentCycle && (
            <EvaluationCycleBox
              cycle={currentCycle}
              isClosed={false}
              onEvaluationEdit={handleEvaluationEdit}
              expanded={expandedCycleId === currentCycle.id}
              onToggleExpand={() =>
                setExpandedCycleId(
                  expandedCycleId === currentCycle.id ? null : currentCycle.id
                )
              }
            />
          )}
          <div className="mt-4">
            <h4>Closed Evaluation Cycles</h4>
            {closedCycles.length > 0 ? (
              closedCycles.map(cycle => (
                <EvaluationCycleBox
                  key={cycle.id}
                  cycle={cycle}
                  isClosed={true}
                  onEvaluationEdit={handleEvaluationEdit}
                  expanded={expandedCycleId === cycle.id}
                  onToggleExpand={() =>
                    setExpandedCycleId(
                      expandedCycleId === cycle.id ? null : cycle.id
                    )
                  }
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

import React from 'react';
import { Button } from 'react-bootstrap';
import { EvaluationCycleCurrent } from '../components/evaluationCycles/EvaluationCycleCurrent';
import { EvaluationCycleClosed } from '../components/evaluationCycles/EvaluationCycleClosed';
import { AdminStatsGrid } from '../components/dashboard/AdminStatsGrid';
// Placeholder data for demonstration
const currentCycle = null; // Replace with real data fetch
const closedCycles = []; // Replace with real data fetch

export const EvaluationCyclesPage = () => {
  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Evaluation Cycles</h2>
        <Button variant="primary">Start New Cycle</Button>
      </div>
      
            <AdminStatsGrid />
      <EvaluationCycleCurrent cycle={currentCycle} />
      <div className="mt-4">
        <h4>Closed Evaluation Cycles</h4>
        {closedCycles.length > 0 ? (
          closedCycles.map(cycle => (
            <EvaluationCycleClosed key={cycle.id} cycle={cycle} />
          ))
        ) : (
          <div>No closed cycles.</div>
        )}
      </div>
    </div>
  );
};

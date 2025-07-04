import React from 'react';

// Placeholder: Shows stats for managers with no users and incomplete evaluations
export const EvaluationCycleStats = ({
  managersWithoutUsers = 0,
  incompleteEvaluations = 0,
}) => (
  <div className="evaluation-cycle-stats mb-3">
    <div className="d-flex gap-4">
      <div>
        <strong>Managers with no users:</strong> {managersWithoutUsers}
      </div>
      <div>
        <strong>Evaluations left to complete:</strong> {incompleteEvaluations}
      </div>
    </div>
  </div>
);

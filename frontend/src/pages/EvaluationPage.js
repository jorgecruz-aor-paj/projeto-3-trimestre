import React, { useState, useEffect } from 'react';
import { EvaluationFilters } from '../components/evaluations/EvaluationFilters';
import { EvaluationList } from '../components/evaluations/EvaluationList';
import { useAuthStore } from '../stores/AuthStore';
import { useEvaluationStore } from '../stores/EvaluationStore';

export const EvaluationPage = () => {
  const [filters, setFilters] = useState({});
  const user = useAuthStore(state => state.user);
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();

  useEffect(() => {
    fetchEvaluations(filters);
    // If your linter complains about exhaustive-deps, ensure fetchEvaluations is stable (from Zustand) or ignore the warning.
    // Remove the eslint-disable-next-line if your linter does not recognize the rule.
  }, [JSON.stringify(filters)]);

  return (
    <div className="container py-4">
      <h2 className="mb-4">Avaliações</h2>
      <EvaluationFilters
        filters={filters}
        setFilters={setFilters}
        user={user}
      />
      <EvaluationList
        evaluations={evaluations}
        loading={loading}
        error={error}
      />
    </div>
  );
};

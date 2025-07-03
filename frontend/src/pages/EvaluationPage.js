import React, { useState, useEffect } from 'react';
import { EvaluationFilters } from '../components/evaluations/EvaluationFilters';
import { EvaluationList } from '../components/evaluations/EvaluationList';
import { useAuthStore } from '../stores/AuthStore';
import { useEvaluationStore } from '../stores/EvaluationStore';

export const EvaluationPage = () => {
  const [filters, setFilters] = useState({});
  const [selectedEvaluations, setSelectedEvaluations] = useState([]); // array of evaluation ids
  const user = useAuthStore(state => state.user);
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();
  const { showAlert } = require('../contexts/AlertContext');

  useEffect(() => {
    fetchEvaluations(filters);
  }, [JSON.stringify(filters)]);

  // Handler for toggling selection
  const handleSelectEvaluation = (evaluationId, checked) => {
    setSelectedEvaluations(prev =>
      checked ? [...prev, evaluationId] : prev.filter(id => id !== evaluationId)
    );
  };

  // Handler for closing selected evaluations
  const handleCloseSelected = async () => {
    if (!selectedEvaluations.length) return;
    // Find the cycleId from the first selected evaluation (assuming all are from the same cycle)
    const selected = evaluations.find(e => e.id === selectedEvaluations[0]);
    const cycleId = selected?.evaluationCycle?.id;
    if (!cycleId) {
      showAlert({
        title: 'Error',
        message: 'Could not determine evaluation cycle.',
        variant: 'danger',
      });
      return;
    }
    try {
      const { evaluationService } = require('../services/evaluationService');
      await evaluationService.closeEvaluation({
        cycleId,
        evaluationIds: selectedEvaluations,
      });
      showAlert({
        title: 'Success',
        message: 'Selected evaluations closed successfully.',
        variant: 'success',
      });
      setSelectedEvaluations([]);
      fetchEvaluations(filters);
    } catch (error) {
      showAlert({
        title: 'Error',
        message: error?.message || 'Failed to close evaluations.',
        variant: 'danger',
      });
    }
  };

  // Only admins can close multiple evaluations
  const isAdmin = user?.role === 'ADMIN';

  return (
    <div className="container py-4">
      <h2 className="mb-4">Avaliações</h2>
      <EvaluationFilters
        filters={filters}
        setFilters={setFilters}
        user={user}
      />
      {isAdmin && (
        <div className="mb-3">
          <button
            className="btn btn-danger"
            disabled={!selectedEvaluations.length}
            onClick={handleCloseSelected}
          >
            Fechar avaliações selecionadas
          </button>
          <span className="ms-2 text-muted">
            {selectedEvaluations.length} selecionada(s)
          </span>
        </div>
      )}
      <EvaluationList
        evaluations={evaluations}
        loading={loading}
        error={error}
        renderCheckbox={isAdmin}
        selectedEvaluations={selectedEvaluations}
        onSelectEvaluation={handleSelectEvaluation}
      />
    </div>
  );
};

import React, { useState } from 'react';
import {EvaluationFilters} from '../components/evaluations/EvaluationFilters';
import {EvaluationList} from '../components/evaluations/EvaluationList';

export const EvaluationPage = () => {
  const [filters, setFilters] = useState({});
  // Optionally, add logic for extra fields, permissions, etc.
  return (
    <div className="container py-4">
      <h2 className="mb-4">Avaliações</h2>
      <EvaluationFilters filters={filters} setFilters={setFilters} />
      <EvaluationList filters={filters} />
    </div>
  );
};


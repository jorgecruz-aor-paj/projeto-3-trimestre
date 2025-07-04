import React from 'react';
import PropTypes from 'prop-types';
import { EvaluationCard } from '../evaluations/EvaluationCard';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';

export const EvaluationCycleClosed = ({ cycle }) => {
  if (!cycle) return null;
  return (
    <div className="evaluation-cycle-closed mb-4">
      <h5>Closed Evaluation Cycle</h5>
      <div>
        <strong>Start Date:</strong> {formatDateToDDMMYYYY(cycle.startDate)}
        <br />
        <strong>Closure Date:</strong>{' '}
        {cycle.closureDate ? formatDateToDDMMYYYY(cycle.closureDate) : 'N/A'}
      </div>
      <div className="mt-3">
        {cycle.evaluations && cycle.evaluations.length > 0 ? (
          cycle.evaluations.map(ev => (
            <EvaluationCard key={ev.id} evaluation={ev} />
          ))
        ) : (
          <div>No evaluations in this cycle.</div>
        )}
      </div>
    </div>
  );
};

EvaluationCycleClosed.propTypes = {
  cycle: PropTypes.object,
};

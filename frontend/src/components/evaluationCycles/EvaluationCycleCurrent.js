import React from 'react';
import PropTypes from 'prop-types';
import { EditableEvaluationCard } from '../evaluations/EditableEvaluationCard';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';

export const EvaluationCycleCurrent = ({ cycle }) => {
  if (!cycle) return null;
  return (
    <div className="evaluation-cycle-current mb-4">
      <h4>Current Evaluation Cycle</h4>
      <div>
        <strong>Start Date:</strong> {formatDateToDDMMYYYY(cycle.startDate)}
        <br />
        <strong>End Date:</strong> {formatDateToDDMMYYYY(cycle.endDate)}
      </div>
      <div className="mt-3">
        {cycle.evaluations && cycle.evaluations.length > 0 ? (
          cycle.evaluations.map(ev => (
            <EditableEvaluationCard key={ev.id} evaluation={ev} />
          ))
        ) : (
          <div>No evaluations in this cycle.</div>
        )}
      </div>
    </div>
  );
};

EvaluationCycleCurrent.propTypes = {
  cycle: PropTypes.object,
};

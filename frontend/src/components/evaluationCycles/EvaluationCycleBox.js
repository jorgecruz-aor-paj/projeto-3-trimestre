import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { EditableEvaluationCard } from '../evaluations/EditableEvaluationCard';
import { EvaluationCard } from '../evaluations/EvaluationCard';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { useLanguage } from 'contexts/LanguageContext';

/**
 * Collapsible box for displaying an evaluation cycle (current or closed)
 * @param {Object} props
 * @param {Object} props.cycle - The evaluation cycle object
 * @param {boolean} props.isClosed - Whether the cycle is closed
 * @param {Function} props.onEvaluationEdit - Callback function called when an evaluation is edited
 */
export const EvaluationCycleBox = ({ cycle, isClosed, onEvaluationEdit }) => {
  const { formatInstant } = useLanguage();
  const [expanded, setExpanded] = useState(false);
  if (!cycle) return null;
  return (
    <div
      className={`evaluation-cycle-box mb-4 border rounded shadow-sm${
        isClosed ? ' bg-light' : ''
      }`}
    >
      <div
        className="d-flex justify-content-between align-items-center p-3 bg-light border-bottom"
        style={{ cursor: 'pointer' }}
        onClick={() => setExpanded(e => !e)}
      >
        <div>
          <strong>Start Date:</strong> {formatDateToDDMMYYYY(cycle.startDate)}
          {'  '}
          {isClosed ? (
            <>
              <strong>Closure Date:</strong>{' '}
              {cycle.closureDate ? formatInstant(cycle.closureDate) : 'N/A'}
            </>
          ) : (
            <>
              <strong>End Date:</strong> {formatDateToDDMMYYYY(cycle.endDate)}
            </>
          )}
        </div>
        <span
          style={{
            fontSize: 24,
            transition: 'transform 0.2s',
            transform: expanded ? 'rotate(90deg)' : 'rotate(0deg)',
          }}
        >
          ▶
        </span>
      </div>
      {expanded && (
        <div className="p-3">
          {cycle.evaluations && cycle.evaluations.length > 0 ? (
            cycle.evaluations.map(ev =>
              isClosed || ev.status === 'CLOSED' ? (
                <EvaluationCard key={ev.id} evaluation={ev} />
              ) : (
                <EditableEvaluationCard 
                  key={ev.id} 
                  evaluation={ev} 
                  onEdit={onEvaluationEdit}
                />
              )
            )
          ) : (
            <div>No evaluations in this cycle.</div>
          )}
        </div>
      )}
    </div>
  );
};

EvaluationCycleBox.propTypes = {
  cycle: PropTypes.object,
  isClosed: PropTypes.bool,
  onEvaluationEdit: PropTypes.func,
};

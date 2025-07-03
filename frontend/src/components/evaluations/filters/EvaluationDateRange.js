import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

export const EvaluationDateRange = ({ startDate, endDate, onChange }) => {
  const { t } = useTranslation();
  return (
    <div className="col-auto">
      <fieldset className="border rounded p-2 mb-0" style={{ minWidth: 220 }}>
        <legend
          className="float-none w-auto mb-1 px-2 small"
          style={{ fontSize: '0.95em' }}
        >
          {t('filters.evaluationCycleDateRange') ||
            'Evaluation Cycle Closure Date Range'}
        </legend>
        <div className="d-flex flex-row gap-2 align-items-center">
          <div>
            <label htmlFor="startDate" className="form-label mb-0">
              {t('filters.startDate')}
            </label>
            <input
              id="startDate"
              type="date"
              name="startDate"
              value={startDate || ''}
              onChange={onChange}
              className="form-control form-control-sm"
              style={{ minWidth: 120 }}
            />
          </div>
          <span className="mx-1">-</span>
          <div>
            <label htmlFor="endDate" className="form-label mb-0">
              {t('filters.endDate')}
            </label>
            <input
              id="endDate"
              type="date"
              name="endDate"
              value={endDate || ''}
              onChange={onChange}
              className="form-control form-control-sm"
              style={{ minWidth: 120 }}
            />
          </div>
        </div>
      </fieldset>
    </div>
  );
};

EvaluationDateRange.propTypes = {
  startDate: PropTypes.string,
  endDate: PropTypes.string,
  onChange: PropTypes.func.isRequired,
};

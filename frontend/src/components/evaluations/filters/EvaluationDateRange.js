import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

export const EvaluationDateRange = ({ startDate, endDate, onChange }) => {
  const { t } = useTranslation();
  return (
    <div>
      <label className="form-label small fw-semibold">
        {t('filters.evaluationCycleDateRange') || 'Date Range'}
      </label>
      <div className="border rounded p-2" style={{ minWidth: '300px' }}>
        <div className="row g-2">
          <div className="col-6">
            <label htmlFor="startDate" className="form-label mb-1 small">
              {t('filters.startDate')}
            </label>
            <input
              id="startDate"
              type="date"
              name="startDate"
              value={startDate || ''}
              onChange={onChange}
              className="form-control"
            />
          </div>
          <div className="col-6">
            <label htmlFor="endDate" className="form-label mb-1 small">
              {t('filters.endDate')}
            </label>
            <input
              id="endDate"
              type="date"
              name="endDate"
              value={endDate || ''}
              onChange={onChange}
              className="form-control"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

EvaluationDateRange.propTypes = {
  startDate: PropTypes.string,
  endDate: PropTypes.string,
  onChange: PropTypes.func.isRequired,
};

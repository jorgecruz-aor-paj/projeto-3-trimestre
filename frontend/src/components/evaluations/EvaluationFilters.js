import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import {EvaluationUserSelect} from './filters/EvaluationUserSelect';
import {EvaluationScaleSelect} from './filters/EvaluationScaleSelect';
import {EvaluationStatusSelect} from './filters/EvaluationStatusSelect';
import {EvaluationSortSelect} from './filters/EvaluationSortSelect';
import {EvaluationDateRange} from './filters/EvaluationDateRange';

/**
 * Modular EvaluationFilters component for use in EvaluationPage and elsewhere.
 * Add/remove filter fields as needed for extensibility.
 */
export const EvaluationFilters = ({
  filters,
  setFilters,
  displayedUserId,
  extraFields,
}) => {
  const { t } = useTranslation();

  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    if (name === 'startDate' || name === 'endDate') {
      setFilters(prev => ({ ...prev, [name]: value }));
    } else {
      setFilters(prev => ({
        ...prev,
        [name]: type === 'checkbox' ? checked : value,
      }));
    }
  };

  return (
    <form
      className="evaluation-filters row row-cols-lg-auto g-2 align-items-center mb-3 small"
      style={{ fontSize: '0.95em', position: 'relative' }}
    >
      <EvaluationUserSelect
        value={filters.evaluatorId}
        onChange={handleChange}
        excludeUserId={displayedUserId}
        name="evaluatorId"
      />
      <EvaluationScaleSelect
        value={filters.evaluationScale}
        onChange={handleChange}
        name="evaluationScale"
      />
      <EvaluationStatusSelect
        value={filters.status}
        onChange={handleChange}
        name="status"
      />
      <div className="col-auto">
        <label htmlFor="searchText" className="form-label mb-0">
          {t('common.search')}
        </label>
        <input
          id="searchText"
          type="text"
          name="searchText"
          value={filters.searchText || ''}
          onChange={handleChange}
          className="form-control form-control-sm"
          placeholder={t('common.search')}
        />
      </div>
      <EvaluationSortSelect
        value={filters.evaluationSortField}
        onChange={handleChange}
        name="evaluationSortField"
      />
      <div className="col-auto">
        <label className="form-label mb-0">{t('filters.ascending')}</label>
        <input
          type="checkbox"
          name="ascending"
          checked={!!filters.ascending}
          onChange={handleChange}
          className="form-check-input ms-1"
          style={{ verticalAlign: 'middle' }}
        />
      </div>
      <EvaluationDateRange
        startDate={filters.startDate}
        endDate={filters.endDate}
        onChange={handleChange}
      />
      {/* Render any extra fields passed in as children or via extraFields prop */}
      {extraFields}
    </form>
  );
};

EvaluationFilters.propTypes = {
  filters: PropTypes.object.isRequired,
  setFilters: PropTypes.func.isRequired,
  displayedUserId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  extraFields: PropTypes.node,
};

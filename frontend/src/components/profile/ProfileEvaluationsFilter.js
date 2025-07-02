import React from 'react';
import { useTranslation } from 'react-i18next';

const scaleOptions = [
  { value: '', label: '' },
  { value: 'LOW_CONTRIBUTION', label: 'Low contribution' },
  { value: 'PARCIAL_CONTRIBUTION', label: 'Partial contribution' },
  { value: 'EXPECTED_CONTRIBUTION', label: 'Expected contribution' },
  { value: 'EXCEED_CONTRIBUTION', label: 'Exceeded contribution' },
];

const sortFieldOptions = [
  { value: 'EVALUATION_ID', label: 'ID' },
  { value: 'EVALUATED_ID', label: 'Evaluated' },
  { value: 'EVALUATOR_ID', label: 'Evaluator' },
  { value: 'STATUS', label: 'Status' },
  { value: 'DATE', label: 'Date' },
  { value: 'SCALE', label: 'Scale' },
];

const ProfileEvaluationsFilter = ({ filters, setFilters }) => {
  const { t } = useTranslation();

  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  return (
    <form className="profile-evaluations-filter d-flex flex-wrap gap-2 mb-3">
      {/* Evaluator ID */}
      <input
        type="number"
        name="evaluatorId"
        value={filters.evaluatorId || ''}
        onChange={handleChange}
        placeholder={t('profile.evaluator')}
        className="form-control"
        min={1}
      />

      {/* Scale */}
      <select
        name="evaluationScale"
        value={filters.evaluationScale || ''}
        onChange={handleChange}
        className="form-select"
      >
        {scaleOptions.map(opt => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>

      {/* Search Text */}
      <input
        type="text"
        name="searchText"
        value={filters.searchText || ''}
        onChange={handleChange}
        placeholder={t('common.search')}
        className="form-control"
      />

      {/* Sort Field */}
      <select
        name="evaluationSortField"
        value={filters.evaluationSortField || 'DATE'}
        onChange={handleChange}
        className="form-select"
      >
        {sortFieldOptions.map(opt => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>

      {/* Ascending */}
      <label className="form-check-label d-flex align-items-center">
        <input
          type="checkbox"
          name="ascending"
          checked={!!filters.ascending}
          onChange={handleChange}
          className="form-check-input me-1"
        />
        {t('common.ascending')}
      </label>

      {/* Start Date */}
      <input
        type="date"
        name="startDate"
        value={filters.startDate || ''}
        onChange={handleChange}
        className="form-control"
        placeholder={t('profile.startDate')}
      />

      {/* End Date */}
      <input
        type="date"
        name="endDate"
        value={filters.endDate || ''}
        onChange={handleChange}
        className="form-control"
        placeholder={t('profile.endDate')}
      />
    </form>
  );
};

export default ProfileEvaluationsFilter;

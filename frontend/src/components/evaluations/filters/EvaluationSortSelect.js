import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

const sortFieldOptions = [
  { value: 'EVALUATION_ID', label: 'profile.sortFields.id' },
  { value: 'EVALUATED_ID', label: 'profile.sortFields.evaluated' },
  { value: 'EVALUATOR_ID', label: 'profile.sortFields.evaluator' },
  { value: 'STATUS', label: 'profile.sortFields.status' },
  { value: 'DATE', label: 'filters.creationDate' },
  { value: 'SCALE', label: 'filters.scale' },
];

export const EvaluationSortSelect = ({ value, onChange, label, name }) => {
  const { t } = useTranslation();
  return (
    <div className="col-auto">
      <label htmlFor={name} className="form-label small fw-semibold">
        {label || t('profile.sortBy')}
      </label>
      <select
        id={name}
        name={name}
        value={value || 'DATE'}
        onChange={onChange}
        className="form-select"
      >
        {sortFieldOptions.map(opt => (
          <option key={opt.value} value={opt.value}>
            {t(opt.label)}
          </option>
        ))}
      </select>
    </div>
  );
};

EvaluationSortSelect.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func.isRequired,
  label: PropTypes.string,
  name: PropTypes.string.isRequired,
};

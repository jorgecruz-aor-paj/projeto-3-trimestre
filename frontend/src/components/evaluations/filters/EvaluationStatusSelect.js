import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

const statusOptions = [
  { value: '', label: 'common.all' },
  { value: 'OPEN', label: 'evaluations.status.OPEN' },
  { value: 'CLOSED', label: 'evaluations.status.CLOSED' },
  { value: 'IN_PROGRESS', label: 'evaluations.status.IN_PROGRESS' },
  { value: 'DRAFT', label: 'evaluations.status.DRAFT' },
];

const EvaluationStatusSelect = ({ value, onChange, label, name }) => {
  const { t } = useTranslation();
  return (
    <div className="col-auto">
      <label htmlFor={name} className="form-label mb-0">
        {label || t('evaluations.status.title')}
      </label>
      <select
        id={name}
        name={name}
        value={value || ''}
        onChange={onChange}
        className="form-select form-select-sm"
      >
        {statusOptions.map(opt => (
          <option key={opt.value} value={opt.value}>
            {t(opt.label)}
          </option>
        ))}
      </select>
    </div>
  );
};

EvaluationStatusSelect.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func.isRequired,
  label: PropTypes.string,
  name: PropTypes.string.isRequired,
};

export default EvaluationStatusSelect;

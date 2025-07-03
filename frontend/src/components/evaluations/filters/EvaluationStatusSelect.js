import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

const statusOptions = [
  { value: '', label: 'common.all' },
  { value: 'ONGOING', label: 'evaluationStatus.ONGOING' },
  { value: 'CLOSED', label: 'evaluationStatus.CLOSED' },
  { value: 'COMPLETED', label: 'evaluationStatus.COMPLETED' },
];

export const EvaluationStatusSelect = ({ value, onChange, label, name }) => {
  const { t } = useTranslation();
  return (
    <div className="col-auto">
      <label htmlFor={name} className="form-label mb-0">
        {label || t('evaluationStatus.title')}
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

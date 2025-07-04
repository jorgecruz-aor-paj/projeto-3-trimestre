import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

const scaleOptions = [
  { value: '', label: 'evaluationScale.any' },
  { value: 'LOW_CONTRIBUTION', label: 'evaluationScale.low' },
  { value: 'PARCIAL_CONTRIBUTION', label: 'evaluationScale.partial' },
  { value: 'EXPECTED_CONTRIBUTION', label: 'evaluationScale.expected' },
  { value: 'EXCEED_CONTRIBUTION', label: 'evaluationScale.exceed' },
];

export const EvaluationScaleSelect = ({ value, onChange, label, name }) => {
  const { t } = useTranslation();
  return (
    <div className="col-auto">
      <label htmlFor={name} className="form-label small fw-semibold">
        {label || t('evaluationScale.title')}
      </label>
      <select
        id={name}
        name={name}
        value={value || ''}
        onChange={onChange}
        className="form-select"
      >
        {scaleOptions.map(opt => (
          <option key={opt.value} value={opt.value}>
            {t(opt.label)}
          </option>
        ))}
      </select>
    </div>
  );
};

EvaluationScaleSelect.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func.isRequired,
  label: PropTypes.string,
  name: PropTypes.string.isRequired,
};

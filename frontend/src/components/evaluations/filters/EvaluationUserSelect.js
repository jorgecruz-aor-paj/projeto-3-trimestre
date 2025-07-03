import React from 'react';
import PropTypes from 'prop-types';
import { useUserList } from '../../../hooks/useUserList';
import { useTranslation } from 'react-i18next';

export const EvaluationUserSelect = ({
  value,
  onChange,
  excludeUserId,
  label,
  name,
}) => {
  const { t } = useTranslation();
  const { userList, loading } = useUserList();
  const filteredUserList =
    userList && excludeUserId
      ? userList.filter(u => String(u.id) !== String(excludeUserId))
      : userList;

  return (
    <div className="col-auto">
      <label htmlFor={name} className="form-label mb-0">
        {label || t('profile.evaluator')}
      </label>
      <select
        id={name}
        name={name}
        value={value || ''}
        onChange={onChange}
        className="form-select form-select-sm"
        disabled={loading || !filteredUserList}
      >
        <option value="">{t('common.all')}</option>
        {filteredUserList &&
          filteredUserList.map(u => (
            <option key={u.id} value={u.id}>
              {u.firstName} {u.lastName}
            </option>
          ))}
      </select>
    </div>
  );
};

EvaluationUserSelect.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func.isRequired,
  excludeUserId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  label: PropTypes.string,
  name: PropTypes.string.isRequired,
};

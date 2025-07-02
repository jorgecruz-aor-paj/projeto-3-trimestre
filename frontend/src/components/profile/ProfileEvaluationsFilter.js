import React from 'react';
import { useUserList } from '../../hooks/useUserList';
import { useTranslation } from 'react-i18next';
import { toApiDateTime } from '../../utils/dateUtils';

const scaleOptions = [
  { value: '', label: 'evaluationScale.any' },
  { value: 'LOW_CONTRIBUTION', label: 'evaluationScale.low' },
  { value: 'PARCIAL_CONTRIBUTION', label: 'evaluationScale.partial' },
  { value: 'EXPECTED_CONTRIBUTION', label: 'evaluationScale.expected' },
  { value: 'EXCEED_CONTRIBUTION', label: 'evaluationScale.exceed' },
];

const sortFieldOptions = [
  { value: 'EVALUATION_ID', label: 'profile.sortFields.id' },
  { value: 'EVALUATED_ID', label: 'profile.sortFields.evaluated' },
  { value: 'EVALUATOR_ID', label: 'profile.sortFields.evaluator' },
  { value: 'STATUS', label: 'profile.sortFields.status' },
  { value: 'DATE', label: 'filters.startDate' },
  { value: 'SCALE', label: 'filters.endDate' },
];

// Accept displayedUserId as a prop to filter out the displayed user from the evaluator dropdown
const ProfileEvaluationsFilter = ({ filters, setFilters, displayedUserId }) => {
  const { t } = useTranslation();
  const {
    userList,
    loading: userListLoading,
    error: userListError,
  } = useUserList();
  // Remove the displayed user from the evaluator dropdown
  const filteredUserList =
    userList && displayedUserId
      ? userList.filter(u => String(u.id) !== String(displayedUserId))
      : userList;

  // Keep both raw and API date in state for correct display and API usage
  // Add dateDisplay for user-friendly display of selected date
  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    if (name === 'startDate' || name === 'endDate') {
      setFilters(prev => {
        const next = { ...prev };
        if (value) {
          next[name] = toApiDateTime(value, name === 'endDate');
          next[`${name}Display`] = value; // Store raw date for display
        } else {
          delete next[name];
          delete next[`${name}Display`];
        }
        return next;
      });
    } else {
      setFilters(prev => {
        const next = {
          ...prev,
          [name]: type === 'checkbox' ? checked : value,
        };
        // Remove empty string filter keys for select fields
        if (
          (name === 'evaluationScale' ||
            name === 'evaluatorId' ||
            name === 'evaluationSortField') &&
          !value
        ) {
          delete next[name];
        }
        return next;
      });
    }
  };

  return (
    <form
      className="profile-evaluations-filter row row-cols-lg-auto g-2 align-items-center mb-3 small"
      style={{ fontSize: '0.95em' }}
    >
      {/* Evaluator */}
      <div className="col-auto">
        <label htmlFor="evaluatorId" className="form-label mb-0">
          {t('profile.evaluator')}
        </label>
        <select
          id="evaluatorId"
          name="evaluatorId"
          value={filters.evaluatorId || ''}
          onChange={handleChange}
          className="form-select form-select-sm"
          disabled={userListLoading || !filteredUserList}
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

      {/* Scale */}
      <div className="col-auto">
        <label htmlFor="evaluationScale" className="form-label mb-0">
          {t('evaluationScale.title')}
        </label>
        <select
          id="evaluationScale"
          name="evaluationScale"
          value={filters.evaluationScale || ''}
          onChange={handleChange}
          className="form-select form-select-sm"
        >
          {scaleOptions.map(opt => (
            <option key={opt.value} value={opt.value}>
              {t(opt.label)}
            </option>
          ))}
        </select>
      </div>

      {/* Search Text */}
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

      {/* Sort Field */}
      <div className="col-auto">
        <label htmlFor="evaluationSortField" className="form-label mb-0">
          {t('profile.sortBy')}
        </label>
        <select
          id="evaluationSortField"
          name="evaluationSortField"
          value={filters.evaluationSortField || 'DATE'}
          onChange={handleChange}
          className="form-select form-select-sm"
        >
          {sortFieldOptions.map(opt => (
            <option key={opt.value} value={opt.value}>
              {t(opt.label)}
            </option>
          ))}
        </select>
      </div>

      {/* Ascending */}
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

      {/* Evaluation Cycle Closure Date Range */}
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
                value={filters.startDateDisplay || ''}
                onChange={handleChange}
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
                value={filters.endDateDisplay || ''}
                onChange={handleChange}
                className="form-control form-control-sm"
                style={{ minWidth: 120 }}
              />
            </div>
          </div>
        </fieldset>
      </div>
    </form>
  );
};

export default ProfileEvaluationsFilter;

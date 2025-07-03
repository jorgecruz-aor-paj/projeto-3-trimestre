import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { EvaluationUserSelect } from './filters/EvaluationUserSelect';
import { EvaluationScaleSelect } from './filters/EvaluationScaleSelect';
import { EvaluationStatusSelect } from './filters/EvaluationStatusSelect';
import { EvaluationSortSelect } from './filters/EvaluationSortSelect';
import { EvaluationDateRange } from './filters/EvaluationDateRange';

/**
 * Modular EvaluationFilters component for use in EvaluationPage and elsewhere.
 * Add/remove filter fields as needed for extensibility.
 */
/**
 * @param {Object} props
 * @param {Object} props.filters
 * @param {Function} props.setFilters
 * @param {string|number} [props.displayedUserId]
 * @param {Object} [props.user] - The full user object (should include id, role, etc)
 * @param {React.ReactNode} [props.extraFields]
 */
export const EvaluationFilters = ({
  filters,
  setFilters,
  displayedUserId,
  user,
  extraFields,
}) => {
  const { t } = useTranslation();
console.log(user);

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

  // If manager, always set evaluatorId to himself
  useEffect(() => {
    if (
      user?.role === 'MANAGER' &&
      user?.id &&
      filters.evaluatorId !== user.id
    ) {
      setFilters(prev => ({ ...prev, evaluatorId: user.id }));
    }
  }, [user?.role, user?.id]);

  // Download all evaluations handler (CSV)
  const handleDownloadAll = async () => {
    try {
      const exportService =
        require('../../services/exportService').exportService;
      const exportFilters = { ...filters };
      delete exportFilters.startDateDisplay;
      delete exportFilters.endDateDisplay;
      const response = await exportService.evaluationsToCSVFiltered(
        exportFilters
      );
      let filename = 'evaluations.csv';
      if (
        response &&
        response.headers &&
        response.headers['content-disposition']
      ) {
        const match = response.headers['content-disposition'].match(
          /filename="?([^";]+)"?/
        );
        if (match && match[1]) filename = match[1];
      }
      const blob = response.data instanceof Blob ? response.data : response;
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.style.display = 'none';
      a.href = url;
      a.setAttribute('download', filename);
      document.body.appendChild(a);
      a.click();
      setTimeout(() => {
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }, 200);
    } catch (error) {
      // eslint-disable-next-line no-console
      console.error('Error downloading evaluations CSV:', error);
      if (typeof window !== 'undefined' && window.alert) {
        window.alert('Failed to download evaluations CSV.');
      }
    }
  };

  // Download all evaluations handler (Excel) - placeholder
  const handleDownloadExcel = () => {
    // TODO: Implement Excel export if needed
    if (typeof window !== 'undefined' && window.alert) {
      window.alert('Excel export not implemented.');
    }
  };

  return (
    <form
      className="evaluation-filters row row-cols-lg-auto g-2 align-items-center mb-3 small"
      style={{ fontSize: '0.95em', position: 'relative' }}
    >
      {/* Download all evaluations icon buttons */}
      <div
        className="position-absolute"
        style={{ top: 0, right: 0, zIndex: 2 }}
      >
        {/* CSV Download button */}
        <button
          type="button"
          className="btn btn-link p-0 me-2"
          title={t('evaluations.title') + ' CSV'}
          onClick={handleDownloadAll}
          aria-label={t('evaluations.title') + ' CSV'}
        >
          <i className="fas fa-file-csv" />
        </button>
        {/* Excel Download button */}
        <button
          type="button"
          className="btn btn-link p-0"
          title={t('evaluations.title') + ' Excel'}
          onClick={handleDownloadExcel}
          aria-label={t('evaluations.title') + ' Excel'}
        >
          <i className="fas fa-file-excel" />
        </button>
      </div>

      {/* Conditional filter fields based on user role */}
      {user?.role === 'ADMIN' && (
        <EvaluationUserSelect
          value={filters.evaluatorId}
          onChange={handleChange}
          excludeUserId={displayedUserId}
          name="evaluatorId"
        />
      )}
      {/* Always show evaluatee filter */}
      <EvaluationUserSelect
        value={filters.evaluatedId}
        onChange={handleChange}
        name="evaluatedId"
        label={t('filters.evaluatee')}
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
  user: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    role: PropTypes.string,
    // ...other user fields as needed
  }),
  extraFields: PropTypes.node,
};

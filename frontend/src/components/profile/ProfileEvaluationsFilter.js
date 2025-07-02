
import React from 'react';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFileCsv, faFileExcel } from '@fortawesome/free-solid-svg-icons';
import { exportService } from '../../services/exportService';
import { useTranslation } from 'react-i18next';
import { useAlert } from '../../contexts/AlertContext';
import EvaluationUserSelect from '../evaluations/filters/EvaluationUserSelect';
import EvaluationScaleSelect from '../evaluations/filters/EvaluationScaleSelect';
import EvaluationSortSelect from '../evaluations/filters/EvaluationSortSelect';
import EvaluationDateRange from '../evaluations/filters/EvaluationDateRange';

// Accept displayedUserId as a prop to filter out the evaluator dropdown

// Accept displayedUserId as a prop to filter out the evaluator dropdown
const ProfileEvaluationsFilter = ({ filters, setFilters, displayedUserId }) => {

  const { t } = useTranslation();
  const { showAlert } = useAlert();

  // Unified handler for all modular filter components
  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => {
      const next = { ...prev, [name]: type === 'checkbox' ? checked : value };
      // Remove empty string filter keys for select fields
      if (
        (name === 'evaluationScale' ||
          name === 'evaluatorId' ||
          name === 'evaluationSortField') &&
        !value
      ) {
        delete next[name];
      }
      // For date fields, keep both display and API value
      if (name === 'startDate' || name === 'endDate') {
        if (value) {
          next[`${name}Display`] = value;
        } else {
          delete next[`${name}Display`];
        }
      }
      return next;
    });
  };

  // Download all evaluations handler
  const handleDownloadAll = async () => {
    try {
      // Prepare filters for export (remove display-only keys)
      const exportFilters = { ...filters };
      delete exportFilters.startDateDisplay;
      delete exportFilters.endDateDisplay;
      // Call exportService with current filters
      const response = await exportService.evaluationsToCSVFiltered(
        exportFilters
      );
      // Get filename from Content-Disposition header if available
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
      // Support both axios (response.data as Blob) and fetch (response as Blob)
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
      console.error('Error downloading evaluations CSV:', error);
      showAlert({
        title: t('common.error', 'Error'),
        message: t(
          'evaluations.downloadError',
          'Failed to download evaluations CSV.'
        ),
        variant: 'danger',
      });
    }
  };


  return (
    <form
      className="profile-evaluations-filter row row-cols-lg-auto g-2 align-items-center mb-3 small"
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
          <FontAwesomeIcon icon={faFileCsv} size="lg" />
        </button>

        {/* Excel Download button */}
        <button
          type="button"
          className="btn btn-link p-0"
          title={t('evaluations.title') + ' Excel'}
          aria-label={t('evaluations.title') + ' Excel'}
        >
          <FontAwesomeIcon icon={faFileExcel} size="lg" />
        </button>
      </div>

      {/* Modular filter components */}
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
        startDate={filters.startDateDisplay}
        endDate={filters.endDateDisplay}
        onChange={handleChange}
      />
    </form>
  );
};

ProfileEvaluationsFilter.propTypes = {
  filters: PropTypes.shape({
    evaluatorId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    evaluationScale: PropTypes.string,
    searchText: PropTypes.string,
    evaluationSortField: PropTypes.string,
    ascending: PropTypes.bool,
    startDateDisplay: PropTypes.string,
    endDateDisplay: PropTypes.string,
  }).isRequired,
  setFilters: PropTypes.func.isRequired,
  displayedUserId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
};

export default ProfileEvaluationsFilter;

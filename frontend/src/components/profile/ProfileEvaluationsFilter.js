import React from 'react';
import PropTypes from 'prop-types';
import { Button, Row, Col, Form } from 'react-bootstrap';
import { exportService } from 'services/exportService';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { SearchableUserSelect } from 'components/evaluations/filters/SearchableUserSelect';
import { EvaluationScaleSelect } from 'components/evaluations/filters/EvaluationScaleSelect';
import { EvaluationSortSelect } from 'components/evaluations/filters/EvaluationSortSelect';
import { EvaluationDateRange } from 'components/evaluations/filters/EvaluationDateRange';

// Accept displayedUserId as a prop to filter out the evaluator dropdown

// Accept displayedUserId as a prop to filter out the evaluator dropdown
export const ProfileEvaluationsFilter = ({
  filters,
  setFilters,
  displayedUserId,
}) => {
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
    <div className="profile-evaluations-filter">
      {/* Export buttons */}
      <div className="d-flex justify-content-end gap-2 mb-3">
        <Button
          variant="outline-primary"
          size="sm"
          onClick={handleDownloadAll}
          title={t('evaluations.title') + ' CSV'}
        >
          <i className="fas fa-file-csv me-1" />
          CSV
        </Button>
        <Button
          variant="outline-success"
          size="sm"
          title={t('evaluations.title') + ' Excel'}
          disabled
        >
          <i className="fas fa-file-excel me-1" />
          Excel
        </Button>
      </div>

      {/* Filter Form */}
      <Form>
        <Row className="g-3 align-items-end">
          {/* Evaluator Select */}
          <Col md={3}>
            <SearchableUserSelect
              value={filters.evaluatorId}
              onChange={handleChange}
              excludeUserId={displayedUserId}
              name="evaluatorId"
              label={t('profile.evaluator')}
              placeholder={t('common.search')}
            />
          </Col>

          {/* Evaluation Scale */}
          <Col md={2}>
            <EvaluationScaleSelect
              value={filters.evaluationScale}
              onChange={handleChange}
              name="evaluationScale"
            />
          </Col>

          {/* Search Text */}
          <Col md={2}>
            <Form.Group>
              <Form.Label className="small fw-semibold">
                {t('common.search')}
              </Form.Label>
              <Form.Control
                type="text"
                name="searchText"
                value={filters.searchText || ''}
                onChange={handleChange}
                placeholder={t('common.search')}
              />
            </Form.Group>
          </Col>

          {/* Sort Field */}
          <Col md={2}>
            <EvaluationSortSelect
              value={filters.evaluationSortField}
              onChange={handleChange}
              name="evaluationSortField"
            />
          </Col>

          {/* Ascending Toggle */}
          <Col md="auto">
            <Form.Group>
              <Form.Label className="small fw-semibold d-block">
                {t('filters.ascending')}
              </Form.Label>
              <Button
                variant={
                  filters.ascending ? 'outline-primary' : 'outline-secondary'
                }
                size="sm"
                onClick={() =>
                  handleChange({
                    target: {
                      name: 'ascending',
                      value: !filters.ascending,
                      type: 'checkbox',
                      checked: !filters.ascending,
                    },
                  })
                }
                title={
                  filters.ascending
                    ? t('common.ascending')
                    : t('common.descending')
                }
              >
                <i
                  className={`bi bi-sort-${filters.ascending ? 'up' : 'down'}`}
                />
              </Button>
            </Form.Group>
          </Col>

          {/* Date Range */}
          <Col md={4}>
            <EvaluationDateRange
              startDate={filters.startDateDisplay}
              endDate={filters.endDateDisplay}
              onChange={handleChange}
            />
          </Col>
        </Row>
      </Form>
    </div>
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

import React from 'react';
import PropTypes from 'prop-types';
import { Button, Row, Col, Form, InputGroup } from 'react-bootstrap';
import { exportService } from 'services/exportService';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { SearchableUserSelect } from 'components/evaluations/filters/SearchableUserSelect';

export const ProfileEvaluationsFilter = ({
  filters,
  setFilters,
  displayedUserId,
}) => {
  const { t } = useTranslation();
  const { showAlert } = useAlert();

  // Unified handler for all filter components
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
      // For date fields, convert to ISO format with start/end of day times
      if (name === 'startDate') {
        if (value) {
          // Convert YYYY-MM-DD to ISO format with start of day
          next.startDate = `${value}T00:00:00.000Z`;
          next.startDateDisplay = value;
        } else {
          delete next.startDate;
          delete next.startDateDisplay;
        }
      } else if (name === 'endDate') {
        if (value) {
          // Convert YYYY-MM-DD to ISO format with end of day
          next.endDate = `${value}T23:59:59.999Z`;
          next.endDateDisplay = value;
        } else {
          delete next.endDate;
          delete next.endDateDisplay;
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

  const handleClearFilters = () => {
    setFilters({ evaluatedId: displayedUserId });
  };

  const scaleOptions = [
    { value: 'LOW_CONTRIBUTION', label: t('evaluationScale.low') },
    { value: 'PARCIAL_CONTRIBUTION', label: t('evaluationScale.partial') },
    { value: 'EXPECTED_CONTRIBUTION', label: t('evaluationScale.expected') },
    { value: 'EXCEED_CONTRIBUTION', label: t('evaluationScale.exceed') },
  ];

  const sortOptions = [
    { value: 'EVALUATION_ID', label: t('profile.sortFields.id') },
    { value: 'EVALUATOR_ID', label: t('profile.sortFields.evaluator') },
    { value: 'SCALE', label: t('filters.scale') },
  ];

  const hasActiveFilters =
    Object.keys(filters).filter(
      key =>
        key !== 'evaluatedId' &&
        filters[key] !== '' &&
        filters[key] !== null &&
        filters[key] !== undefined
    ).length > 0;

  return (
    <div>
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
        {/* Excell export button }
                <Button
          variant="outline-success"
          size="sm"
          title={t('evaluations.title') + ' Excel'}
          disabled
        >
          <i className="fas fa-file-excel me-1" />
          Excel
        </Button> */}
      </div>

      <div className="d-flex justify-content-between align-items-center mb-3">
        <h6 className="mb-0">
          <i className="bi bi-funnel me-2"></i>
          {t('common.filters')}
        </h6>
        {hasActiveFilters && (
          <Button
            variant="outline-secondary"
            size="sm"
            onClick={handleClearFilters}
          >
            <i className="bi bi-x-circle me-1"></i>
            {t('common.clearFilters')}
          </Button>
        )}
      </div>

      <Row className="g-3">
        {/* Search Text */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('evaluations.searchFeedback', 'Search in Feedback')}
            </Form.Label>
            <InputGroup>
              <InputGroup.Text>
                <i className="bi bi-search"></i>
              </InputGroup.Text>
              <Form.Control
                type="text"
                name="searchText"
                placeholder={t(
                  'evaluations.searchFeedbackPlaceholder',
                  'Search evaluation feedback...'
                )}
                value={filters.searchText || ''}
                onChange={handleChange}
              />
            </InputGroup>
          </Form.Group>
        </Col>

        {/* Evaluator */}
        <Col md={6} lg={4}>
          <Form.Group>
            <SearchableUserSelect
              value={filters.evaluatorId || ''}
              onChange={handleChange}
              excludeUserId={displayedUserId}
              label={t('filters.evaluator')}
              name="evaluatorId"
              placeholder={t('common.all')}
            />
          </Form.Group>
        </Col>

        {/* Scale */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('evaluationScale.title')}
            </Form.Label>
            <Form.Select
              name="evaluationScale"
              value={filters.evaluationScale || ''}
              onChange={handleChange}
            >
              <option value="">{t('evaluationScale.any')}</option>
              {scaleOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>

        {/* Date Range Group */}
        <Col md={12} lg={8}>
          <div className="border rounded p-3 bg-light">
            <h6 className="mb-3 text-muted">
              <i className="bi bi-calendar-range me-2"></i>
              {t(
                'filters.rangeByCycleClosureDate',
                'Range by evaluation cycle closure'
              )}
            </h6>
            <Row className="g-2">
              <Col md={6}>
                <Form.Group>
                  <Form.Label className="small fw-semibold">
                    {t('filters.startDate')}
                  </Form.Label>
                  <Form.Control
                    type="date"
                    name="startDate"
                    value={filters.startDateDisplay || ''}
                    onChange={handleChange}
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group>
                  <Form.Label className="small fw-semibold">
                    {t('filters.endDate')}
                  </Form.Label>
                  <Form.Control
                    type="date"
                    name="endDate"
                    value={filters.endDateDisplay || ''}
                    onChange={handleChange}
                  />
                </Form.Group>
              </Col>
            </Row>
          </div>
        </Col>

        {/* Sort */}
        <Col md={12} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('common.sortBy')}
            </Form.Label>
            <Row className="g-2">
              <Col>
                <Form.Select
                  name="evaluationSortField"
                  value={filters.evaluationSortField || 'DATE'}
                  onChange={handleChange}
                >
                  {sortOptions.map(option => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </Form.Select>
              </Col>
              <Col xs="auto">
                <Button
                  variant={
                    filters.ascending ? 'outline-primary' : 'outline-secondary'
                  }
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
                    className={`bi bi-sort-${
                      filters.ascending ? 'up' : 'down'
                    }`}
                  ></i>
                </Button>
              </Col>
            </Row>
          </Form.Group>
        </Col>
      </Row>
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

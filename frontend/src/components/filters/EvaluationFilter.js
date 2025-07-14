import React, { useState, useEffect } from 'react';
import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import { SearchableUserSelect } from 'components/evaluations/filters/SearchableUserSelect';
import { useUserList } from 'hooks/useUserList';
import { exportService } from 'services/exportService';
import { translateEvaluationScale } from 'utils/evaluationScaleTranslations';

/**
 * Modern evaluation filter component for filtering evaluation list
 * Unified component with modern design, searchable dropdowns, and export features
 * @param {Object} props
 * @param {Object} props.filters - Current filter values
 * @param {Function} props.onFiltersChange - Filter change handler (legacy)
 * @param {Function} props.setFilters - Filter change handler (new)
 * @param {Object} props.user - Current user object
 * @param {string|number} props.displayedUserId - User ID to exclude from dropdown
 * @param {React.ReactNode} props.extraFields - Additional filter fields
 * @returns {React.Component} Modern evaluation filter form
 */
export const EvaluationFilter = ({
  filters,
  onFiltersChange,
  setFilters,
  user,
  displayedUserId,
  extraFields,
}) => {
  const { t, i18n } = useTranslation();
  const { userList } = useUserList();
  const [searchTerm, setSearchTerm] = useState(filters.searchText || '');

  const handleFilterChange = (field, value) => {
    // Handle special case for search term with debouncing
    if (field === 'searchText') {
      setSearchTerm(value);
      return;
    }

    if (setFilters) {
      // New pattern: use setFilters function
      if (field === 'startDate' || field === 'endDate') {
        setFilters(prev => ({ ...prev, [field]: value }));
      } else {
        setFilters(prev => ({
          ...prev,
          [field]: typeof value === 'boolean' ? value : value,
        }));
      }
    } else {
      // Legacy pattern: use onFiltersChange
      const newFilters = { ...filters, [field]: value };

      // Remove empty filters
      Object.keys(newFilters).forEach(key => {
        if (
          newFilters[key] === '' ||
          newFilters[key] === null ||
          newFilters[key] === undefined
        ) {
          delete newFilters[key];
        }
      });

      onFiltersChange(newFilters);
    }
  };

  // Handle changes from SearchableUserSelect
  const handleUserSelectChange = e => {
    const { name, value } = e.target;
    handleFilterChange(name, value);
  };

  // Debounce search input to prevent immediate refresh
  useEffect(() => {
    const delayedSearch = setTimeout(() => {
      if (searchTerm !== filters.searchText) {
        if (setFilters) {
          setFilters(prev => ({ ...prev, searchText: searchTerm }));
        } else if (onFiltersChange) {
          // Use the legacy method without circular call
          const newFilters = { ...filters, searchText: searchTerm };
          Object.keys(newFilters).forEach(key => {
            if (
              newFilters[key] === '' ||
              newFilters[key] === null ||
              newFilters[key] === undefined
            ) {
              delete newFilters[key];
            }
          });
          onFiltersChange(newFilters);
        }
      }
    }, 750); // 750ms delay for search

    return () => clearTimeout(delayedSearch);
  }, [searchTerm, setFilters, onFiltersChange, filters.searchText]);

  // Update local search term when filters change externally (only if not actively typing)
  useEffect(() => {
    if (
      filters.searchText !== searchTerm &&
      document.activeElement?.name !== 'searchText'
    ) {
      setSearchTerm(filters.searchText || '');
    }
  }, [filters.searchText]);

  // If manager, always set evaluatorId to himself
  useEffect(() => {
    if (
      user?.role === 'MANAGER' &&
      user?.id &&
      filters.evaluatorId !== user.id
    ) {
      if (setFilters) {
        setFilters(prev => ({ ...prev, evaluatorId: user.id }));
      }
    }
  }, [user?.role, user?.id, setFilters, filters.evaluatorId]);

  // Download all evaluations handler (CSV)
  const handleDownloadAll = async () => {
    try {
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
      if (typeof window !== 'undefined' && window.alert) {
        // eslint-disable-next-line no-alert
        window.alert('Failed to download evaluations CSV.');
      }
    }
  };

  // Download all evaluations handler (Excel) - placeholder
  const handleDownloadExcel = () => {
    // TODO: Implement Excel export if needed
    if (typeof window !== 'undefined' && window.alert) {
      // eslint-disable-next-line no-alert
      window.alert('Excel export not implemented.');
    }
  };

  const handleClearFilters = () => {
    setSearchTerm('');
    if (setFilters) {
      setFilters({});
    } else {
      onFiltersChange({});
    }
  };

  const statusOptions = [
    { value: 'ONGOING', label: t('evaluationStatus.ONGOING') },
    { value: 'CLOSED', label: t('evaluationStatus.CLOSED') },
    { value: 'COMPLETED', label: t('evaluationStatus.COMPLETED') },
  ];

  const scaleOptions = [
    { value: 'LOW_CONTRIBUTION', label: t('evaluationScale.low') },
    { value: 'PARCIAL_CONTRIBUTION', label: t('evaluationScale.partial') },
    { value: 'EXPECTED_CONTRIBUTION', label: t('evaluationScale.expected') },
    { value: 'EXCEED_CONTRIBUTION', label: t('evaluationScale.exceed') },
  ];

  const sortOptions = [
    { value: 'EVALUATION_ID', label: t('profile.sortFields.id') },
    { value: 'EVALUATED_ID', label: t('profile.sortFields.evaluated') },
    { value: 'EVALUATOR_ID', label: t('profile.sortFields.evaluator') },
    { value: 'STATUS', label: t('profile.sortFields.status') },
    { value: 'DATE', label: t('filters.creationDate') },
    { value: 'SCALE', label: t('filters.scale') },
  ];

  const hasActiveFilters = Object.keys(filters).length > 0;

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
        <Button
          variant="outline-success"
          size="sm"
          onClick={handleDownloadExcel}
          title={t('evaluations.title') + ' Excel'}
        >
          <i className="fas fa-file-excel me-1" />
          Excel
        </Button>
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
                value={searchTerm}
                onChange={e => setSearchTerm(e.target.value)}
              />
            </InputGroup>
          </Form.Group>
        </Col>

        {/* Evaluator (only for admin) */}
        {user?.role === 'ADMIN' && (
          <Col md={6} lg={4}>
            <Form.Group>
              <SearchableUserSelect
                value={filters.evaluatorId || ''}
                onChange={handleUserSelectChange}
                excludeUserId={displayedUserId}
                label={t('filters.evaluator')}
                name="evaluatorId"
                placeholder={t('common.all')}
              />
            </Form.Group>
          </Col>
        )}

        {/* Evaluatee */}
        <Col md={6} lg={4}>
          <Form.Group>
            <SearchableUserSelect
              value={filters.evaluatedId || ''}
              onChange={handleUserSelectChange}
              excludeUserId={null}
              label={t('filters.evaluatee')}
              name="evaluatedId"
              placeholder={t('common.all')}
            />
          </Form.Group>
        </Col>

        {/* Status */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('evaluationStatus.title')}
            </Form.Label>
            <Form.Select
              value={filters.status || ''}
              onChange={e => handleFilterChange('status', e.target.value)}
            >
              <option value="">{t('common.all')}</option>
              {statusOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>

        {/* Scale */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('evaluationScale.title')}
            </Form.Label>
            <Form.Select
              value={filters.evaluationScale || ''}
              onChange={e =>
                handleFilterChange('evaluationScale', e.target.value)
              }
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

        {/* Sort */}
        <Col md={12} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('common.sortBy')}
            </Form.Label>
            <Row className="g-2">
              <Col>
                <Form.Select
                  value={filters.evaluationSortField || 'DATE'}
                  onChange={e =>
                    handleFilterChange('evaluationSortField', e.target.value)
                  }
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
                    handleFilterChange('ascending', !filters.ascending)
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

      {/* Date Range Filter Box - now full width and separated below */}
      <Row className="mt-4">
        <Col xs={12}>
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
                    onChange={e => {
                      const value = e.target.value;
                      setFilters(prev => {
                        const next = { ...prev };
                        if (value) {
                          next.startDate = `${value}T00:00:00.000Z`;
                          next.startDateDisplay = value;
                        } else {
                          delete next.startDate;
                          delete next.startDateDisplay;
                        }
                        return next;
                      });
                    }}
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
                    onChange={e => {
                      const value = e.target.value;
                      setFilters(prev => {
                        const next = { ...prev };
                        if (value) {
                          next.endDate = `${value}T23:59:59.999Z`;
                          next.endDateDisplay = value;
                        } else {
                          delete next.endDate;
                          delete next.endDateDisplay;
                        }
                        return next;
                      });
                    }}
                  />
                </Form.Group>
              </Col>
            </Row>
          </div>
        </Col>
      </Row>

      {/* Active Filters Summary */}
      {hasActiveFilters && (
        <div className="mt-3 pt-3 border-top">
          <div className="d-flex flex-wrap gap-2">
            <small className="text-muted align-self-center">
              {t('common.activeFilters')}:
            </small>
            {Object.entries(filters).map(([key, value]) => {
              // Hide startDateDisplay and endDateDisplay from summary
              if (!value && value !== false) return null;
              if (key === 'startDateDisplay' || key === 'endDateDisplay')
                return null;

              let displayValue = value;
              if (key === 'status') {
                displayValue = t(`evaluationStatus.${value}`, value);
              } else if (key === 'evaluationScale') {
                displayValue = translateEvaluationScale(value, i18n.language);
              } else if (key === 'evaluatorId' && userList) {
                const userItem = userList.find(
                  u => String(u.id) === String(value)
                );
                displayValue = userItem
                  ? `${userItem.firstName} ${userItem.lastName}`
                  : value;
              } else if (key === 'evaluatedId' && userList) {
                const userItem = userList.find(
                  u => String(u.id) === String(value)
                );
                displayValue = userItem
                  ? `${userItem.firstName} ${userItem.lastName}`
                  : value;
              } else if (key === 'startDate') {
                displayValue = filters.startDateDisplay || value;
              } else if (key === 'endDate') {
                displayValue = filters.endDateDisplay || value;
              }

              return (
                <span key={key} className="badge bg-light text-dark border">
                  {t(`filters.${key}`, key)}: {displayValue}
                  <button
                    type="button"
                    className="btn-close btn-close-sm ms-1"
                    aria-label="Remove filter"
                    onClick={() => handleFilterChange(key, '')}
                    style={{ fontSize: '0.7em' }}
                  ></button>
                </span>
              );
            })}
          </div>
        </div>
      )}

      {/* Extra fields */}
      {extraFields}
    </div>
  );
};

EvaluationFilter.propTypes = {
  filters: PropTypes.object.isRequired,
  onFiltersChange: PropTypes.func, // Legacy prop
  setFilters: PropTypes.func, // New prop
  user: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    role: PropTypes.string,
  }),
  displayedUserId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  extraFields: PropTypes.node,
};

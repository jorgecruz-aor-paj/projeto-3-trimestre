import React from 'react';
import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';

/**
 * Training filter component for filtering training list
 * @param {Object} props
 * @param {Object} props.filters - Current filter values
 * @param {Function} props.onFiltersChange - Filter change handler
 * @param {Array} props.trainingAreas - Available training areas
 * @returns {React.Component} Training filter form
 */
export const TrainingFilter = ({
  filters,
  onFiltersChange,
  trainingAreas = [],
}) => {
  const { t } = useTranslation();

  const handleFilterChange = (field, value) => {
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
  };

  const handleClearFilters = () => {
    onFiltersChange({});
  };

  const commonLanguages = [
    'English',
    'Portuguese',
    'Spanish',
    'French',
    'German',
    'Italian',
    'Dutch',
    'Other',
  ];

  const sortOptions = [
    { value: 'TITLE', label: t('trainings.sort.title') },
    { value: 'DATE', label: t('trainings.sort.date') },
    { value: 'DURATION', label: t('trainings.sort.duration') },
    { value: 'LANGUAGE', label: t('trainings.sort.language') },
    { value: 'AREA', label: t('trainings.sort.area') },
  ];

  const hasActiveFilters = Object.keys(filters).length > 0;

  return (
    <div>
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
              {t('common.search')}
            </Form.Label>
            <InputGroup>
              <InputGroup.Text>
                <i className="bi bi-search"></i>
              </InputGroup.Text>
              <Form.Control
                type="text"
                placeholder={t('trainings.filter.searchPlaceholder')}
                value={filters.searchText || ''}
                onChange={e => handleFilterChange('searchText', e.target.value)}
              />
            </InputGroup>
          </Form.Group>
        </Col>

        {/* Training Area */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('trainings.fields.area')}
            </Form.Label>
            <Form.Select
              value={filters.trainingAreaId || ''}
              onChange={e =>
                handleFilterChange('trainingAreaId', e.target.value)
              }
            >
              <option value="">{t('common.all')}</option>
              {trainingAreas.map(area => (
                <option key={area.id} value={area.id}>
                  {area.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>

        {/* Language */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('trainings.fields.language')}
            </Form.Label>
            <Form.Select
              value={filters.language || ''}
              onChange={e => handleFilterChange('language', e.target.value)}
            >
              <option value="">{t('common.all')}</option>
              {commonLanguages.map(lang => (
                <option key={lang} value={lang}>
                  {lang}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>

        {/* Duration Range */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('trainings.fields.duration')} ({t('common.minutes')})
            </Form.Label>
            <Form.Control
              type="number"
              placeholder={t('trainings.filter.durationPlaceholder')}
              value={filters.duration || ''}
              onChange={e => handleFilterChange('duration', e.target.value)}
              min="1"
            />
          </Form.Group>
        </Col>

        {/* Status */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('common.status')}
            </Form.Label>
            <Form.Select
              value={
                filters.activeOnly !== undefined
                  ? filters.activeOnly.toString()
                  : 'true'
              }
              onChange={e => {
                const value = e.target.value;
                if (value === 'all') {
                  handleFilterChange('activeOnly', undefined);
                } else {
                  handleFilterChange('activeOnly', value === 'true');
                }
              }}
            >
              <option value="true">{t('common.active')}</option>
              <option value="false">{t('common.inactive')}</option>
              <option value="all">{t('common.all')}</option>
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
                  value={filters.sortField || 'DATE'}
                  onChange={e =>
                    handleFilterChange('sortField', e.target.value)
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
                    filters.sortAscending
                      ? 'outline-primary'
                      : 'outline-secondary'
                  }
                  onClick={() =>
                    handleFilterChange('sortAscending', !filters.sortAscending)
                  }
                  title={
                    filters.sortAscending
                      ? t('common.ascending')
                      : t('common.descending')
                  }
                >
                  <i
                    className={`bi bi-sort-${
                      filters.sortAscending ? 'up' : 'down'
                    }`}
                  ></i>
                </Button>
              </Col>
            </Row>
          </Form.Group>
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
              if (!value && value !== false) return null;

              let displayValue = value;
              if (key === 'trainingAreaId') {
                const area = trainingAreas.find(a => a.id === parseInt(value));
                displayValue = area?.name || value;
              } else if (key === 'activeOnly') {
                displayValue = value
                  ? t('common.active')
                  : t('common.inactive');
              }

              return (
                <span key={key} className="badge bg-light text-dark border">
                  {t(`trainings.fields.${key}`, key)}: {displayValue}
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
    </div>
  );
};

TrainingFilter.propTypes = {
  filters: PropTypes.object.isRequired,
  onFiltersChange: PropTypes.func.isRequired,
  trainingAreas: PropTypes.array,
};

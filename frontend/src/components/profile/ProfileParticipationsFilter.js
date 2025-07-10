import React from 'react';
import PropTypes from 'prop-types';
import { Button, Row, Col, Form, InputGroup } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

export const ProfileParticipationsFilter = ({ filters, setFilters }) => {
  const { t } = useTranslation();

  // Handle filter changes
  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => {
      const next = { ...prev, [name]: type === 'checkbox' ? checked : value };
      // Remove empty string filter keys for select fields
      if ((name === 'status' || name === 'sortField') && !value) {
        delete next[name];
      }
      return next;
    });
  };

  const handleClearFilters = () => {
    setFilters({ participantId: filters.participantId });
  };

  const statusOptions = [
    { value: 'REGISTERED', label: t('participations.status.registered') },
    { value: 'ATTENDED', label: t('participations.status.attended') },
    { value: 'COMPLETED', label: t('participations.status.completed') },
    { value: 'CANCELLED', label: t('participations.status.cancelled') },
  ];

  const sortOptions = [
    { value: 'TRAINING_TITLE', label: t('participations.sort.trainingTitle') },
    { value: 'STATUS', label: t('participations.sort.status') },
    {
      value: 'REGISTRATION_DATE',
      label: t('participations.sort.registrationDate'),
    },
    {
      value: 'COMPLETION_DATE',
      label: t('participations.sort.completionDate'),
    },
  ];

  const hasActiveFilters =
    Object.keys(filters).filter(
      key =>
        key !== 'participantId' &&
        filters[key] !== '' &&
        filters[key] !== null &&
        filters[key] !== undefined
    ).length > 0;

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
                name="searchText"
                value={filters.searchText || ''}
                onChange={handleChange}
                placeholder={t(
                  'participations.searchPlaceholder',
                  'Search trainings...'
                )}
              />
            </InputGroup>
          </Form.Group>
        </Col>

        {/* Status Filter */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('participations.status.title', 'Status')}
            </Form.Label>
            <Form.Select
              name="status"
              value={filters.status || ''}
              onChange={handleChange}
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

        {/* Sort */}
        <Col md={12} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('common.sortBy', 'Sort By')}
            </Form.Label>
            <Row className="g-2">
              <Col>
                <Form.Select
                  name="sortField"
                  value={filters.sortField || 'REGISTRATION_DATE'}
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

ProfileParticipationsFilter.propTypes = {
  filters: PropTypes.object.isRequired,
  setFilters: PropTypes.func.isRequired,
};

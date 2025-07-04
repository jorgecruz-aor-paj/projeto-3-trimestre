import React from 'react';
import PropTypes from 'prop-types';
import { Button, Row, Col, Form } from 'react-bootstrap';
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

  const statusOptions = [
    { value: '', label: t('common.all') },
    { value: 'REGISTERED', label: t('participations.status.registered') },
    { value: 'ATTENDED', label: t('participations.status.attended') },
    { value: 'COMPLETED', label: t('participations.status.completed') },
    { value: 'CANCELLED', label: t('participations.status.cancelled') },
  ];

  const sortOptions = [
    { value: '', label: t('common.default') },
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

  return (
    <div className="profile-participations-filter">
      {/* Filter Form */}
      <Form>
        <Row className="g-3 align-items-end">
          {/* Search Text */}
          <Col md={3}>
            <Form.Group>
              <Form.Label className="small fw-semibold">
                {t('common.search')}
              </Form.Label>
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
            </Form.Group>
          </Col>

          {/* Status Filter */}
          <Col md={2}>
            <Form.Group>
              <Form.Label className="small fw-semibold">
                {t('participations.status.title', 'Status')}
              </Form.Label>
              <Form.Select
                name="status"
                value={filters.status || ''}
                onChange={handleChange}
              >
                {statusOptions.map(option => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
          </Col>

          {/* Sort Field */}
          <Col md={2}>
            <Form.Group>
              <Form.Label className="small fw-semibold">
                {t('common.sortBy', 'Sort By')}
              </Form.Label>
              <Form.Select
                name="sortField"
                value={filters.sortField || ''}
                onChange={handleChange}
              >
                {sortOptions.map(option => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
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
        </Row>
      </Form>
    </div>
  );
};

ProfileParticipationsFilter.propTypes = {
  filters: PropTypes.object.isRequired,
  setFilters: PropTypes.func.isRequired,
};

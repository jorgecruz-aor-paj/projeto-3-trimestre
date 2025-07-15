import React, { useEffect, useState } from 'react';
import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap';
import { useWorkplaces } from 'hooks/useWorkplaces';
import { useTranslation } from 'react-i18next';
import { exportService } from 'services/exportService';
import { SearchableUserSelect } from 'components/evaluations/filters/SearchableUserSelect';
import PropTypes from 'prop-types';

const sortFields = [
  { value: 'WORKPLACE', labelKey: 'user.workplace' },
  { value: 'MANAGER', labelKey: 'usersPage.filters.manager' },
  { value: 'DATE_OF_BIRTH', labelKey: 'user.birthdate' },
  { value: 'DATE', labelKey: 'usersPage.filters.createdAt' },
];

/**
 * Modern user filter component for filtering user list
 * @param {Object} props
 * @param {Object} props.filters - Current filter values
 * @param {Function} props.setFilters - Filter change handler
 * @returns {React.Component} Modern user filter form
 */
export const UserFilters = ({ filters, setFilters }) => {
  const { t } = useTranslation();
  const { workplaces } = useWorkplaces();
  const [searchTerm, setSearchTerm] = useState(filters.searchText || '');

  const handleFilterChange = (field, value) => {
    // Handle special case for search term with debouncing
    if (field === 'searchText') {
      setSearchTerm(value);
      return;
    }

    setFilters(prev => ({
      ...prev,
      [field]: value,
    }));
  };

  // Debounce search input to prevent immediate refresh
  useEffect(() => {
    const delayedSearch = setTimeout(() => {
      if (searchTerm !== filters.searchText) {
        setFilters(prev => ({ ...prev, searchText: searchTerm }));
      }
    }, 750); // 750ms delay for search

    return () => clearTimeout(delayedSearch);
  }, [searchTerm, setFilters, filters.searchText]);

  // Download all evaluations handler (Excel) - placeholder
  const handleDownloadExcel = () => {
    // TODO: Implement Excel export if needed
    if (typeof window !== 'undefined' && window.alert) {
      // eslint-disable-next-line no-alert
      window.alert('Excel export not implemented.');
    }
  };

  // Update local search term when filters change externally (only if not actively typing)
  useEffect(() => {
    if (
      filters.searchText !== searchTerm &&
      document.activeElement?.name !== 'searchText'
    ) {
      setSearchTerm(filters.searchText || '');
    }
  }, [filters.searchText]);

  // Download users as CSV
  const handleDownloadCSV = async () => {
    try {
      const response = await exportService.usersToCSVFiltered(filters);
      let filename = 'users.csv';
      if (response?.headers?.['content-disposition']) {
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
      console.error('Error downloading users CSV:', error);
      if (typeof window !== 'undefined' && window.alert) {
        // eslint-disable-next-line no-alert
        window.alert('Failed to download users CSV.');
      }
    }
  };

  const handleClearFilters = () => {
    setSearchTerm('');
    setFilters({});
  };

  const hasActiveFilters = Object.keys(filters).length > 0;

  return (
    <div>
      {/* Export buttons */}
      <div className="d-flex justify-content-end gap-2 mb-3">
        <Button
          variant="outline-primary"
          size="sm"
          onClick={handleDownloadCSV}
          title={t('usersPage.title') + ' CSV'}
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
          {t('common.filters', 'Filters')}
        </h6>
        {hasActiveFilters && (
          <Button
            variant="outline-secondary"
            size="sm"
            onClick={handleClearFilters}
          >
            <i className="bi bi-x-circle me-1"></i>
            {t('common.clearFilters', 'Clear Filters')}
          </Button>
        )}
      </div>

      <Row className="g-3">
        {/* Search Text */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('common.search', 'Search')}
            </Form.Label>
            <InputGroup>
              <InputGroup.Text>
                <i className="bi bi-search"></i>
              </InputGroup.Text>
              <Form.Control
                type="text"
                name="searchText"
                placeholder={t('usersPage.filters.search', 'Search users...')}
                value={searchTerm}
                onChange={e => setSearchTerm(e.target.value)}
              />
            </InputGroup>
          </Form.Group>
        </Col>

        {/* Workplace */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('user.workplace', 'Workplace')}
            </Form.Label>
            <Form.Select
              value={filters.workplaceId || ''}
              onChange={e => handleFilterChange('workplaceId', e.target.value)}
            >
              <option value="">{t('common.all', 'All')}</option>
              {workplaces.map(w => (
                <option key={w.id} value={w.id}>
                  {w.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>

        {/* Manager */}
        <Col md={6} lg={4}>
          <SearchableUserSelect
            value={filters.managerId || ''}
            onChange={e => handleFilterChange('managerId', e.target.value)}
            name="managerId"
            label={t('usersPage.filters.manager', 'Manager')}
            placeholder={t('usersPage.filters.manager', 'Search manager...')}
            includeAdmins={false}
          />
        </Col>

        {/* Sort */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('common.sortBy', 'Sort By')}
            </Form.Label>
            <Row className="g-2">
              <Col>
                <Form.Select
                  value={filters.sortField || ''}
                  onChange={e =>
                    handleFilterChange('sortField', e.target.value)
                  }
                >
                  <option value="">{t('common.default', 'Default')}</option>
                  {sortFields.map(f => (
                    <option key={f.value} value={f.value}>
                      {t(f.labelKey)}
                    </option>
                  ))}
                </Form.Select>
              </Col>
              <Col xs="auto">
                <Button
                  variant={
                    filters.ascending !== false
                      ? 'outline-primary'
                      : 'outline-secondary'
                  }
                  onClick={() =>
                    handleFilterChange(
                      'ascending',
                      !(filters.ascending !== false)
                    )
                  }
                  title={
                    filters.ascending !== false
                      ? t('common.ascending', 'Ascending')
                      : t('common.descending', 'Descending')
                  }
                >
                  <i
                    className={`bi bi-sort-${
                      filters.ascending !== false ? 'up' : 'down'
                    }`}
                  ></i>
                </Button>
              </Col>
            </Row>
          </Form.Group>
        </Col>

        {/* No Manager Switch */}
        <Col md={6} lg={4}>
          <Form.Group>
            <Form.Label className="small fw-semibold">
              {t('usersPage.filters.options', 'Options')}
            </Form.Label>
            <div className="mt-2">
              <Form.Check
                type="switch"
                id="noManagerSwitch"
                label={t('usersPage.filters.noManager', 'No Manager')}
                checked={!!filters.noManager}
                onChange={e =>
                  handleFilterChange('noManager', e.target.checked)
                }
              />
            </div>
          </Form.Group>
        </Col>
      </Row>
    </div>
  );
};

UserFilters.propTypes = {
  filters: PropTypes.object.isRequired,
  setFilters: PropTypes.func.isRequired,
};

import React, { useEffect } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { useWorkplaces } from 'hooks/useWorkplaces';
import { useManagersStore } from 'stores/ManagersStore';
import { useTranslation } from 'react-i18next';
import { exportService } from 'services/exportService';

const sortFields = [
  { value: 'WORKPLACE', labelKey: 'user.workplace' },
  { value: 'MANAGER', labelKey: 'usersPage.filters.manager' },
  { value: 'DATE_OF_BIRTH', labelKey: 'user.birthdate' },
  { value: 'DATE', labelKey: 'usersPage.filters.createdAt' },
];

export const UserFilters = ({ filters, onChange }) => {
  const { t } = useTranslation();
  const { workplaces } = useWorkplaces();
  const { managers, fetchManagers } = useManagersStore();

  useEffect(() => {
    fetchManagers();
  }, [fetchManagers]);

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

  return (
    <Form className="mb-4">
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
      </div>

      <Row className="align-items-end">
        <Col md={3} className="mb-2">
          <Form.Control
            type="text"
            name="searchText"
            placeholder={t('usersPage.filters.search')}
            value={filters.searchText || ''}
            onChange={onChange}
          />
        </Col>
        <Col md={3} className="mb-2">
          <Form.Select
            name="workplaceId"
            value={filters.workplaceId || ''}
            onChange={onChange}
          >
            <option value="">{t('usersPage.filters.workplace')}</option>
            {workplaces.map(w => (
              <option key={w.id} value={w.id}>
                {w.name}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={3} className="mb-2">
          <Form.Select
            name="managerId"
            value={filters.managerId || ''}
            onChange={onChange}
          >
            <option value="">{t('usersPage.filters.manager')}</option>
            {managers.map(m => (
              <option key={m.id} value={m.id}>
                {m.firstName} {m.lastName}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={2} className="mb-2">
          <Form.Select
            name="sortField"
            value={filters.sortField || ''}
            onChange={onChange}
          >
            <option value="">{t('usersPage.filters.sortBy')}</option>
            {sortFields.map(f => (
              <option key={f.value} value={f.value}>
                {t(f.labelKey)}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={1} className="mb-2">
          <Form.Check
            type="switch"
            id="noManagerSwitch"
            name="noManager"
            label={t('usersPage.filters.noManager')}
            checked={!!filters.noManager}
            onChange={e =>
              onChange({
                target: {
                  name: 'noManager',
                  value: e.target.checked,
                },
              })
            }
          />
        </Col>
        <Col md={2} className="mb-2">
          <Form.Check
            type="switch"
            id="ascendingSwitch"
            name="ascending"
            label={
              filters.ascending !== false
                ? t('usersPage.filters.ascending')
                : t('usersPage.filters.descending')
            }
            checked={filters.ascending !== false}
            onChange={e =>
              onChange({
                target: {
                  name: 'ascending',
                  value: e.target.checked,
                },
              })
            }
          />
        </Col>
      </Row>
    </Form>
  );
};

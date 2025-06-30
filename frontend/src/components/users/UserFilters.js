import React, { useEffect } from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { useWorkplaces } from 'hooks/useWorkplaces';
import { useManagersStore } from 'stores/ManagersStore';
import { useTranslation } from 'react-i18next';

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

  return (
    <Form className="mb-4">
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

import React, { useEffect } from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import { useWorkplaces } from 'hooks/useWorkplaces';
import { useManagersStore } from 'stores/ManagersStore';

const sortFields = [

  { value: 'WORKPLACE', label: 'Workplace' },
  { value: 'MANAGER', label: 'Manager' },
  { value: 'DATE_OF_BIRTH', label: 'Date of Birth' },
  { value: 'DATE', label: 'Created At' },

];

export const UserFilters = ({ filters, onChange }) => {
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
            placeholder="Search..."
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
            <option value="">All Workplaces</option>
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
            <option value="">All Managers</option>
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
            <option value="">Sort By</option>
            {sortFields.map(f => (
              <option key={f.value} value={f.value}>
                {f.label}
              </option>
            ))}
          </Form.Select>
        </Col>
         <Col md={2} className="mb-2">
          <Form.Check
            type="switch"
            id="ascendingSwitch"
            name="ascending"
            label={filters.ascending ? 'Ascending' : 'Descending'}
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
        <Col md={1} className="mb-2">
          <Form.Check
            type="switch"
            id="noManagerSwitch"
            name="noManager"
            label="NoManager"
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
       
      </Row>
    </Form>
  );
};

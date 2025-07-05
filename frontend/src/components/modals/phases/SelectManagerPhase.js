import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import { Button, Form } from 'react-bootstrap';

export const SelectManagerPhase = ({
  managers,
  selectedUsers,
  onNext,
  onBack,
}) => {
  const { t } = useTranslation();
  const [selectedManagerId, setSelectedManagerId] = useState(null);

  const handleManagerSelect = managerId => {
    setSelectedManagerId(managerId);
  };

  const handleNext = () => {
    const selectedManager = managers.find(
      manager => manager.id === selectedManagerId
    );
    onNext(selectedManager);
  };

  const hasSelection = selectedManagerId !== null;

  // Filter out selected users from managers list
  const availableManagers = managers.filter(
    manager => !selectedUsers.some(user => user.id === manager.id)
  );

  return (
    <div>
      <h5 className="mb-3">{t('managerAssignment.selectManager.title')}</h5>
      <p className="text-muted mb-3">
        {t(
          `managerAssignment.selectManager.description.${
            selectedUsers.length === 1 ? 'single' : 'multiple'
          }`,
          { count: selectedUsers.length }
        )}
      </p>

      <div className="mb-3 p-3 bg-light rounded">
        <h6 className="mb-2">
          {t('managerAssignment.selectManager.selectedUsers')}:
        </h6>
        <div className="d-flex flex-wrap gap-1">
          {selectedUsers.map(user => (
            <span key={user.id} className="badge bg-primary">
              {user.firstName} {user.lastName}
            </span>
          ))}
        </div>
      </div>

      <div
        className="border rounded p-3"
        style={{ maxHeight: '300px', overflowY: 'auto' }}
      >
        {availableManagers.length === 0 ? (
          <div className="text-center text-muted py-4">
            {t('managerAssignment.selectManager.noManagers')}
          </div>
        ) : (
          availableManagers.map(manager => (
            <Form.Check
              key={manager.id}
              type="radio"
              name="manager-selection"
              id={`manager-${manager.id}`}
              label={
                <div>
                  <strong>
                    {manager.firstName} {manager.lastName}
                  </strong>
                  <br />
                  <small className="text-muted">{manager.email}</small>
                  {manager.workplace && (
                    <small className="text-muted">
                      {' '}
                      • {manager.workplace.name}
                    </small>
                  )}
                  {manager.role && (
                    <small className="text-muted"> • {manager.role}</small>
                  )}
                </div>
              }
              checked={selectedManagerId === manager.id}
              onChange={() => handleManagerSelect(manager.id)}
              className="mb-2 p-2 border-bottom"
            />
          ))
        )}
      </div>

      <div className="d-flex justify-content-between mt-4">
        <Button variant="outline-secondary" onClick={onBack}>
          {t('managerAssignment.selectManager.buttons.back')}
        </Button>
        <Button variant="primary" onClick={handleNext} disabled={!hasSelection}>
          {t('managerAssignment.selectManager.buttons.next')}
        </Button>
      </div>
    </div>
  );
};

SelectManagerPhase.propTypes = {
  managers: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      firstName: PropTypes.string.isRequired,
      lastName: PropTypes.string.isRequired,
      email: PropTypes.string.isRequired,
      role: PropTypes.string,
      workplace: PropTypes.shape({
        name: PropTypes.string,
      }),
    })
  ).isRequired,
  selectedUsers: PropTypes.array.isRequired,
  onNext: PropTypes.func.isRequired,
  onBack: PropTypes.func.isRequired,
};

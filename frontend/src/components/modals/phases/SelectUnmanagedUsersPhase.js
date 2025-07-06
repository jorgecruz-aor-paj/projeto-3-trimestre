import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { Button, Form } from 'react-bootstrap';

export const SelectUnmanagedUsersPhase = ({
  unmanagedUsers,
  onNext,
  onBack,
}) => {
  const { t } = useTranslation();
  const [selectedUserIds, setSelectedUserIds] = useState(new Set());

  const handleUserToggle = userId => {
    const newSelected = new Set(selectedUserIds);
    if (newSelected.has(userId)) {
      newSelected.delete(userId);
    } else {
      newSelected.add(userId);
    }
    setSelectedUserIds(newSelected);
  };

  const handleSelectAll = () => {
    if (selectedUserIds.size === users.length) {
      setSelectedUserIds(new Set());
    } else {
      setSelectedUserIds(new Set(unmanagedUsers.map(user => user.id)));
    }
  };

  const handleNext = () => {
    const selectedUsers = unmanagedUsers.filter(user =>
      selectedUserIds.has(user.id)
    );
    onNext(selectedUsers);
  };

  const isAllSelected = selectedUserIds.size === unmanagedUsers.length;
  const hasSelection = selectedUserIds.size > 0;

  return (
    <div>
      <h5 className="mb-3">{t('managerAssignment.selectUsers.title')}</h5>
      <p className="text-muted mb-3">
        {t('managerAssignment.selectUsers.description')}
      </p>

      <div className="d-flex justify-content-between align-items-center mb-3">
        <span className="text-muted">
          {t('managerAssignment.selectUsers.selectionCount', {
            selected: selectedUserIds.size,
            total: unmanagedUsers.length,
          })}
        </span>
        <Button variant="outline-secondary" size="sm" onClick={handleSelectAll}>
          {isAllSelected
            ? t('managerAssignment.selectUsers.deselectAll')
            : t('managerAssignment.selectUsers.selectAll')}
        </Button>
      </div>

      <div
        className="border rounded p-3"
        style={{ maxHeight: '300px', overflowY: 'auto' }}
      >
        {unmanagedUsers.length === 0 ? (
          <div className="text-center text-muted py-4">
            {t('managerAssignment.selectUsers.noUsers')}
          </div>
        ) : (
          unmanagedUsers.map(user => (
            <Form.Check
              key={user.id}
              type="checkbox"
              id={`user-${user.id}`}
              label={
                <div>
                  <strong>
                    {user.firstName} {user.lastName}
                  </strong>
                  <br />
                  <small className="text-muted">{user.email}</small>
                  {user.workplace && (
                    <small className="text-muted">
                      {' '}
                      • {user.workplace.name}
                    </small>
                  )}
                </div>
              }
              checked={selectedUserIds.has(user.id)}
              onChange={() => handleUserToggle(user.id)}
              className="mb-2 p-2 border-bottom"
            />
          ))
        )}
      </div>

      <div className="d-flex justify-content-end gap-2 mt-4">
        <Button variant="secondary" onClick={onBack}>
          {t('managerAssignment.selectUsers.buttons.cancel')}
        </Button>
        <Button variant="primary" onClick={handleNext} disabled={!hasSelection}>
          {t('managerAssignment.selectUsers.buttons.next')}
        </Button>
      </div>
    </div>
  );
};

SelectUnmanagedUsersPhase.propTypes = {
  unmanagedUsers: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      firstName: PropTypes.string.isRequired,
      lastName: PropTypes.string.isRequired,
      email: PropTypes.string.isRequired,
      workplace: PropTypes.shape({
        name: PropTypes.string,
      }),
    })
  ).isRequired,
  onNext: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};

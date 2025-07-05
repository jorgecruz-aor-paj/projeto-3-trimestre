import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Button, Alert, Spinner } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

export const ConfirmAssignmentPhase = ({
  selectedUsers,
  selectedManager,
  onNext,
  onBack,
}) => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const { t } = useTranslation();

  const handleConfirm = async () => {
    setIsSubmitting(true);
    setError(null);

    try {
      // Create assignment object with manager ID and array of user IDs
      const assignment = {
        managerId: selectedManager.id,
        userIds: selectedUsers.map(user => user.id),
      };

      await onNext(assignment);
    } catch (err) {
      setError(err.message || t('managerAssignment.confirm.error'));
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <h5 className="mb-3">{t('managerAssignment.confirm.title')}</h5>
      <p className="text-muted mb-3">
        {t('managerAssignment.confirm.description')}
      </p>

      {error && (
        <Alert variant="danger" className="mb-3">
          {error}
        </Alert>
      )}

      <div className="row">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h6 className="mb-0">
                {t('managerAssignment.confirm.selectedManager')}
              </h6>
            </div>
            <div className="card-body">
              <div className="d-flex align-items-center">
                <div>
                  <strong>
                    {selectedManager.firstName} {selectedManager.lastName}
                  </strong>
                  <br />
                  <small className="text-muted">{selectedManager.email}</small>
                  {selectedManager.workplace && (
                    <>
                      <br />
                      <small className="text-muted">
                        {selectedManager.workplace.name}
                      </small>
                    </>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h6 className="mb-0">
                {t('managerAssignment.confirm.usersToAssign')} (
                {selectedUsers.length})
              </h6>
            </div>
            <div
              className="card-body"
              style={{ maxHeight: '200px', overflowY: 'auto' }}
            >
              {selectedUsers.map(user => (
                <div key={user.id} className="mb-2">
                  <strong>
                    {user.firstName} {user.lastName}
                  </strong>
                  <br />
                  <small className="text-muted">{user.email}</small>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <Alert variant="info" className="mt-3">
        {t(
          `managerAssignment.confirm.summary.${
            selectedUsers.length === 1 ? 'single' : 'multiple'
          }`,
          {
            managerName: `${selectedManager.firstName} ${selectedManager.lastName}`,
            count: selectedUsers.length,
          }
        )}
      </Alert>

      <div className="d-flex justify-content-between mt-4">
        <Button
          variant="outline-secondary"
          onClick={onBack}
          disabled={isSubmitting}
        >
          {t('managerAssignment.confirm.buttons.back')}
        </Button>
        <Button
          variant="success"
          onClick={handleConfirm}
          disabled={isSubmitting}
        >
          {isSubmitting ? (
            <>
              <Spinner size="sm" className="me-2" />
              {t('managerAssignment.confirm.buttons.confirming')}
            </>
          ) : (
            t('managerAssignment.confirm.buttons.confirm')
          )}
        </Button>
      </div>
    </div>
  );
};

ConfirmAssignmentPhase.propTypes = {
  selectedUsers: PropTypes.array.isRequired,
  selectedManager: PropTypes.shape({
    id: PropTypes.number.isRequired,
    firstName: PropTypes.string.isRequired,
    lastName: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
    workplace: PropTypes.shape({
      name: PropTypes.string,
    }),
  }).isRequired,
  onNext: PropTypes.func.isRequired,
  onBack: PropTypes.func.isRequired,
};

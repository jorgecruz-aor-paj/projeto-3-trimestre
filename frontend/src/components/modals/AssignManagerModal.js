import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Modal } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { ModalProgressBar } from './ModalProgressBar';
import { SelectUnmanagedUsersPhase } from './phases/SelectUnmanagedUsersPhase';
import { SelectManagerPhase } from './phases/SelectManagerPhase';
import { ConfirmAssignmentPhase } from './phases/ConfirmAssignmentPhase';

const PHASES = {
  SELECT_USERS: 1,
  SELECT_MANAGER: 2,
  CONFIRM: 3,
};

const getPhaseLabels = t => [
  t('managerAssignment.phases.selectUsers'),
  t('managerAssignment.phases.chooseManager'),
  t('managerAssignment.phases.confirm'),
];

export const AssignManagerModal = ({
  show,
  onBack: onHide,
  onComplete: onAssignmentComplete,
  unmanagedUsers = [],
  managers = [],
}) => {
  const [currentPhase, setCurrentPhase] = useState(PHASES.SELECT_USERS);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [selectedManager, setSelectedManager] = useState(null);

  const handleClose = () => {
    // Reset state when modal closes
    setCurrentPhase(PHASES.SELECT_USERS);
    setSelectedUsers([]);
    setSelectedManager(null);
    onHide();
  };

  const handleUsersSelected = users => {
    setSelectedUsers(users);
    setCurrentPhase(PHASES.SELECT_MANAGER);
  };

  const handleManagerSelected = manager => {
    setSelectedManager(manager);
    setCurrentPhase(PHASES.CONFIRM);
  };

  const handleConfirmAssignment = async assignments => {
    await onAssignmentComplete(assignments);
    handleClose();
  };

  const handleBack = () => {
    if (currentPhase === PHASES.SELECT_MANAGER) {
      setCurrentPhase(PHASES.SELECT_USERS);
    } else if (currentPhase === PHASES.CONFIRM) {
      setCurrentPhase(PHASES.SELECT_MANAGER);
    }
  };

  const { t } = useTranslation();
  const phaseLabels = getPhaseLabels(t);

  const renderCurrentPhase = () => {
    switch (currentPhase) {
      case PHASES.SELECT_USERS:
        return (
          <SelectUnmanagedUsersPhase
            unmanagedUsers={unmanagedUsers}
            onNext={handleUsersSelected}
            onBack={handleClose}
          />
        );
      case PHASES.SELECT_MANAGER:
        return (
          <SelectManagerPhase
            managers={managers}
            selectedUsers={selectedUsers}
            onNext={handleManagerSelected}
            onBack={handleBack}
          />
        );
      case PHASES.CONFIRM:
        return (
          <ConfirmAssignmentPhase
            selectedUsers={selectedUsers}
            selectedManager={selectedManager}
            onNext={handleConfirmAssignment}
            onBack={handleBack}
          />
        );
      default:
        return null;
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg" centered>
      <Modal.Header closeButton>
        <Modal.Title>{t('managerAssignment.modalTitle')}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ModalProgressBar
          phases={phaseLabels}
          currentPhase={currentPhase - 1}
        />
        <div className="mt-4">{renderCurrentPhase()}</div>
      </Modal.Body>
    </Modal>
  );
};

AssignManagerModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onBack: PropTypes.func.isRequired,
  onComplete: PropTypes.func.isRequired,
  unmanagedUsers: PropTypes.array,
  managers: PropTypes.array,
};

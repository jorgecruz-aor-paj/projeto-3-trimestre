import React from 'react';
import PropTypes from 'prop-types';
import { Button } from 'react-bootstrap';

export const StartCycleButton = ({ disabled, unmanagedCount, onClick }) => (
  <div>
    <Button
      variant="primary"
      disabled={disabled}
      title={
        disabled && unmanagedCount > 0
          ? 'Cannot start a new cycle while there are unmanaged users.'
          : undefined
      }
      onClick={onClick}
    >
      Start New Cycle
    </Button>
    {unmanagedCount > 0 && (
      <div className="alert alert-warning mt-2 mb-0 py-1 px-2 small">
        There are {unmanagedCount} unmanaged users.
      </div>
    )}
  </div>
);

StartCycleButton.propTypes = {
  disabled: PropTypes.bool,
  unmanagedCount: PropTypes.number,
  onClick: PropTypes.func,
};

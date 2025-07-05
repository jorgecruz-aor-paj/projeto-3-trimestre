import React from 'react';
import PropTypes from 'prop-types';

export const ModalProgressBar = ({ phases, currentPhase }) => {
  return (
    <div className="d-flex align-items-center mb-3">
      {phases.map((phase, index) => (
        <React.Fragment key={index}>
          <div className="d-flex flex-column align-items-center">
            <div
              className={`rounded-circle d-flex align-items-center justify-content-center ${
                index <= currentPhase
                  ? 'bg-primary text-white'
                  : 'bg-light text-muted border'
              }`}
              style={{ width: '32px', height: '32px', fontSize: '14px' }}
            >
              {index + 1}
            </div>
            <small
              className={`mt-1 ${
                index <= currentPhase ? 'text-primary' : 'text-muted'
              }`}
            >
              {phase}
            </small>
          </div>
          {index < phases.length - 1 && (
            <div
              className={`flex-grow-1 mx-2 ${
                index < currentPhase ? 'bg-primary' : 'bg-light'
              }`}
              style={{ height: '2px', minWidth: '50px' }}
            />
          )}
        </React.Fragment>
      ))}
    </div>
  );
};

ModalProgressBar.propTypes = {
  phases: PropTypes.arrayOf(PropTypes.string).isRequired,
  currentPhase: PropTypes.number.isRequired,
};

import React from 'react';
import PropTypes from 'prop-types';
import { Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

export const StartCycleButton = ({
  disabled,
  unmanagedCount,
  onClick,
  onAssignManagers,
}) => {
  const { t } = useTranslation();

  return (
    <div>
      <div className="d-flex gap-2">
        <Button
          variant="primary"
          disabled={disabled}
          title={
            disabled && unmanagedCount > 0
              ? t('evaluationCycles.startCycle.disabled')
              : undefined
          }
          onClick={onClick}
        >
          {t('evaluationCycles.startCycle.button')}
        </Button>
        {unmanagedCount > 0 && onAssignManagers && (
          <Button
            variant="outline-warning"
            onClick={onAssignManagers}
            title={t('evaluationCycles.startCycle.assignManagers')}
          >
            {t('evaluationCycles.startCycle.assignManagers')}
          </Button>
        )}
      </div>
      {unmanagedCount > 0 && (
        <div className="alert alert-warning mt-2 mb-0 py-1 px-2 small">
          {t(
            `evaluationCycles.startCycle.unmanagedWarning.${
              unmanagedCount === 1 ? 'single' : 'multiple'
            }`,
            { count: unmanagedCount }
          )}
          {onAssignManagers &&
            ' ' + t('evaluationCycles.startCycle.unmanagedWarning.action')}
        </div>
      )}
    </div>
  );
};

StartCycleButton.propTypes = {
  disabled: PropTypes.bool,
  unmanagedCount: PropTypes.number,
  onClick: PropTypes.func,
  onAssignManagers: PropTypes.func,
};

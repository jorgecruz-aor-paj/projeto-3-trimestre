import React from 'react';
import PropTypes from 'prop-types';
import { EditableEvaluationCard } from './EditableEvaluationCard';
import { EvaluationCard } from './EvaluationCard';
import { useTranslation } from 'react-i18next';

/**
 * EvaluationList fetches and displays a list of evaluations based on filter props.
 * It supports loading, error, and empty states, and renders either EvaluationCard or EditableEvaluationCard.
 */
export const EvaluationList = ({
  evaluations = [],
  loading = false,
  error = null,
  editable,
  onEdit,
  renderCheckbox = false,
  selectedEvaluations = [],
  onSelectEvaluation,
  ...props
}) => {
  const { t } = useTranslation();

  if (loading)
    return (
      <div
        className="profile-page-spinner-container"
        style={{
          minHeight: 200,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <div
          className="spinner-border text-primary"
          role="status"
          aria-label="Loading"
        >
          <span className="visually-hidden">{t('common.loading')}</span>
        </div>
      </div>
    );

  if (error) {
    if (typeof error === 'string') {
      return <div className="alert alert-danger">{error}</div>;
    }
    return null;
  }
  if (!evaluations.length) return <div>{t('evaluations.noResults')}</div>;

  return (
    <div className="evaluation-list" {...props}>
      {evaluations.map(evaluation => {
        if (evaluation.status === 'CLOSED') {
          return <EvaluationCard key={evaluation.id} evaluation={evaluation} />;
        }
        // Editable evaluation: render checkbox if admin
        return (
          <div key={evaluation.id} className="d-flex align-items-start mb-2">
            {renderCheckbox && (
              <input
                type="checkbox"
                className="form-check-input mt-2 me-2"
                checked={selectedEvaluations.includes(evaluation.id)}
                onChange={e =>
                  onSelectEvaluation(evaluation.id, e.target.checked)
                }
                aria-label={t('common.select')}
              />
            )}
            <EditableEvaluationCard evaluation={evaluation} onEdit={onEdit} />
          </div>
        );
      })}
    </div>
  );
};

EvaluationList.propTypes = {
  evaluations: PropTypes.array,
  loading: PropTypes.bool,
  error: PropTypes.any,
  editable: PropTypes.bool,
  onEdit: PropTypes.func,
  renderCheckbox: PropTypes.bool,
  selectedEvaluations: PropTypes.array,
  onSelectEvaluation: PropTypes.func,
};

EvaluationList.defaultProps = {
  editable: false,
  onEdit: undefined,
};

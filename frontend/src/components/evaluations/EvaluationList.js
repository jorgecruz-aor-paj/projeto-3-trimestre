import React from 'react';
import PropTypes from 'prop-types';
import { Row, Col, Spinner } from 'react-bootstrap';
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
  onEdit,
  renderCheckbox = false,
  selectedEvaluations = [],
  onSelectEvaluation,
  userRole,
  ...props
}) => {
  const { t } = useTranslation();

  /**
   * Determines if an evaluation should be editable based on user role and evaluation status
   * @param {Object} evaluation - The evaluation object
   * @param {string} userRole - The current user's role ('ADMIN' or 'MANAGER')
   * @returns {boolean} Whether the evaluation should be editable
   */
  const isEvaluationEditable = (evaluation, userRole) => {
    // If no user role is provided, fall back to original logic
    if (!userRole) {
      return (
        evaluation.status !== 'CLOSED' && evaluation.status !== 'COMPLETED'
      );
    }

    // Admin can edit evaluations if status is not CLOSED
    if (userRole === 'ADMIN') {
      return evaluation.status !== 'CLOSED';
    }

    // Manager can edit evaluations only if status is ONGOING
    if (userRole === 'MANAGER') {
      return evaluation.status === 'ONGOING';
    }

    // Default to non-editable for other roles
    return false;
  };

  if (loading)
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('common.loading')}</p>
      </div>
    );

  if (error) {
    if (typeof error === 'string') {
      return <div className="alert alert-danger">{error}</div>;
    }
    return null;
  }

  if (!evaluations.length) {
    return (
      <div className="text-center py-5">
        <i className="bi bi-clipboard-check display-4 text-muted"></i>
        <h5 className="mt-3 text-muted">{t('evaluations.noResults')}</h5>
        <p className="text-muted">
          {t(
            'evaluations.admin.noEvaluationsDesc',
            'No evaluations match your current filters'
          )}
        </p>
      </div>
    );
  }

  return (
    <Row className="evaluation-list" {...props}>
      {evaluations.map(evaluation => (
        <Col key={evaluation.id} md={6} lg={4} className="mb-4">
          <div className="position-relative h-100">
            {renderCheckbox && (
              <div
                className="position-absolute evaluation-checkbox-container"
                style={{ top: '-8px', left: '-8px', zIndex: 3 }}
              >
                <input
                  type="checkbox"
                  className="form-check-input evaluation-selection-checkbox"
                  checked={selectedEvaluations.includes(evaluation.id)}
                  onChange={e =>
                    onSelectEvaluation(evaluation.id, e.target.checked)
                  }
                  aria-label={t('common.select')}
                  style={{
                    width: '24px',
                    height: '24px',
                    backgroundColor: selectedEvaluations.includes(evaluation.id)
                      ? '#0d6efd'
                      : 'white',
                    border: selectedEvaluations.includes(evaluation.id)
                      ? '2px solid #0d6efd'
                      : '2px solid #dee2e6',
                    cursor: 'pointer',
                    boxShadow: selectedEvaluations.includes(evaluation.id)
                      ? '0 0 0 2px rgba(13, 110, 253, 0.25)'
                      : 'none',
                  }}
                />
              </div>
            )}
            <div
              className={
                selectedEvaluations.includes(evaluation.id)
                  ? 'selected-evaluation'
                  : ''
              }
            >
              {isEvaluationEditable(evaluation, userRole) ? (
                <EditableEvaluationCard
                  evaluation={evaluation}
                  onEdit={onEdit}
                />
              ) : (
                <EvaluationCard evaluation={evaluation} />
              )}
            </div>
          </div>
        </Col>
      ))}
    </Row>
  );
};

EvaluationList.propTypes = {
  evaluations: PropTypes.array,
  loading: PropTypes.bool,
  error: PropTypes.any,
  onEdit: PropTypes.func,
  renderCheckbox: PropTypes.bool,
  selectedEvaluations: PropTypes.array,
  onSelectEvaluation: PropTypes.func,
  userRole: PropTypes.string,
};

EvaluationList.defaultProps = {
  onEdit: undefined,
};

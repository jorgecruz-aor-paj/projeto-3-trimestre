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
  ...props
}) => {
  const { t } = useTranslation();

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
                className="position-absolute"
                style={{ top: '-8px', left: '-8px', zIndex: 3 }}
              >
                <input
                  type="checkbox"
                  className="form-check-input"
                  checked={selectedEvaluations.includes(evaluation.id)}
                  onChange={e =>
                    onSelectEvaluation(evaluation.id, e.target.checked)
                  }
                  aria-label={t('common.select')}
                  style={{
                    width: '20px',
                    height: '20px',
                    backgroundColor: 'white',
                    border: '2px solid #dee2e6',
                  }}
                />
              </div>
            )}
            {evaluation.status === 'CLOSED' ? (
              <EvaluationCard evaluation={evaluation} />
            ) : (
              <EditableEvaluationCard evaluation={evaluation} onEdit={onEdit} />
            )}
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
};

EvaluationList.defaultProps = {
  onEdit: undefined,
};

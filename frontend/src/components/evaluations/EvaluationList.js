import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { EditableEvaluationCard } from './EditableEvaluationCard';
import { EvaluationCard } from './EvaluationCard';
import { useTranslation } from 'react-i18next';
import { useEvaluationStore } from 'stores/EvaluationStore';
import { useAlert } from '../../contexts/AlertContext';

/**
 * EvaluationList fetches and displays a list of evaluations based on filter props.
 * It supports loading, error, and empty states, and renders either EvaluationCard or EditableEvaluationCard.
 */
export const EvaluationList = ({ filters, editable, onEdit, ...props }) => {
  const { t } = useTranslation();
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();
  const { showAlert } = useAlert();

  useEffect(() => {
    fetchEvaluations(filters);
  }, [JSON.stringify(filters), fetchEvaluations]);

  useEffect(() => {
    if (error && typeof error === 'object' && error !== null) {
      showAlert({
        title: t('common.error', 'Error'),
        message: error.message || JSON.stringify(error),
        variant: 'danger',
      });
    }
  }, [error, showAlert, t]);

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
    // If error is a string, show inline, otherwise rely on AlertContext
    if (typeof error === 'string') {
      return <div className="alert alert-danger">{error}</div>;
    }
    // If error is object, don't render anything (AlertContext will show modal)
    return null;
  }
  if (!evaluations.length) return <div>{t('evaluations.noResults')}</div>;

  return (
    <div className="evaluation-list" {...props}>
      {evaluations.map(evaluation =>
        evaluation.status === 'CLOSED' ? (
          <EvaluationCard key={evaluation.id} evaluation={evaluation} />
        ) : (
          <EditableEvaluationCard
            key={evaluation.id}
            evaluation={evaluation}
            onEdit={onEdit}
          />
        )
      )}
    </div>
  );
};

EvaluationList.propTypes = {
  filters: PropTypes.object.isRequired,
  editable: PropTypes.bool,
  onEdit: PropTypes.func,
  user: PropTypes.object,
};

EvaluationList.defaultProps = {
  editable: false,
  onEdit: undefined,
};

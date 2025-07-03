import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { EvaluationCard } from './EvaluationCard';
import { EditableEvaluationCard } from './EditableEvaluationCard';
import { useTranslation } from 'react-i18next';
import { useEvaluationStore } from 'stores/EvaluationStore';

/**
 * EvaluationList fetches and displays a list of evaluations based on filter props.
 * It supports loading, error, and empty states, and renders either EvaluationCard or EditableEvaluationCard.
 */
export const EvaluationList = ({ filters, editable, onEdit, ...props }) => {
  const { t } = useTranslation();
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();

  useEffect(() => {
    fetchEvaluations(filters);
  }, [JSON.stringify(filters), fetchEvaluations]);

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

  if (error) return <div className="alert alert-danger">{error}</div>;
  if (!evaluations.length) return <div>{t('evaluations.noResults')}</div>;

  return (
    <div className="evaluation-list" {...props}>
      {evaluations.map(evaluation =>
        editable ? (
          <EditableEvaluationCard
            key={evaluation.id}
            evaluation={evaluation}
            onEdit={onEdit}
          />
        ) : (
          <EvaluationCard key={evaluation.id} evaluation={evaluation} />
        )
      )}
    </div>
  );
};

EvaluationList.propTypes = {
  filters: PropTypes.object.isRequired,
  editable: PropTypes.bool,
  onEdit: PropTypes.func,
};

EvaluationList.defaultProps = {
  editable: false,
  onEdit: undefined,
};

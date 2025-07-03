import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

/**
 * EditableEvaluationCard allows editing of an evaluation's fields.
 * Calls onEdit with updated data when saved.
 */
export const EditableEvaluationCard = ({ evaluation, onEdit }) => {
  const { t } = useTranslation();
  const [form, setForm] = useState({ ...evaluation });
  const [editing, setEditing] = useState(false);

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSave = () => {
    setEditing(false);
    if (onEdit) onEdit(form);
  };

  return (
    <div className="card evaluation-card mb-3">
      <div className="card-body">
        <h5 className="card-title">
          {t('evaluations.evaluation')} #{form.id}
        </h5>
        {editing ? (
          <>
            <div className="mb-2">
              <label>{t('evaluations.comments')}</label>
              <input
                type="text"
                name="comments"
                value={form.comments || ''}
                onChange={handleChange}
                className="form-control"
              />
            </div>
            {/* Add more editable fields as needed */}
            <button className="btn btn-primary me-2" onClick={handleSave}>
              {t('common.save')}
            </button>
            <button
              className="btn btn-secondary"
              onClick={() => setEditing(false)}
            >
              {t('common.cancel')}
            </button>
          </>
        ) : (
          <>
            <p className="card-text">
              <strong>{t('evaluations.evaluator')}:</strong>{' '}
              {form.evaluator?.name || '-'}
              <br />
              <strong>{t('evaluations.scale')}:</strong> {form.scale || '-'}
              <br />
              <strong>{t('evaluationStatus')}:</strong>{' '}
              {t(`evaluationStatus.${form.status}`) || form.status}
              <br />
              <strong>{t('evaluations.date')}:</strong>{' '}
              {form.date ? new Date(form.date).toLocaleDateString() : '-'}
              <br />
              <strong>{t('evaluations.comments')}:</strong>{' '}
              {form.comments || '-'}
            </p>
            <button
              className="btn btn-outline-primary"
              onClick={() => setEditing(true)}
            >
              {t('common.edit')}
            </button>
          </>
        )}
      </div>
    </div>
  );
};

EditableEvaluationCard.propTypes = {
  evaluation: PropTypes.object.isRequired,
  onEdit: PropTypes.func,
};

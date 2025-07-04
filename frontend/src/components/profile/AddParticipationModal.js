import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { FormInput } from '../forms/FormInput';
import { FormDropdownInput } from '../forms/FormDropdownInput';
import PropTypes from 'prop-types';

/**
 * Modal for adding a participation to a training
 * @param {Object} props
 * @param {boolean} props.show
 * @param {function} props.onHide
 * @param {function} props.onSubmit
 * @param {Array} props.trainings - Array of { value, label }
 * @param {number} props.participantId
 * @param {number} props.managerId
 * @param {string} [props.t] - translation function
 */
export const AddParticipationModal = ({
  show,
  onHide,
  onSubmit,
  trainings,
  trainingsLoading = false,
  trainingsError = null,
  participantId,
  managerId,
  t = v => v,
}) => {
  const [form, setForm] = useState({
    trainingId: '',
    participatedAt: '',
  });
  const [formError, setFormError] = useState({});
  const [submitting, setSubmitting] = useState(false);

  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(f => ({ ...f, [name]: value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setFormError({});
    const errors = {};
    if (!form.trainingId) errors.trainingId = 'Required';
    if (!form.participatedAt) errors.participatedAt = 'Required';
    if (Object.keys(errors).length) {
      setFormError(errors);
      return;
    }
    setSubmitting(true);
    const participationObj = {
      participantId,
      managerId,
      trainingId: Number(form.trainingId),
      participatedAt: new Date(form.participatedAt).toISOString(),
    };
    await onSubmit(participationObj);
    setSubmitting(false);
    setForm({ trainingId: '', participatedAt: '' });
    onHide();
  };

  const handleClose = () => {
    setFormError({});
    setForm({ trainingId: '', participatedAt: '' });
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>
          {t('profile.addParticipation') || 'Add Participation'}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {trainingsLoading ? (
            <div>{t('common.loading') || 'Loading...'}</div>
          ) : trainingsError ? (
            <div className="text-danger">{trainingsError}</div>
          ) : (
            <>
              <FormDropdownInput
                name="trainingId"
                label={t('profile.training') || 'Training'}
                value={form.trainingId}
                onChange={handleFormChange}
                options={trainings}
                error={formError.trainingId}
                placeholder={t('profile.selectTraining') || 'Select training'}
              />
              <FormInput
                name="participatedAt"
                label={t('profile.participatedAt') || 'Participation Date'}
                type="datetime-local"
                value={form.participatedAt}
                onChange={handleFormChange}
                error={formError.participatedAt}
              />
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={handleClose}
            disabled={submitting}
          >
            {t('common.cancel') || 'Cancel'}
          </Button>
          <Button variant="primary" type="submit" disabled={submitting}>
            {t('common.save') || 'Save'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

AddParticipationModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  trainings: PropTypes.array.isRequired,
  trainingsLoading: PropTypes.bool,
  trainingsError: PropTypes.string,
  participantId: PropTypes.number.isRequired,
  managerId: PropTypes.number.isRequired,
  t: PropTypes.func,
};

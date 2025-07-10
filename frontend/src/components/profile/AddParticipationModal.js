import React, { useState } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { FormInput } from 'components/forms/FormInput';
import { FormDropdownInput } from 'components/forms/FormDropdownInput';
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
    participatedDate: new Date().toISOString().slice(0, 10), // Format: YYYY-MM-DD
    participatedTime: new Date().toTimeString().slice(0, 5), // Format: HH:MM
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
    if (!form.participatedDate) errors.participatedDate = 'Required';
    if (!form.participatedTime) errors.participatedTime = 'Required';
    if (Object.keys(errors).length) {
      setFormError(errors);
      return;
    }
    setSubmitting(true);
    
    // Combine date and time into ISO string
    const combinedDateTime = `${form.participatedDate}T${form.participatedTime}:00.000Z`;
    
    const participationObj = {
      participantId,
      managerId,
      trainingId: Number(form.trainingId),
      participatedAt: combinedDateTime,
    };
    await onSubmit(participationObj);
    setSubmitting(false);
    setForm({
      trainingId: '',
      participatedDate: new Date().toISOString().slice(0, 10),
      participatedTime: new Date().toTimeString().slice(0, 5),
    });
    onHide();
  };

  const handleClose = () => {
    setFormError({});
    setForm({
      trainingId: '',
      participatedDate: new Date().toISOString().slice(0, 10),
      participatedTime: new Date().toTimeString().slice(0, 5),
    });
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>{t('profile.addParticipation')}</Modal.Title>
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
                label={t('profile.training')}
                value={form.trainingId}
                onChange={handleFormChange}
                options={trainings}
                error={formError.trainingId}
                placeholder={t('profile.selectTraining')}
              />
              <Form.Group className="mb-3">
                <Form.Label htmlFor="participatedAt">
                  {t('profile.participatedAt')}
                </Form.Label>
                <Form.Control
                  id="participatedAt"
                  name="participatedAt"
                  type="datetime-local"
                  value={form.participatedAt}
                  onChange={handleFormChange}
                  isInvalid={!!formError.participatedAt}
                  style={{ minWidth: '200px' }}
                />
                <Form.Control.Feedback type="invalid">
                  {formError.participatedAt}
                </Form.Control.Feedback>
              </Form.Group>
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

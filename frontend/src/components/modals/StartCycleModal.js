import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

export const StartCycleModal = ({ show, onHide, onSubmit }) => {
  const { t } = useTranslation();
  const [endDate, setEndDate] = useState('');
  const [error, setError] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setIsSubmitting(true);

    try {
      // Set start date to current time
      const startDate = new Date();

      // Set end date to selected date at 23:59:59
      const endDateTime = new Date(endDate);
      endDateTime.setHours(23, 59, 59, 999);

      if (endDateTime <= startDate) {
        setError(t('evaluationCycles.startCycle.error.invalidEndDate'));
        return;
      }

      await onSubmit({
        startDate: startDate.toISOString(),
        endDate: endDateTime.toISOString(),
      });

      onHide();
    } catch (err) {
      setError(err.message || t('evaluationCycles.startCycle.error.generic'));
    } finally {
      setIsSubmitting(false);
    }
  };

  // Calculate minimum date (tomorrow)
  const minDate = new Date();
  minDate.setDate(minDate.getDate() + 1);
  const minDateStr = minDate.toISOString().split('T')[0];

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>{t('evaluationCycles.startCycle.modalTitle')}</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {error && (
            <Alert variant="danger" className="mb-3">
              {error}
            </Alert>
          )}

          <Form.Group className="mb-3">
            <Form.Label>
              {t('evaluationCycles.startCycle.startDate')}
            </Form.Label>
            <Form.Control
              type="text"
              value={new Date().toLocaleDateString()}
              disabled
            />
            <Form.Text className="text-muted">
              {t('evaluationCycles.startCycle.startDateHint')}
            </Form.Text>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>{t('evaluationCycles.startCycle.endDate')}</Form.Label>
            <Form.Control
              type="date"
              value={endDate}
              onChange={e => setEndDate(e.target.value)}
              min={minDateStr}
              required
            />
            <Form.Text className="text-muted">
              {t('evaluationCycles.startCycle.endDateHint')}
            </Form.Text>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide} disabled={isSubmitting}>
            {t('common.cancel')}
          </Button>
          <Button
            variant="primary"
            type="submit"
            disabled={!endDate || isSubmitting}
          >
            {isSubmitting ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" />
                {t('evaluationCycles.startCycle.starting')}
              </>
            ) : (
              t('evaluationCycles.startCycle.start')
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

StartCycleModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};

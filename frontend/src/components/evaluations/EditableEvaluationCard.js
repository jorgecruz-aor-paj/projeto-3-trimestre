import React, { useState, useEffect } from 'react';
import { ProfilePicture } from 'components/common/ProfilePicture';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { exportService } from 'services/exportService';
import { FormInput } from 'components/forms/FormInput';
import { FormDropdownInput } from 'components/forms/FormDropdownInput';
import { evaluationService } from 'services/evaluationService';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { translateEvaluationScale } from 'utils/evaluationScaleTranslations';
import { useFormValidation, evaluationValidationRules } from 'utils/validation';
import { useErrorMessage } from 'hooks/useErrorMessage';
import { useEvaluationStore } from 'stores/EvaluationStore';
import { useAuthStore } from 'stores/AuthStore';
import { Spinner } from 'react-bootstrap';

export const EditableEvaluationCard = ({
  evaluation,
  onEdit,
  loading = false,
}) => {
  const { t, i18n } = useTranslation();
  const { showAlert, showConfirm } = useAlert();
  const [editing, setEditing] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { getErrorMessage } = useErrorMessage();
  const evaluationStore = useEvaluationStore();
  const user = useAuthStore(state => state.user);

  // Check if user is admin or manager
  const isAdmin = user?.role === 'ADMIN';
  const isManager = user?.role === 'MANAGER';

  // Initialize form validation
  const {
    formData: form,
    errors,
    handleChange: handleValidationChange,
    validateAll,
    resetForm,
    setFormData: setForm,
  } = useFormValidation({ ...evaluation }, evaluationValidationRules);

  // Track which fields have been touched
  const [touched, setTouched] = useState({});

  // When the evaluation prop changes, update the form data
  useEffect(() => {
    // Find the current evaluation in the store to ensure we're using the latest data
    const currentEvaluation =
      evaluationStore.evaluations.find(
        storeEvaluation => storeEvaluation.id === evaluation.id
      ) || evaluation;

    // Only update if not currently editing to avoid overwriting user changes
    if (!editing) {
      setForm(currentEvaluation);
    }
  }, [evaluation, evaluationStore.evaluations, editing, setForm]);

  const scaleNumberMap = {
    LOW_CONTRIBUTION: 1,
    PARCIAL_CONTRIBUTION: 2,
    EXPECTED_CONTRIBUTION: 3,
    EXCEED_CONTRIBUTION: 4,
  };

  const handleChange = e => {
    const { name, value } = e.target;

    // Mark the field as touched when it changes
    setTouched(prev => ({
      ...prev,
      [name]: true,
    }));

    // Update the field value and validate it
    handleValidationChange(name, value);
  };

  const handleSave = async () => {
    // Mark all fields as touched for validation feedback
    const allFieldsTouched = Object.keys(form).reduce((acc, field) => {
      acc[field] = true;
      return acc;
    }, {});
    setTouched(allFieldsTouched);

    // Validate all fields before saving
    const isValid = validateAll();
    if (!isValid) {
      console.error('Validation failed with errors:', errors);
      showAlert({
        title: t('validation.error', 'Validation Error'),
        message: t(
          'validation.pleaseFixErrors',
          'Please fix the validation errors before saving'
        ),
        variant: 'danger',
      });
      return; // Don't save if validation fails
    }

    try {
      setIsSubmitting(true);
      // Send updated evaluation to backend
      const updatedEvaluation = await evaluationService.update(form);

      // Update the local form data with the response from the server
      setForm(updatedEvaluation);

      // Update the evaluation in the store
      if (evaluationStore.evaluations.length > 0) {
        const updatedEvaluations = evaluationStore.evaluations.map(evaluation =>
          evaluation.id === updatedEvaluation.id
            ? updatedEvaluation
            : evaluation
        );
        evaluationStore.setEvaluations(updatedEvaluations);
      }

      setEditing(false);

      // Pass the updated evaluation to the parent component
      if (onEdit) onEdit(updatedEvaluation);

      showAlert({
        title: t('common.success', 'Success'),
        message: t('evaluations.saveSuccess', 'Evaluation saved successfully.'),
        variant: 'success',
      });
    } catch (error) {
      setEditing(false);
      showAlert({
        title: t('common.error', 'Error'),
        message:
          error?.message ||
          t('evaluations.saveError', 'Failed to save evaluation.'),
        variant: 'danger',
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSubmitEvaluation = async () => {
    // Mark all fields as touched for validation feedback
    const allFieldsTouched = Object.keys(form).reduce((acc, field) => {
      acc[field] = true;
      return acc;
    }, {});
    setTouched(allFieldsTouched);

    // Validate all fields before saving
    const isValid = validateAll();
    if (!isValid) {
      console.error('Validation failed with errors:', errors);
      showAlert({
        title: t('validation.error', 'Validation Error'),
        message: t(
          'validation.pleaseFixErrors',
          'Please fix the validation errors before saving'
        ),
        variant: 'danger',
      });
      return;
    }

    // Show confirmation dialog before submitting
    showConfirm({
      title: t('evaluations.confirmSubmit', 'Confirm Submission'),
      message: t(
        'evaluations.confirmSubmitMessage',
        'Are you sure you want to submit this evaluation? Once submitted, it will be marked as completed and cannot be edited further.'
      ),
      confirmLabel: t('common.submit', 'Submit'),
      variant: 'primary',
      onConfirm: async () => {
        try {
          setIsSubmitting(true);
          // Send updated evaluation to backend with COMPLETED status
          const updatedEvaluation = await evaluationService.update({
            ...form,
            status: 'COMPLETED',
          });

          // Update the local form data with the response from the server
          setForm(updatedEvaluation);

          // Update the evaluation in the store
          if (evaluationStore.evaluations.length > 0) {
            const updatedEvaluations = evaluationStore.evaluations.map(
              evaluation =>
                evaluation.id === updatedEvaluation.id
                  ? updatedEvaluation
                  : evaluation
            );
            evaluationStore.setEvaluations(updatedEvaluations);
          }

          setEditing(false);

          // Pass the updated evaluation to the parent component
          if (onEdit) onEdit(updatedEvaluation);

          showAlert({
            title: t('common.success', 'Success'),
            message: t(
              'evaluations.submitSuccess',
              'Evaluation submitted successfully.'
            ),
            variant: 'success',
          });
        } catch (error) {
          setEditing(false);
          showAlert({
            title: t('common.error', 'Error'),
            message:
              error?.message ||
              t('evaluations.submitError', 'Failed to submit evaluation.'),
            variant: 'danger',
          });
        } finally {
          setIsSubmitting(false);
        }
      },
    });
  };

  const handleSaveAsDraft = async () => {
    // Mark all fields as touched for validation feedback
    const allFieldsTouched = Object.keys(form).reduce((acc, field) => {
      acc[field] = true;
      return acc;
    }, {});
    setTouched(allFieldsTouched);

    // Validate all fields before saving
    const isValid = validateAll();
    if (!isValid) {
      console.error('Validation failed with errors:', errors);
      showAlert({
        title: t('validation.error', 'Validation Error'),
        message: t(
          'validation.pleaseFixErrors',
          'Please fix the validation errors before saving'
        ),
        variant: 'danger',
      });
      return;
    }

    try {
      setIsSubmitting(true);
      // Send updated evaluation to backend with ONGOING status
      const updatedEvaluation = await evaluationService.update({
        ...form,
        status: 'ONGOING',
      });

      // Update the local form data with the response from the server
      setForm(updatedEvaluation);

      // Update the evaluation in the store
      if (evaluationStore.evaluations.length > 0) {
        const updatedEvaluations = evaluationStore.evaluations.map(evaluation =>
          evaluation.id === updatedEvaluation.id
            ? updatedEvaluation
            : evaluation
        );
        evaluationStore.setEvaluations(updatedEvaluations);
      }

      setEditing(false);

      // Pass the updated evaluation to the parent component
      if (onEdit) onEdit(updatedEvaluation);

      showAlert({
        title: t('common.success', 'Success'),
        message: t('evaluations.draftSaved', 'Draft saved successfully.'),
        variant: 'success',
      });
    } catch (error) {
      setEditing(false);
      showAlert({
        title: t('common.error', 'Error'),
        message:
          error?.message ||
          t('evaluations.draftSaveError', 'Failed to save draft.'),
        variant: 'danger',
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    // Find the current evaluation in the store to ensure we're using the latest data
    const currentEvaluation =
      evaluationStore.evaluations.find(
        storeEvaluation => storeEvaluation.id === evaluation.id
      ) || evaluation;

    // Reset form to the latest evaluation from the store
    setForm(currentEvaluation);
    resetForm(currentEvaluation);
    setTouched({});
    setEditing(false);
  };

  const handleDownload = () => {
    const fetchFile = async () => {
      try {
        const response = await exportService.evaluationToPDF(form.id);
        let filename = `evaluation_${form.id}.pdf`;
        if (
          response &&
          response.headers &&
          response.headers['content-disposition']
        ) {
          const match = response.headers['content-disposition'].match(
            /filename="?([^";]+)"?/
          );
          if (match && match[1]) filename = match[1];
        }
        const blob = response.data instanceof Blob ? response.data : response;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.setAttribute('download', filename);
        document.body.appendChild(a);
        a.click();
        setTimeout(() => {
          window.URL.revokeObjectURL(url);
          document.body.removeChild(a);
        }, 200);
      } catch (error) {
        console.error('Error downloading evaluation:', error);
        showAlert({
          title: t('common.error', 'Error'),
          message: t('profile.downloadError'),
          variant: 'danger',
        });
      }
    };
    fetchFile();
  };

  const scaleNumber = scaleNumberMap[form.scale] || '-';
  const scaleLabel = form.scale
    ? translateEvaluationScale(form.scale, i18n.language)
    : '-';

  return (
    <div className="card h-100 shadow-sm evaluation-card">
      <div className="card-body d-flex flex-column">
        {/* Header with profile and download */}
        <div className="d-flex justify-content-between align-items-start mb-3">
          <div className="d-flex align-items-center">
            <ProfilePicture
              userId={form.evaluatee?.id}
              className="rounded-circle me-3"
              alt={
                form.evaluatee
                  ? `${form.evaluatee.firstName} ${form.evaluatee.lastName}`
                  : 'Profile'
              }
              style={{
                width: 48,
                height: 48,
                objectFit: 'cover',
                border: '1px solid #dee2e6',
              }}
            />
            <div>
              <h6 className="card-title mb-1">
                {form.evaluatee
                  ? `${form.evaluatee.firstName} ${form.evaluatee.lastName}`
                  : '-'}
              </h6>
              <div className="small text-muted">
                <i className="bi bi-person-badge me-1"></i>
                {t('filters.evaluatee', 'Evaluatee')}
              </div>
            </div>
          </div>
          <button
            className="btn btn-outline-secondary btn-sm"
            title={t('profile.downloadEvaluation')}
            onClick={handleDownload}
            aria-label={t('profile.downloadEvaluation')}
          >
            <FontAwesomeIcon icon={faFilePdf} />
          </button>
        </div>

        {/* Status and Scale badges */}
        {!editing && (
          <div className="d-flex gap-2 mb-3">
            {/* Show status badge for all users */}
            <span
              className={`badge ${
                form.status === 'COMPLETED'
                  ? 'bg-success'
                  : form.status === 'ONGOING'
                  ? 'bg-warning'
                  : form.status === 'CLOSED'
                  ? 'bg-danger'
                  : 'bg-secondary'
              }`}
            >
              <i className="bi bi-info-circle me-1"></i>
              {t(`evaluationStatus.${form.status}`) || form.status}
            </span>
            {scaleNumber !== '-' && (
              <span className="badge bg-primary">
                <i className="bi bi-award me-1"></i>
                {scaleNumber} - {scaleLabel}
              </span>
            )}
          </div>
        )}

        {/* Evaluation details or edit form */}
        <div className="flex-grow-1">
          {editing ? (
            <div className="mt-2">
              <FormDropdownInput
                name="scale"
                label={t('evaluationScale.title')}
                value={form.scale || ''}
                onChange={handleChange}
                options={[
                  { value: '', label: t('evaluationScale.notEvaluated') },
                  {
                    value: 'LOW_CONTRIBUTION',
                    label: `1 - ${translateEvaluationScale(
                      'LOW_CONTRIBUTION',
                      i18n.language
                    )}`,
                  },
                  {
                    value: 'PARCIAL_CONTRIBUTION',
                    label: `2 - ${translateEvaluationScale(
                      'PARCIAL_CONTRIBUTION',
                      i18n.language
                    )}`,
                  },
                  {
                    value: 'EXPECTED_CONTRIBUTION',
                    label: `3 - ${translateEvaluationScale(
                      'EXPECTED_CONTRIBUTION',
                      i18n.language
                    )}`,
                  },
                  {
                    value: 'EXCEED_CONTRIBUTION',
                    label: `4 - ${translateEvaluationScale(
                      'EXCEED_CONTRIBUTION',
                      i18n.language
                    )}`,
                  },
                ]}
                error={
                  touched.scale && errors.scale
                    ? getErrorMessage(errors.scale)
                    : null
                }
              />
              {/* Only show status field for admins */}
              {isAdmin && (
                <FormDropdownInput
                  name="status"
                  label={t('profile.status')}
                  value={form.status || ''}
                  onChange={handleChange}
                  options={[
                    { value: 'ONGOING', label: t('evaluationStatus.ONGOING') },
                    {
                      value: 'COMPLETED',
                      label: t('evaluationStatus.COMPLETED'),
                    },
                    { value: 'CLOSED', label: t('evaluationStatus.CLOSED') },
                  ]}
                  error={
                    touched.status && errors.status
                      ? getErrorMessage(errors.status)
                      : null
                  }
                />
              )}
              <FormInput
                name="feedback"
                label={t('evaluations.feedback')}
                type="text"
                value={form.feedback || ''}
                onChange={handleChange}
                error={
                  touched.feedback && errors.feedback
                    ? getErrorMessage(errors.feedback)
                    : null
                }
              />
            </div>
          ) : (
            <>
              <div className="mb-2">
                <small className="text-muted d-block">
                  <i className="bi bi-calendar3 me-1"></i>
                  {t('evaluationCycle.cycleClosureDate')}
                </small>
                <div>
                  {form.evaluationCycle?.closureDate
                    ? formatDateToDDMMYYYY(form.evaluationCycle.closureDate)
                    : t('evaluationCycle.closureDateNotAvailable')}
                </div>
              </div>

              {form.evaluator && (
                <div className="mb-2">
                  <small className="text-muted d-block">
                    <i className="bi bi-person-check me-1"></i>
                    {t('filters.evaluator')}
                  </small>
                  <div>
                    {`${form.evaluator.firstName} ${form.evaluator.lastName}`}
                  </div>
                </div>
              )}
            </>
          )}
        </div>
      </div>

      <div className="card-footer bg-transparent d-flex justify-content-between align-items-center">
        <small className="text-muted">
          <i className="bi bi-hash me-1"></i>
          ID: {form.id}
        </small>
        {editing ? (
          <div>
            {isAdmin ? (
              // Admin buttons - single save button
              <>
                <button
                  className="btn btn-primary btn-sm me-2"
                  onClick={handleSave}
                  disabled={isSubmitting}
                >
                  {isSubmitting ? (
                    <>
                      <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                        className="me-1"
                      />
                      {t('common.saving', 'Saving...')}
                    </>
                  ) : (
                    t('common.save')
                  )}
                </button>
                <button
                  className="btn btn-secondary btn-sm"
                  onClick={handleCancel}
                  disabled={isSubmitting}
                >
                  {t('common.cancel')}
                </button>
              </>
            ) : (
              // Manager buttons - submit, save as draft, and cancel
              <>
                <button
                  className="btn btn-success btn-sm me-2"
                  onClick={handleSubmitEvaluation}
                  disabled={isSubmitting}
                >
                  {isSubmitting ? (
                    <>
                      <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                        className="me-1"
                      />
                      {t('evaluations.submitting', 'Submitting...')}
                    </>
                  ) : (
                    t('evaluations.submit', 'Submit')
                  )}
                </button>
                <button
                  className="btn btn-outline-primary btn-sm me-2"
                  onClick={handleSaveAsDraft}
                  disabled={isSubmitting}
                >
                  {isSubmitting ? (
                    <>
                      <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                        className="me-1"
                      />
                      {t('evaluations.savingDraft', 'Saving Draft...')}
                    </>
                  ) : (
                    t('evaluations.saveAsDraft', 'Save as Draft')
                  )}
                </button>
                <button
                  className="btn btn-secondary btn-sm"
                  onClick={handleCancel}
                  disabled={isSubmitting}
                >
                  {t('common.cancel')}
                </button>
              </>
            )}
          </div>
        ) : (
          <button
            className="btn btn-outline-primary btn-sm"
            onClick={() => setEditing(true)}
            disabled={isSubmitting}
          >
            <i className="bi bi-pencil me-1"></i>
            {t('common.edit')}
          </button>
        )}
      </div>
    </div>
  );
};

EditableEvaluationCard.propTypes = {
  evaluation: PropTypes.shape({
    id: PropTypes.number.isRequired,
    scale: PropTypes.string,
    status: PropTypes.string,
    evaluationCycle: PropTypes.shape({
      closureDate: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number,
        PropTypes.instanceOf(Date),
      ]),
    }),
    evaluatee: PropTypes.shape({
      id: PropTypes.number,
      firstName: PropTypes.string,
      lastName: PropTypes.string,
      photograph: PropTypes.string,
    }),
    evaluator: PropTypes.shape({
      id: PropTypes.number,
      firstName: PropTypes.string,
      lastName: PropTypes.string,
    }),
    comments: PropTypes.string,
    feedback: PropTypes.string,
  }).isRequired,
  onEdit: PropTypes.func,
  loading: PropTypes.bool,
};

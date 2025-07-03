import React, { useState } from 'react';
import { ProfilePicture } from '../common/ProfilePicture';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { exportService } from 'services/exportService';
import { FormInput } from '../forms/FormInput';
import { FormDropdownInput } from '../forms/FormDropdownInput';
import { evaluationService } from 'services/evaluationService';

const scaleNumberMap = {
  LOW_CONTRIBUTION: 1,
  PARCIAL_CONTRIBUTION: 2,
  EXPECTED_CONTRIBUTION: 3,
  EXCEED_CONTRIBUTION: 4,
};

export const EditableEvaluationCard = ({ evaluation, onEdit }) => {
  const { t, i18n } = useTranslation();
  const { showAlert } = useAlert();
  const [form, setForm] = useState({ ...evaluation });
  const [editing, setEditing] = useState(false);

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      // Send updated evaluation to backend
      await evaluationService.update(form);
      setEditing(false);
      if (onEdit) onEdit(form);
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
    }
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
    ? require('utils/evaluationScaleTranslations').translateEvaluationScale(
        form.scale,
        i18n.language
      )
    : '-';

  return (
    <div
      className="card evaluation-card mb-3 position-relative"
      style={{ paddingTop: 28 }}
    >
      {/* Download icon button in top right */}
      <button
        className="btn btn-link p-0 position-absolute"
        style={{ top: 4, right: 8, zIndex: 2 }}
        title={t('profile.downloadEvaluation')}
        onClick={handleDownload}
        aria-label={t('profile.downloadEvaluation')}
      >
        <FontAwesomeIcon icon={faFilePdf} size="lg" color="currentColor" />
      </button>
      {/* Profile picture and name */}
      <div className="d-flex align-items-center mb-2">
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
            border: '1px solid #ccc',
          }}
        />
        <div>
          <div className="fw-bold">
            {form.evaluatee
              ? `${form.evaluatee.firstName} ${form.evaluatee.lastName}`
              : '-'}
          </div>
          <div className="text-muted small">
            {t('profile.status')}:{' '}
            {t(`evaluationStatus.${form.status}`) || form.status}
          </div>
        </div>
      </div>
      {/* Closure date */}
      <div className="mb-1">
        <strong>{t('evaluationCycle.closureDate')}:</strong>{' '}
        {form.evaluationCycle?.closureDate
          ? formatDateToDDMMYYYY(form.evaluationCycle.closureDate)
          : t('evaluationCycle.closureDateNotAvailable')}
      </div>
      <div className="mb-1">
        <strong>{t('evaluationScale.title')}:</strong>{' '}
        {scaleNumber !== '-'
          ? `${scaleNumber} - ${scaleLabel}`
          : t('evaluationScale.notEvaluated')}
      </div>
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
                label: `1 - ${require('utils/evaluationScaleTranslations').translateEvaluationScale(
                  'LOW_CONTRIBUTION',
                  i18n.language
                )}`,
              },
              {
                value: 'PARCIAL_CONTRIBUTION',
                label: `2 - ${require('utils/evaluationScaleTranslations').translateEvaluationScale(
                  'PARCIAL_CONTRIBUTION',
                  i18n.language
                )}`,
              },
              {
                value: 'EXPECTED_CONTRIBUTION',
                label: `3 - ${require('utils/evaluationScaleTranslations').translateEvaluationScale(
                  'EXPECTED_CONTRIBUTION',
                  i18n.language
                )}`,
              },
              {
                value: 'EXCEED_CONTRIBUTION',
                label: `4 - ${require('utils/evaluationScaleTranslations').translateEvaluationScale(
                  'EXCEED_CONTRIBUTION',
                  i18n.language
                )}`,
              },
            ]}
          />
          <FormDropdownInput
            name="status"
            label={t('profile.status')}
            value={form.status || ''}
            onChange={handleChange}
            options={[
              { value: 'ONGOING', label: t('evaluationStatus.ONGOING') },
              { value: 'COMPLETED', label: t('evaluationStatus.COMPLETED') },
              { value: 'CLOSED', label: t('evaluationStatus.CLOSED') },
            ]}
          />
          <FormInput
            name="feedback"
            label={t('evaluations.feedback')}
            type="text"
            value={form.feedback || ''}
            onChange={handleChange}
          />
          <button className="btn btn-primary me-2" onClick={handleSave}>
            {t('common.save')}
          </button>
          <button
            className="btn btn-secondary"
            onClick={() => setEditing(false)}
          >
            {t('common.cancel')}
          </button>
        </div>
      ) : (
        <div className="mt-2">
          <button
            className="btn btn-outline-primary"
            onClick={() => setEditing(true)}
          >
            {t('common.edit')}
          </button>
        </div>
      )}
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
      firstName: PropTypes.string,
      lastName: PropTypes.string,
      photograph: PropTypes.string,
    }),
    comments: PropTypes.string,
  }).isRequired,
  onEdit: PropTypes.func,
};

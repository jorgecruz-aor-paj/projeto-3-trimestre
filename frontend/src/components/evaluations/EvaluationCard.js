import React from 'react';
import { ProfilePicture } from '../common/ProfilePicture';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { exportService } from 'services/exportService';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { translateEvaluationScale } from 'utils/evaluationScaleTranslations';

const scaleNumberMap = {
  LOW_CONTRIBUTION: 1,
  PARCIAL_CONTRIBUTION: 2,
  EXPECTED_CONTRIBUTION: 3,
  EXCEED_CONTRIBUTION: 4,
};

export const EvaluationCard = ({ evaluation }) => {
  const { t, i18n } = useTranslation();
  const { showAlert } = useAlert();

  const handleDownload = () => {
    const fetchFile = async () => {
      try {
        const response = await exportService.evaluationToPDF(evaluation.id);
        let filename = `evaluation_${evaluation.id}.pdf`;
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

  const scaleNumber = scaleNumberMap[evaluation.scale] || '-';
  const scaleLabel = evaluation.scale
    ? translateEvaluationScale(evaluation.scale, i18n.language)
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
      <div className="d-flex align-items-center mb-2">
        <ProfilePicture
          userId={evaluation.evaluatee?.id}
          className="rounded-circle me-3"
          alt={
            evaluation.evaluatee
              ? `${evaluation.evaluatee.firstName} ${evaluation.evaluatee.lastName}`
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
            {evaluation.evaluatee
              ? `${evaluation.evaluatee.firstName} ${evaluation.evaluatee.lastName}`
              : '-'}
          </div>
          <div className="text-muted small">
            {t('profile.status')}:{' '}
            {t(`evaluationStatus.${evaluation.status}`) || evaluation.status}
          </div>
        </div>
      </div>
      <div className="mb-1">
        <strong>{t('evaluationCycle.closureDate')}:</strong>{' '}
        {evaluation.evaluationCycle?.closureDate
          ? formatDateToDDMMYYYY(evaluation.evaluationCycle.closureDate)
          : t('evaluationCycle.closureDateNotAvailable')}
      </div>
      <div className="mb-1">
        <strong>{t('evaluationScale.title')}:</strong>{' '}
        {scaleNumber !== '-'
          ? `${scaleNumber} - ${scaleLabel}`
          : t('evaluationScale.notEvaluated')}
      </div>
    </div>
  );
};

EvaluationCard.propTypes = {
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
  }).isRequired,
};

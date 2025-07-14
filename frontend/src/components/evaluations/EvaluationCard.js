import React from 'react';
import '../../styles/profile-picture-responsive.css';
import { ProfilePicture } from 'components/common/ProfilePicture';
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
    <div className="card h-100 shadow-sm evaluation-card">
      <div className="card-body d-flex flex-column">
        {/* Header with profile and download */}
        <div className="d-flex justify-content-between align-items-start mb-3">
          <div className="d-flex align-items-center">
            <ProfilePicture
              userId={evaluation.evaluatee?.id}
              className="me-3 profile-picture-responsive"
              alt={
                evaluation.evaluatee
                  ? `${evaluation.evaluatee.firstName} ${evaluation.evaluatee.lastName}`
                  : 'Profile'
              }
              style={{ border: '1px solid #dee2e6' }}
            />
            <div>
              <h6 className="card-title mb-1">
                {evaluation.evaluatee
                  ? `${evaluation.evaluatee.firstName} ${evaluation.evaluatee.lastName}`
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
        <div className="d-flex gap-2 mb-3">
          <span
            className={`badge ${
              evaluation.status === 'COMPLETED'
                ? 'bg-success'
                : evaluation.status === 'ONGOING'
                ? 'bg-warning'
                : evaluation.status === 'CLOSED'
                ? 'bg-danger'
                : 'bg-secondary'
            }`}
          >
            <i className="bi bi-info-circle me-1"></i>
            {t(`evaluationStatus.${evaluation.status}`) || evaluation.status}
          </span>
          {scaleNumber !== '-' && (
            <span className="badge bg-primary">
              <i className="bi bi-award me-1"></i>
              {scaleNumber} - {scaleLabel}
            </span>
          )}
        </div>

        {/* Evaluation details */}
        <div className="flex-grow-1">
          <div className="mb-2">
            <small className="text-muted d-block">
              <i className="bi bi-calendar3 me-1"></i>
              {t('evaluationCycle.cycleClosureDate')}
            </small>
            <div>
              {evaluation.evaluationCycle?.closureDate
                ? formatDateToDDMMYYYY(evaluation.evaluationCycle.closureDate)
                : t('evaluationCycle.closureDateNotAvailable')}
            </div>
          </div>

          {evaluation.evaluator && (
            <div className="mb-2">
              <small className="text-muted d-block">
                <i className="bi bi-person-check me-1"></i>
                {t('filters.evaluator')}
              </small>
              <div>
                {`${evaluation.evaluator.firstName} ${evaluation.evaluator.lastName}`}
              </div>
            </div>
          )}
        </div>
      </div>

      <div className="card-footer bg-transparent border-top-0 pt-0">
        <small className="text-muted">
          <i className="bi bi-hash me-1"></i>
          ID: {evaluation.id}
        </small>
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
  }).isRequired,
};

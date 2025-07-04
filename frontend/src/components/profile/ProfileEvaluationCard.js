import React from 'react';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { translateEvaluationScale } from 'utils/evaluationScaleTranslations';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { exportService } from 'services/exportService';

export const ProfileEvaluationCard = ({ evaluation }) => {
  const { t, i18n } = useTranslation();
  const { showAlert } = useAlert();

  // Download handler: triggers file download from API response
  const handleDownload = () => {
    const fetchFile = async () => {
      try {
        console.log('Downloading evaluation PDF...');
        const response = await exportService.evaluationToPDF(evaluation.id);
        // If the service returns a Blob or File, handle download
        // Try to get filename from Content-Disposition header if available
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
        // Support both axios (response.data as Blob) and fetch (response as Blob)
        const blob = response.data instanceof Blob ? response.data : response;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.setAttribute('download', filename);
        // For Firefox, the link must be added to the document
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

  return (
    <div
      className="profile-evaluation-card position-relative"
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
      <div>
        {t('profile.date')}: {formatDateToDDMMYYYY(evaluation.createdAt)}
      </div>
      <div>
        {t('evaluationScale.title')}:{' '}
        {translateEvaluationScale(evaluation.scale, i18n.language)}
      </div>
      <div>
        {t('profile.evaluator')}:{' '}
        {evaluation.evaluator
          ? `${evaluation.evaluator.firstName} ${evaluation.evaluator.lastName}`
          : '-'}
      </div>
    </div>
  );
};

ProfileEvaluationCard.propTypes = {
  evaluation: PropTypes.shape({
    id: PropTypes.number,
    createdAt: PropTypes.string,
    scale: PropTypes.string,
    evaluator: PropTypes.shape({
      firstName: PropTypes.string,
      lastName: PropTypes.string,
    }),
  }).isRequired,
};


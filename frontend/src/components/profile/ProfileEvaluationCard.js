import React from 'react';
import PropTypes from 'prop-types';
import { formatDateToDDMMYYYY } from 'utils/dateUtils';
import { translateEvaluationScale } from 'utils/evaluationScaleTranslations';
import { useTranslation } from 'react-i18next';
import { exportService } from 'services/exportService';
const ProfileEvaluationCard = ({ evaluation }) => {
  const { t, i18n } = useTranslation();

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
        alert(t('profile.downloadError'));
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
        {/* SVG download icon TODO replace with fontAwesome pdf download icon */}

        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="20"
          height="20"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path d="M10 2a1 1 0 0 1 1 1v7.586l2.293-2.293a1 1 0 1 1 1.414 1.414l-4 4a1 1 0 0 1-1.414 0l-4-4A1 1 0 1 1 6.707 8.293L9 10.586V3a1 1 0 0 1 1-1zm-7 13a1 1 0 0 1 1-1h12a1 1 0 1 1 0 2H4a1 1 0 0 1-1-1z" />
        </svg>
      </button>
      <div>
        {t('profile.date')}: {formatDateToDDMMYYYY(evaluation.createdAt)}
      </div>
      <div>
        {t('profile.scale')}:{' '}
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

export default ProfileEvaluationCard;

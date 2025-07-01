import React from 'react';
import { formatDateToDDMMYYYY } from '../../utils/dateUtils';

const ProfileEvaluationCard = ({ evaluation }) => {
  return (
    <div className="profile-evaluation-card">
      <div>
        <strong>Evaluation #{evaluation.id}</strong>
      </div>
      <div>Date: {formatDateToDDMMYYYY(evaluation.createdAt)}</div>
      <div>Scale: {evaluation.scale}</div>
      <div>
        Evaluator:{' '}
        {evaluation.evaluator
          ? `${evaluation.evaluator.firstName} ${evaluation.evaluator.lastName}`
          : '-'}
      </div>
      <button
        className="download-evaluation-btn"
        onClick={() => {
          /* TODO: implement download */
        }}
      >
        Download Evaluation
      </button>
    </div>
  );
};

export default ProfileEvaluationCard;

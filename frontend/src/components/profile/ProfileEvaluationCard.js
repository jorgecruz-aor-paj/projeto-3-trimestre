import React from 'react';

const ProfileEvaluationCard = ({ evaluation }) => {
  // Convert epoch seconds to readable date
  const formatDate = ts => {
    if (!ts) return '-';
    const d = new Date(ts * 1000);
    return d.toLocaleDateString();
  };

  return (
    <div className="profile-evaluation-card">
      <div>
        <strong>Evaluation #{evaluation.id}</strong>
      </div>
      <div>Status: {evaluation.status}</div>
      <div>Scale: {evaluation.scale}</div>
      <div>Feedback: {evaluation.feedback}</div>
      <div>Created: {formatDate(evaluation.createdAt)}</div>
      <div>Last Edited: {formatDate(evaluation.editedAt)}</div>
      <div>Evaluation Cycle ID: {evaluation.evaluationCycleId}</div>
      <div>Evaluatee ID: {evaluation.evaluateeId}</div>
      <div>Evaluator ID: {evaluation.evaluatorId}</div>
    </div>
  );
};

export default ProfileEvaluationCard;

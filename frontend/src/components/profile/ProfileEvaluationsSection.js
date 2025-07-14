import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { Row, Col, Spinner } from 'react-bootstrap';
import { useEvaluationStore } from 'stores/EvaluationStore';
import { ProfileEvaluationsFilter } from 'components/profile/ProfileEvaluationsFilter';
import { EvaluationCard } from 'components/evaluations/EvaluationCard';
import { useTranslation } from 'react-i18next';

export const ProfileEvaluationsSection = ({ userId }) => {
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();
  const [filters, setFilters] = useState({
    evaluatedId: userId,
    status: 'CLOSED',
    cycleClosed: true,
  });
  const { t } = useTranslation();

  useEffect(() => {
    // Filter out display-only fields before sending to API
    const apiFilters = { ...filters };
    delete apiFilters.startDateDisplay;
    delete apiFilters.endDateDisplay;

    fetchEvaluations(apiFilters);
  }, [JSON.stringify(filters), fetchEvaluations]);

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('common.loading')}</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger">
        {t('common.error') || 'Error loading evaluations'}
      </div>
    );
  }

  return (
    <div className="profile-evaluations-section">
      {/* Filters section */}
      <Row className="mb-4">
        <Col>
          <ProfileEvaluationsFilter
            filters={filters}
            setFilters={setFilters}
            displayedUserId={userId}
          />
        </Col>
      </Row>

      {/* Evaluations list */}
      <Row>
        <Col>
          {evaluations && evaluations.length > 0 ? (
            <Row className="evaluation-list">
              {evaluations.map(evaluation => (
                <Col key={evaluation.id} md={6} lg={4} className="mb-4">
                  <div className="h-100">
                    <EvaluationCard evaluation={evaluation} />
                  </div>
                </Col>
              ))}
            </Row>
          ) : (
            <div className="text-center py-5">
              <i className="bi bi-clipboard-check display-4 text-muted"></i>
              <h5 className="mt-3 text-muted">{t('profile.noEvaluations')}</h5>
              <p className="text-muted">
                {t(
                  'evaluations.admin.noEvaluationsDesc',
                  'No evaluations match your current criteria'
                )}
              </p>
            </div>
          )}
        </Col>
      </Row>
    </div>
  );
};

ProfileEvaluationsSection.propTypes = {
  userId: PropTypes.number.isRequired,
};

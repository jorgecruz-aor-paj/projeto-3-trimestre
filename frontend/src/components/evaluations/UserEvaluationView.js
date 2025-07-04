import React, { useState, useEffect } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { EvaluationFilter } from 'components/filters';
import { EvaluationList } from './EvaluationList';
import { useAuthStore } from 'stores/AuthStore';
import { useEvaluationStore } from 'stores/EvaluationStore';
import { useTranslation } from 'react-i18next';
import 'styles/pages/EvaluationsPage.css';

/**
 * User evaluation view component for regular authenticated users
 * Displays user's evaluations with appropriate filtering options
 * @returns {React.Component} User evaluation view
 */
export const UserEvaluationView = () => {
  const { t } = useTranslation();
  const [filters, setFilters] = useState({});
  const user = useAuthStore(state => state.user);
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();

  useEffect(() => {
    fetchEvaluations(filters);
  }, [JSON.stringify(filters)]);

  return (
    <div className="evaluations-page">
      <Container className="py-4">
        {/* Header section */}
        <Row className="mb-4">
          <Col>
            <div className="page-header">
              <h2 className="mb-2">
                {t('evaluations.user.title', 'My Evaluations')}
              </h2>
              <p className="text-muted mb-0">
                {t(
                  'evaluations.user.subtitle',
                  'View and manage your performance evaluations'
                )}
              </p>
            </div>
          </Col>
        </Row>

        {/* Filters section */}
        <Row className="mb-4">
          <Col>
            <EvaluationFilter
              filters={filters}
              setFilters={setFilters}
              user={user}
            />
          </Col>
        </Row>

        {/* Evaluations list */}
        <Row>
          <Col>
            <EvaluationList
              evaluations={evaluations}
              loading={loading}
              error={error}
              renderCheckbox={false}
            />
          </Col>
        </Row>
      </Container>
    </div>
  );
};

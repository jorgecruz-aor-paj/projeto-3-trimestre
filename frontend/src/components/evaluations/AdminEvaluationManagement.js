import React, { useState, useEffect } from 'react';
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Table,
  Badge,
  Spinner,
  Alert,
  OverlayTrigger,
  Tooltip,
} from 'react-bootstrap';
import { EvaluationFilter } from 'components/filters';
import { EvaluationList } from './EvaluationList';
import { useEvaluationStore } from 'stores/EvaluationStore';
import { useAuthStore } from 'stores/AuthStore';
import { useTranslation } from 'react-i18next';
import { useAlert } from 'contexts/AlertContext';
import { translateEvaluationScale } from 'utils/evaluationScaleTranslations';
import { evaluationService } from 'services/evaluationService';
import 'styles/pages/EvaluationsPage.css';

/**
 * Admin evaluation management component
 * Provides comprehensive evaluation management tools for administrators
 * @returns {React.Component} Admin evaluation management interface
 */
export const AdminEvaluationManagement = () => {
  const { t, i18n } = useTranslation();
  const [filters, setFilters] = useState({});
  const [selectedEvaluations, setSelectedEvaluations] = useState([]); // array of evaluation ids
  const [viewMode, setViewMode] = useState('cards'); // 'table' or 'cards'
  const user = useAuthStore(state => state.user);
  const { evaluations, fetchEvaluations, loading, error } =
    useEvaluationStore();
  const { showAlert, showConfirm } = useAlert();

  useEffect(() => {
    fetchEvaluations(filters);
  }, [JSON.stringify(filters)]);

  // Handler for toggling selection
  const handleSelectEvaluation = (evaluationId, checked) => {
    setSelectedEvaluations(prev =>
      checked ? [...prev, evaluationId] : prev.filter(id => id !== evaluationId)
    );
  };

  // Handler for closing selected evaluations
  const handleCloseSelected = async () => {
    if (!selectedEvaluations.length) return;

    // Show confirmation dialog
    showConfirm({
      title: t(
        'evaluations.admin.confirmCloseTitle',
        'Confirm Close Evaluations'
      ),
      message: t(
        'evaluations.admin.confirmCloseMessage',
        `Are you sure you want to close ${selectedEvaluations.length} selected evaluation(s)? This action cannot be undone.`
      ),
      confirmLabel: t(
        'evaluations.admin.confirmCloseButton',
        'Close Evaluations'
      ),
      variant: 'danger',
      onConfirm: async () => {
        // Find the cycleId from the first selected evaluation (assuming all are from the same cycle)
        const selected = evaluations.find(e => e.id === selectedEvaluations[0]);
        const cycleId = selected?.evaluationCycle?.id;
        if (!cycleId) {
          showAlert({
            title: t('common.error', 'Error'),
            message: t(
              'evaluations.admin.noCycleError',
              'Could not determine evaluation cycle.'
            ),
            variant: 'danger',
          });
          return;
        }
        try {
          await evaluationService.closeEvaluation({
            cycleId,
            evaluationIds: selectedEvaluations,
          });
          showAlert({
            title: t('common.success', 'Success'),
            message: t(
              'evaluations.admin.closeSuccess',
              'Selected evaluations closed successfully.'
            ),
            variant: 'success',
          });
          setSelectedEvaluations([]);
          fetchEvaluations(filters);
        } catch (err) {
          showAlert({
            title: t('common.error', 'Error'),
            message:
              err?.message ||
              t('evaluations.admin.closeError', 'Failed to close evaluations.'),
            variant: 'danger',
          });
        }
      },
    });
  };

  const renderEvaluationTable = () => {
    if (loading) {
      return (
        <div className="text-center py-5">
          <Spinner animation="border" variant="primary" className="mb-3" />
          <p>{t('common.loading')}</p>
        </div>
      );
    }

    if (evaluations.length === 0) {
      return (
        <div className="text-center py-5">
          <i className="bi bi-clipboard-check display-4 text-muted"></i>
          <h5 className="mt-3 text-muted">
            {t('evaluations.noResults', 'No evaluations found')}
          </h5>
          <p className="text-muted">
            {t(
              'evaluations.admin.noEvaluationsDesc',
              'No evaluations match your current filters'
            )}
          </p>
        </div>
      );
    }

    return (
      <div className="table-responsive">
        <Table hover className="mb-0">
          <thead className="table-light">
            <tr>
              {viewMode === 'table' && (
                <th width="40">
                  <input
                    type="checkbox"
                    className="form-check-input evaluation-selection-checkbox"
                    onChange={e => {
                      if (e.target.checked) {
                        setSelectedEvaluations(
                          evaluations.map(evaluation => evaluation.id)
                        );
                      } else {
                        setSelectedEvaluations([]);
                      }
                    }}
                    checked={
                      selectedEvaluations.length === evaluations.length &&
                      evaluations.length > 0
                    }
                    style={{
                      width: '20px',
                      height: '20px',
                      cursor: 'pointer',
                    }}
                  />
                </th>
              )}
              <th>{t('profile.sortFields.id', 'ID')}</th>
              <th>{t('filters.evaluatee', 'Evaluatee')}</th>
              <th>{t('filters.evaluator', 'Evaluator')}</th>
              <th>{t('common.status', 'Status')}</th>
              <th>{t('evaluationScale.title', 'Scale')}</th>
              <th>{t('filters.creationDate', 'Date')}</th>
              <th className="text-center">{t('common.actions', 'Actions')}</th>
            </tr>
          </thead>
          <tbody>
            {evaluations.map(evaluation => (
              <tr
                key={evaluation.id}
                className={
                  selectedEvaluations.includes(evaluation.id)
                    ? 'selected-evaluation-row'
                    : ''
                }
              >
                {viewMode === 'table' && (
                  <td>
                    <input
                      type="checkbox"
                      className="form-check-input evaluation-selection-checkbox"
                      checked={selectedEvaluations.includes(evaluation.id)}
                      onChange={e =>
                        handleSelectEvaluation(evaluation.id, e.target.checked)
                      }
                      style={{
                        width: '20px',
                        height: '20px',
                        cursor: 'pointer',
                        backgroundColor: selectedEvaluations.includes(
                          evaluation.id
                        )
                          ? '#0d6efd'
                          : 'white',
                        border: selectedEvaluations.includes(evaluation.id)
                          ? '2px solid #0d6efd'
                          : '2px solid #dee2e6',
                      }}
                    />
                  </td>
                )}
                <td>#{evaluation.id}</td>
                <td>
                  {evaluation.evaluatee
                    ? `${evaluation.evaluatee.firstName} ${evaluation.evaluatee.lastName}`
                    : '-'}
                </td>
                <td>
                  {evaluation.evaluator
                    ? `${evaluation.evaluator.firstName} ${evaluation.evaluator.lastName}`
                    : '-'}
                </td>
                <td>
                  <Badge
                    bg={
                      evaluation.status === 'COMPLETED'
                        ? 'success'
                        : evaluation.status === 'ONGOING'
                        ? 'warning'
                        : evaluation.status === 'CLOSED'
                        ? 'danger'
                        : 'secondary'
                    }
                  >
                    {t(
                      `evaluationStatus.${evaluation.status}`,
                      evaluation.status
                    )}
                  </Badge>
                </td>
                <td>
                  {evaluation.scale ? (
                    <Badge bg="info">
                      {translateEvaluationScale(
                        evaluation.scale,
                        i18n.language
                      )}
                    </Badge>
                  ) : (
                    <span className="text-muted">-</span>
                  )}
                </td>
                <td>
                  {evaluation.evaluationCycle?.closureDate
                    ? new Date(
                        evaluation.evaluationCycle.closureDate
                      ).toLocaleDateString()
                    : '-'}
                </td>
                <td className="text-center">
                  <OverlayTrigger
                    placement="top"
                    overlay={<Tooltip>{t('common.view', 'View')}</Tooltip>}
                  >
                    <Button
                      variant="outline-primary"
                      size="sm"
                      className="me-1"
                    >
                      <i className="bi bi-eye"></i>
                    </Button>
                  </OverlayTrigger>
                  {evaluation.status !== 'CLOSED' && (
                    <OverlayTrigger
                      placement="top"
                      overlay={<Tooltip>{t('common.edit', 'Edit')}</Tooltip>}
                    >
                      <Button variant="outline-secondary" size="sm">
                        <i className="bi bi-pencil"></i>
                      </Button>
                    </OverlayTrigger>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    );
  };

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('common.loading')}</p>
      </div>
    );
  }

  return (
    <Container fluid className="py-4">
      {/* Header */}
      <Row className="mb-4">
        <Col>
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h2>{t('evaluations.admin.title', 'Evaluation Management')}</h2>
              <p className="text-muted mb-0">
                {t(
                  'evaluations.admin.subtitle',
                  'Manage and oversee all organizational evaluations'
                )}
              </p>
            </div>
            <div className="d-flex gap-2">
              <Button
                variant="outline-primary"
                onClick={() => {
                  /* TODO: Open settings modal */
                }}
              >
                <i className="bi bi-gear me-2"></i>
                {t('evaluations.admin.settings', 'Settings')}
              </Button>
              <Button
                variant="primary"
                onClick={() => {
                  /* TODO: Open create cycle modal */
                }}
              >
                <i className="bi bi-plus-circle me-2"></i>
                {t('evaluations.admin.createCycle', 'Create Evaluation Cycle')}
              </Button>
            </div>
          </div>
        </Col>
      </Row>

      {/* Statistics cards */}
      <Row className="mb-4">
        <Col md={3} className="mb-3">
          <Card className="stats-card h-100">
            <Card.Body className="text-center">
              <div className="stats-icon text-primary mb-2">
                <i
                  className="bi bi-clipboard-check"
                  style={{ fontSize: '2rem' }}
                ></i>
              </div>
              <h5 className="card-title">{evaluations?.length || 0}</h5>
              <p className="card-text text-muted">
                {t('evaluations.stats.total', 'Total Evaluations')}
              </p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3} className="mb-3">
          <Card className="stats-card h-100">
            <Card.Body className="text-center">
              <div className="stats-icon text-warning mb-2">
                <i className="bi bi-clock" style={{ fontSize: '2rem' }}></i>
              </div>
              <h5 className="card-title">
                {evaluations?.filter(e => e.status === 'ONGOING')?.length || 0}
              </h5>
              <p className="card-text text-muted">
                {t('evaluations.stats.ongoing', 'Ongoing')}
              </p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3} className="mb-3">
          <Card className="stats-card h-100">
            <Card.Body className="text-center">
              <div className="stats-icon text-success mb-2">
                <i
                  className="bi bi-check-circle"
                  style={{ fontSize: '2rem' }}
                ></i>
              </div>
              <h5 className="card-title">
                {evaluations?.filter(e => e.status === 'COMPLETED')?.length ||
                  0}
              </h5>
              <p className="card-text text-muted">
                {t('evaluations.stats.completed', 'Completed')}
              </p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3} className="mb-3">
          <Card className="stats-card h-100">
            <Card.Body className="text-center">
              <div className="stats-icon text-danger mb-2">
                <i className="bi bi-x-circle" style={{ fontSize: '2rem' }}></i>
              </div>
              <h5 className="card-title">
                {evaluations?.filter(e => e.status === 'CLOSED')?.length || 0}
              </h5>
              <p className="card-text text-muted">
                {t('evaluations.stats.closed', 'Closed')}
              </p>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Filters */}
      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Body>
              <EvaluationFilter
                filters={filters}
                setFilters={setFilters}
                user={user}
              />
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Error Alert */}
      {error && (
        <Row className="mb-4">
          <Col>
            <Alert variant="danger">
              <Alert.Heading>{t('common.error', 'Error')}</Alert.Heading>
              <p>{error}</p>
              <Button
                variant="outline-danger"
                onClick={() => fetchEvaluations(filters)}
              >
                {t('common.retry')}
              </Button>
            </Alert>
          </Col>
        </Row>
      )}

      {/* Evaluations List/Table */}
      <Row>
        <Col>
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <div className="d-flex align-items-center">
                <h5 className="mb-0 me-3">
                  {t('evaluations.title', 'Evaluations')}
                </h5>
                <Badge bg="secondary">
                  {evaluations.length}{' '}
                  {t('evaluations.stats.total', 'evaluations')}
                </Badge>
              </div>
              <div className="d-flex align-items-center gap-2">
                {/* Bulk Actions */}
                {selectedEvaluations.length > 0 && (
                  <div className="me-3 bulk-actions">
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={handleCloseSelected}
                      className="me-2"
                    >
                      <i className="bi bi-x-circle me-1"></i>
                      {t('evaluations.admin.closeSelected', 'Close Selected')}
                    </Button>
                    <Badge bg="primary" className="py-2 px-3">
                      <i className="bi bi-check-circle me-1"></i>
                      {selectedEvaluations.length}{' '}
                      {t('evaluations.admin.selected', 'selected')}
                    </Badge>
                  </div>
                )}

                {/* View Toggle */}
                <div className="btn-group" role="group">
                  <Button
                    variant={
                      viewMode === 'table' ? 'primary' : 'outline-secondary'
                    }
                    size="sm"
                    onClick={() => setViewMode('table')}
                    title={t('common.list', 'List View')}
                  >
                    <i className="bi bi-list"></i>
                  </Button>
                  <Button
                    variant={
                      viewMode === 'cards' ? 'primary' : 'outline-secondary'
                    }
                    size="sm"
                    onClick={() => setViewMode('cards')}
                    title={t('common.cards', 'Card View')}
                  >
                    <i className="bi bi-grid-3x3-gap"></i>
                  </Button>
                </div>
              </div>
            </Card.Header>
            <Card.Body className="p-0">
              {viewMode === 'table' ? (
                renderEvaluationTable()
              ) : (
                <div className="p-3">
                  <EvaluationList
                    evaluations={evaluations}
                    loading={loading}
                    error={error}
                    renderCheckbox={true}
                    selectedEvaluations={selectedEvaluations}
                    onSelectEvaluation={handleSelectEvaluation}
                    userRole={user?.role}
                  />
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

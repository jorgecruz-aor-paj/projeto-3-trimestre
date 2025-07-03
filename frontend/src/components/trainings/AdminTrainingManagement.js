import React, { useState, useEffect } from 'react';
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Table,
  Badge,
  Form,
  Modal,
  Spinner,
  Alert,
  OverlayTrigger,
  Tooltip,
} from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { trainingService } from 'services/trainingService';
import { useAlert } from 'contexts/AlertContext';
import { TrainingForm } from 'components/forms';
import { TrainingFilter } from 'components/filters';

/**
 * Admin Training Management component
 * Allows admins to list, create, edit and manage trainings
 * @returns {React.Component} Admin training management interface
 */
export const AdminTrainingManagement = () => {
  const { t } = useTranslation();
  const { showAlert, showConfirm } = useAlert();

  // State management
  const [trainings, setTrainings] = useState([]);
  const [trainingAreas, setTrainingAreas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({});

  // Modal states
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showAreaModal, setShowAreaModal] = useState(false);
  const [selectedTraining, setSelectedTraining] = useState(null);

  // Form states
  const [formLoading, setFormLoading] = useState(false);
  const [newAreaName, setNewAreaName] = useState('');

  const fetchTrainings = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await trainingService.getAll(filters);
      setTrainings(data);
    } catch (err) {
      setError(err.message || t('trainings.admin.fetchError'));
      console.error('Failed to fetch trainings:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchTrainingAreas = async () => {
    try {
      const areas = await trainingService.getAllAreas();
      setTrainingAreas(areas);
    } catch (err) {
      console.error('Failed to fetch training areas:', err);
    }
  };

  // Fetch data on component mount
  useEffect(() => {
    fetchTrainings();
    fetchTrainingAreas();
  }, [filters]);

  const handleCreateTraining = async trainingData => {
    try {
      setFormLoading(true);
      await trainingService.create(trainingData);
      showAlert({
        title: t('trainings.admin.success'),
        message: t('trainings.admin.createSuccess'),
      });
      setShowCreateModal(false);
      fetchTrainings();
    } catch (err) {
      showAlert({
        title: t('trainings.admin.error'),
        message: err.message || t('trainings.admin.createError'),
        variant: 'danger',
      });
    } finally {
      setFormLoading(false);
    }
  };

  const handleEditTraining = async trainingData => {
    try {
      setFormLoading(true);
      await trainingService.update(trainingData);
      showAlert({
        title: t('trainings.admin.success'),
        message: t('trainings.admin.updateSuccess'),
      });
      setShowEditModal(false);
      setSelectedTraining(null);
      fetchTrainings();
    } catch (err) {
      showAlert({
        title: t('trainings.admin.error'),
        message: err.message || t('trainings.admin.updateError'),
        variant: 'danger',
      });
    } finally {
      setFormLoading(false);
    }
  };

  const handleDeactivateTraining = training => {
    showConfirm({
      title: t('trainings.admin.deactivateTitle'),
      message: t('trainings.admin.deactivateMessage', {
        title: training.title,
      }),
      onConfirm: async () => {
        try {
          await trainingService.deactivate(training.id);
          showAlert({
            title: t('trainings.admin.success'),
            message: t('trainings.admin.deactivateSuccess'),
          });
          fetchTrainings();
        } catch (err) {
          showAlert({
            title: t('trainings.admin.error'),
            message: err.message || t('trainings.admin.deactivateError'),
            variant: 'danger',
          });
        }
      },
    });
  };

  const handleCreateArea = async () => {
    if (!newAreaName.trim()) return;

    try {
      setFormLoading(true);
      await trainingService.manageTrainingArea({ name: newAreaName });
      showAlert({
        title: t('trainings.admin.success'),
        message: t('trainings.admin.areaCreateSuccess'),
      });
      setNewAreaName('');
      setShowAreaModal(false);
      fetchTrainingAreas();
    } catch (err) {
      showAlert({
        title: t('trainings.admin.error'),
        message: err.message || t('trainings.admin.areaCreateError'),
        variant: 'danger',
      });
    } finally {
      setFormLoading(false);
    }
  };

  const formatDuration = minutes => {
    if (minutes < 60) return `${minutes}m`;
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return mins > 0 ? `${hours}h ${mins}m` : `${hours}h`;
  };

  const getLanguageBadgeVariant = language => {
    const variants = {
      English: 'primary',
      Portuguese: 'success',
      Spanish: 'info',
      French: 'warning',
      German: 'secondary',
    };
    return variants[language] || 'outline-dark';
  };

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('trainings.admin.loading')}</p>
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
              <h2>{t('trainings.admin.title', 'Training Management')}</h2>
              <p className="text-muted mb-0">
                {t(
                  'trainings.admin.subtitle',
                  'Manage training programs and areas'
                )}
              </p>
            </div>
            <div className="d-flex gap-2">
              <Button
                variant="outline-primary"
                onClick={() => setShowAreaModal(true)}
              >
                <i className="bi bi-plus-circle me-2"></i>
                {t('trainings.admin.newArea', 'New Area')}
              </Button>
              <Button
                variant="primary"
                onClick={() => setShowCreateModal(true)}
              >
                <i className="bi bi-plus-circle me-2"></i>
                {t('trainings.admin.newTraining', 'New Training')}
              </Button>
            </div>
          </div>
        </Col>
      </Row>

      {/* Filters */}
      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Body>
              <TrainingFilter
                filters={filters}
                onFiltersChange={setFilters}
                trainingAreas={trainingAreas}
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
              <Alert.Heading>{t('trainings.admin.error')}</Alert.Heading>
              <p>{error}</p>
              <Button variant="outline-danger" onClick={fetchTrainings}>
                {t('common.retry')}
              </Button>
            </Alert>
          </Col>
        </Row>
      )}

      {/* Trainings Table */}
      <Row>
        <Col>
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">{t('trainings.admin.trainingsList')}</h5>
              <Badge bg="secondary">
                {trainings.length} {t('trainings.admin.trainingsCount')}
              </Badge>
            </Card.Header>
            <Card.Body className="p-0">
              {trainings.length === 0 ? (
                <div className="text-center py-5">
                  <i className="bi bi-book display-4 text-muted"></i>
                  <h5 className="mt-3 text-muted">
                    {t('trainings.admin.noTrainings')}
                  </h5>
                  <p className="text-muted">
                    {t('trainings.admin.noTrainingsDesc')}
                  </p>
                  <Button
                    variant="primary"
                    onClick={() => setShowCreateModal(true)}
                  >
                    {t('trainings.admin.createFirst')}
                  </Button>
                </div>
              ) : (
                <div className="table-responsive">
                  <Table hover className="mb-0">
                    <thead className="table-light">
                      <tr>
                        <th>{t('trainings.fields.title')}</th>
                        <th>{t('trainings.fields.area')}</th>
                        <th>{t('trainings.fields.duration')}</th>
                        <th>{t('trainings.fields.language')}</th>
                        <th>{t('trainings.fields.status')}</th>
                        <th>{t('trainings.fields.created')}</th>
                        <th className="text-center">{t('common.actions')}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {trainings.map(training => (
                        <tr key={training.id}>
                          <td>
                            <div>
                              <strong>{training.title}</strong>
                              {training.description && (
                                <div
                                  className="text-muted small text-truncate"
                                  style={{ maxWidth: '200px' }}
                                >
                                  {training.description}
                                </div>
                              )}
                            </div>
                          </td>
                          <td>{training.trainingArea?.name}</td>
                          <td>{formatDuration(training.duration)}</td>
                          <td>
                            <Badge
                              bg={getLanguageBadgeVariant(training.language)}
                            >
                              {training.language}
                            </Badge>
                          </td>
                          <td>
                            <Badge
                              bg={training.isActive ? 'success' : 'secondary'}
                            >
                              {training.isActive
                                ? t('common.active')
                                : t('common.inactive')}
                            </Badge>
                          </td>
                          <td className="text-muted small">
                            {new Date(training.createdAt).toLocaleDateString()}
                          </td>
                          <td>
                            <div className="d-flex justify-content-center gap-1">
                              <OverlayTrigger
                                placement="top"
                                overlay={<Tooltip>{t('common.edit')}</Tooltip>}
                              >
                                <Button
                                  variant="outline-primary"
                                  size="sm"
                                  onClick={() => {
                                    setSelectedTraining(training);
                                    setShowEditModal(true);
                                  }}
                                >
                                  <i className="bi bi-pencil"></i>
                                </Button>
                              </OverlayTrigger>

                              {training.isActive && (
                                <OverlayTrigger
                                  placement="top"
                                  overlay={
                                    <Tooltip>
                                      {t('trainings.admin.deactivate')}
                                    </Tooltip>
                                  }
                                >
                                  <Button
                                    variant="outline-danger"
                                    size="sm"
                                    onClick={() =>
                                      handleDeactivateTraining(training)
                                    }
                                  >
                                    <i className="bi bi-x-circle"></i>
                                  </Button>
                                </OverlayTrigger>
                              )}

                              {training.link && (
                                <OverlayTrigger
                                  placement="top"
                                  overlay={
                                    <Tooltip>
                                      {t('trainings.admin.viewLink')}
                                    </Tooltip>
                                  }
                                >
                                  <Button
                                    variant="outline-info"
                                    size="sm"
                                    as="a"
                                    href={training.link}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                  >
                                    <i className="bi bi-link-45deg"></i>
                                  </Button>
                                </OverlayTrigger>
                              )}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Create Training Modal */}
      <Modal
        show={showCreateModal}
        onHide={() => setShowCreateModal(false)}
        size="lg"
      >
        <Modal.Header closeButton>
          <Modal.Title>{t('trainings.admin.newTraining')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <TrainingForm
            onSubmit={handleCreateTraining}
            onCancel={() => setShowCreateModal(false)}
            trainingAreas={trainingAreas}
            loading={formLoading}
          />
        </Modal.Body>
      </Modal>

      {/* Edit Training Modal */}
      <Modal
        show={showEditModal}
        onHide={() => {
          setShowEditModal(false);
          setSelectedTraining(null);
        }}
        size="lg"
      >
        <Modal.Header closeButton>
          <Modal.Title>{t('trainings.admin.editTraining')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedTraining && (
            <TrainingForm
              training={selectedTraining}
              onSubmit={handleEditTraining}
              onCancel={() => {
                setShowEditModal(false);
                setSelectedTraining(null);
              }}
              trainingAreas={trainingAreas}
              loading={formLoading}
              isEditing={true}
            />
          )}
        </Modal.Body>
      </Modal>

      {/* Create Training Area Modal */}
      <Modal
        show={showAreaModal}
        onHide={() => {
          setShowAreaModal(false);
          setNewAreaName('');
        }}
      >
        <Modal.Header closeButton>
          <Modal.Title>{t('trainings.admin.newArea')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form
            onSubmit={e => {
              e.preventDefault();
              handleCreateArea();
            }}
          >
            <Form.Group className="mb-3">
              <Form.Label>{t('trainings.fields.areaName')}</Form.Label>
              <Form.Control
                type="text"
                placeholder={t('trainings.admin.areaNamePlaceholder')}
                value={newAreaName}
                onChange={e => setNewAreaName(e.target.value)}
                required
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => {
              setShowAreaModal(false);
              setNewAreaName('');
            }}
          >
            {t('common.cancel')}
          </Button>
          <Button
            variant="primary"
            onClick={handleCreateArea}
            disabled={!newAreaName.trim() || formLoading}
          >
            {formLoading ? (
              <Spinner animation="border" size="sm" className="me-2" />
            ) : (
              <i className="bi bi-plus-circle me-2"></i>
            )}
            {t('common.create')}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

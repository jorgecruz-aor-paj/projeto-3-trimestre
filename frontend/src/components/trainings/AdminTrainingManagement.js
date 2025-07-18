import React, { useState, useEffect } from 'react';
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Badge,
  Form,
  Modal,
  Spinner,
  Alert,
} from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { trainingService } from 'services/trainingService';
import { useAlert } from 'contexts/AlertContext';
import { TrainingForm } from 'components/forms';
import { TrainingFilter } from 'components/filters';
import { TrainingCard } from './TrainingCard';
import { useFormValidation } from 'utils/validation';
import { trainingAreaValidationRules } from 'utils/validation';
import { useErrorMessage } from 'hooks/useErrorMessage';

/**
 * Admin Training Management component
 * Allows admins to list, create, edit and manage trainings
 * @returns {React.Component} Admin training management interface
 */
export const AdminTrainingManagement = () => {
  const { t } = useTranslation();
  const { showAlert, showConfirm } = useAlert();
  const { getErrorMessage } = useErrorMessage();

  // Validation hook for training area form
  const {
    formData: areaFormData,
    errors: areaErrors,
    handleChange: handleAreaChange,
    validateAll: validateArea,
    resetForm: resetAreaForm,
  } = useFormValidation({ name: '' }, trainingAreaValidationRules);

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

      // Parse and transform the training data to match NewTrainingDTO structure
      const newTrainingData = {
        title: trainingData.title,
        duration: parseInt(trainingData.duration, 10),
        description: trainingData.description || '',
        link: trainingData.link || '',
        language: trainingData.language,
        //BUGFIX Send trainingAreaId instead of trainingArea object
        trainingAreaId:
          trainingData.trainingAreaId || trainingData.trainingArea?.id,
      };

      await trainingService.create(newTrainingData);
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

      // Find the original training object to get all fields
      const originalTraining = trainings.find(t => t.id === trainingData.id);

      // Find the trainingArea object based on trainingAreaId that came from the form
      const trainingAreaObj = trainingAreas.find(
        area => area.id === trainingData.trainingAreaId
      );

      // Format the training object for the backend
      // Reconstruct the complete object with all required fields
      const trainingToUpdate = {
        id: trainingData.id,
        title: trainingData.title,
        duration: trainingData.duration,
        description: trainingData.description,
        link: trainingData.link,
        language: trainingData.language,
        // Preserve original fields or use defaults
        createdAt: originalTraining?.createdAt || new Date().toISOString(),
        editedAt: new Date().toISOString(),
        // Use the trainingArea object, not just the ID
        trainingArea: trainingAreaObj || originalTraining?.trainingArea,
        active:
          originalTraining?.active !== undefined
            ? originalTraining.active
            : true,
      };

      await trainingService.update(trainingToUpdate);
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
      message: t('trainings.admin.deactivateMessage').replace(
        '{title}',
        training.title
      ),
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
    try {
      // Validate form data
      if (!validateArea()) {
        return;
      }

      setFormLoading(true);
      await trainingService.manageTrainingArea({ name: areaFormData.name });
      showAlert({
        title: t('trainings.admin.success'),
        message: t('trainings.admin.areaCreateSuccess'),
      });
      setShowAreaModal(false);
      resetAreaForm();
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

  // Helper functions moved to TrainingCard component

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

      {/* Trainings Grid */}
      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">{t('trainings.admin.trainingsList')}</h5>
              <Badge bg="secondary">
                {trainings.length} {t('trainings.admin.trainingsCount')}
              </Badge>
            </Card.Header>
            <Card.Body>
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
                <Row xs={1} md={2} lg={3} className="g-4">
                  {trainings.map(training => (
                    <Col key={training.id}>
                      <TrainingCard
                        training={training}
                        onEdit={
                          training.active === false
                            ? undefined
                            : () => {
                                setSelectedTraining(training);
                                setShowEditModal(true);
                              }
                        }
                        onView={
                          training.active === false
                            ? () => {
                                setSelectedTraining(training);
                                setShowEditModal(true);
                              }
                            : undefined
                        }
                        onDeactivate={handleDeactivateTraining}
                      />
                    </Col>
                  ))}
                </Row>
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
              readOnly={selectedTraining.active === false}
            />
          )}
        </Modal.Body>
      </Modal>

      {/* Create Training Area Modal */}
      <Modal
        show={showAreaModal}
        onHide={() => {
          setShowAreaModal(false);
          resetAreaForm();
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
                value={areaFormData.name}
                onChange={e => handleAreaChange('name', e.target.value)}
                isInvalid={!!areaErrors.name}
                required
              />
              {areaErrors.name && (
                <Form.Control.Feedback type="invalid">
                  {getErrorMessage(areaErrors.name)}
                </Form.Control.Feedback>
              )}
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => {
              setShowAreaModal(false);
              resetAreaForm();
            }}
          >
            {t('common.cancel')}
          </Button>
          <Button
            variant="primary"
            onClick={handleCreateArea}
            disabled={!areaFormData.name.trim() || formLoading}
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

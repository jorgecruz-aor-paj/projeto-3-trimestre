import React, { useState, useEffect } from 'react';
import {
  Container,
  Row,
  Col,
  Card,
  Badge,
  Spinner,
  Alert,
  Button,
  Form,
  InputGroup,
} from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { trainingService } from 'services/trainingService';

/**
 * User Training View component
 * Allows regular users to view and browse available trainings
 * @returns {React.Component} User training view interface
 */
export const UserTrainingView = () => {
  const { t } = useTranslation();

  // State management
  const [trainings, setTrainings] = useState([]);
  const [trainingAreas, setTrainingAreas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Filter states
  const [searchText, setSearchText] = useState('');
  const [selectedArea, setSelectedArea] = useState('');
  const [selectedLanguage, setSelectedLanguage] = useState('');

  const fetchTrainings = async () => {
    try {
      setLoading(true);
      setError(null);

      const filters = {
        activeOnly: true, // Only show active trainings to users
      };

      if (searchText) filters.searchText = searchText;
      if (selectedArea) filters.trainingAreaId = selectedArea;
      if (selectedLanguage) filters.language = selectedLanguage;

      const data = await trainingService.getAll(filters);
      setTrainings(data);
    } catch (err) {
      setError(err.message || t('trainings.fetchError'));
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
  }, []);

  // Refetch when filters change
  useEffect(() => {
    const delayedFetch = setTimeout(() => {
      fetchTrainings();
    }, 750); // Debounce search

    return () => clearTimeout(delayedFetch);
  }, [searchText, selectedArea, selectedLanguage]);

  const formatDuration = minutes => {
    if (minutes < 60) return `${minutes}m`;
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return mins > 0 ? `${hours}h ${mins}m` : `${hours}h`;
  };

  const getLanguageBadgeVariant = language => {
    const variants = {
      ENGLISH: 'primary',
      PORTUGUESE: 'success',
      SPANISH: 'info',
      FRENCH: 'warning',
      GERMAN: 'secondary',
    };
    return variants[language] || 'outline-dark';
  };

  const commonLanguages = [
    'ENGLISH',
    'PORTUGUESE',
    'SPANISH',
    'FRENCH',
    'GERMAN',
    'ITALIAN',
    'DUTCH',
  ];

  const clearFilters = () => {
    setSearchText('');
    setSelectedArea('');
    setSelectedLanguage('');
  };

  const hasActiveFilters = searchText || selectedArea || selectedLanguage;

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('trainings.loading')}</p>
      </div>
    );
  }

  return (
    <Container fluid className="py-4">
      {/* Header */}
      <Row className="mb-4">
        <Col>
          <div className="text-center">
            <h2>
              {t('trainings.availableTrainings', 'Available Training Programs')}
            </h2>
            <p className="text-muted">
              {t(
                'trainings.user.subtitle',
                'Discover and explore professional development opportunities'
              )}
            </p>
          </div>
        </Col>
      </Row>

      {/* Filters */}
      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Body>
              <Row className="g-3">
                <Col md={6} lg={4}>
                  <Form.Group>
                    <Form.Label className="small fw-semibold">
                      {t('common.search')}
                    </Form.Label>
                    <InputGroup>
                      <InputGroup.Text>
                        <i className="bi bi-search"></i>
                      </InputGroup.Text>
                      <Form.Control
                        type="text"
                        placeholder={t('trainings.filter.searchPlaceholder')}
                        value={searchText}
                        onChange={e => setSearchText(e.target.value)}
                      />
                    </InputGroup>
                  </Form.Group>
                </Col>

                <Col md={6} lg={3}>
                  <Form.Group>
                    <Form.Label className="small fw-semibold">
                      {t('trainings.fields.area')}
                    </Form.Label>
                    <Form.Select
                      value={selectedArea}
                      onChange={e => setSelectedArea(e.target.value)}
                    >
                      <option value="">{t('common.all')}</option>
                      {trainingAreas.map(area => (
                        <option key={area.id} value={area.id}>
                          {area.name}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>

                <Col md={6} lg={3}>
                  <Form.Group>
                    <Form.Label className="small fw-semibold">
                      {t('trainings.fields.language')}
                    </Form.Label>
                    <Form.Select
                      value={selectedLanguage}
                      onChange={e => setSelectedLanguage(e.target.value)}
                    >
                      <option value="">{t('common.all')}</option>
                      {commonLanguages.map(lang => (
                        <option key={lang} value={lang}>
                          {t(`languages.${lang}`)}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>

                <Col md={6} lg={2} className="d-flex align-items-end">
                  {hasActiveFilters && (
                    <Button
                      variant="outline-secondary"
                      onClick={clearFilters}
                      className="w-100"
                    >
                      <i className="bi bi-x-circle me-1"></i>
                      {t('common.clearFilters')}
                    </Button>
                  )}
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Error Alert */}
      {error && (
        <Row className="mb-4">
          <Col>
            <Alert variant="danger">
              <Alert.Heading>{t('common.error')}</Alert.Heading>
              <p>{error}</p>
              <Button variant="outline-danger" onClick={fetchTrainings}>
                {t('common.retry')}
              </Button>
            </Alert>
          </Col>
        </Row>
      )}

      {/* Training Cards */}
      <Row>
        {trainings.length === 0 ? (
          <Col>
            <div className="text-center py-5">
              <i className="bi bi-book display-4 text-muted"></i>
              <h5 className="mt-3 text-muted">
                {hasActiveFilters
                  ? t(
                      'trainings.user.noMatchingTrainings',
                      'No trainings match your criteria'
                    )
                  : t('trainings.noTrainings', 'No trainings available')}
              </h5>
              <p className="text-muted">
                {hasActiveFilters
                  ? t(
                      'trainings.user.tryDifferentFilters',
                      'Try adjusting your filters'
                    )
                  : t(
                      'trainings.user.checkBackLater',
                      'Check back later for new training programs'
                    )}
              </p>
              {hasActiveFilters && (
                <Button variant="primary" onClick={clearFilters}>
                  {t('common.clearFilters')}
                </Button>
              )}
            </div>
          </Col>
        ) : (
          trainings.map(training => (
            <Col key={training.id} md={6} lg={4} className="mb-4">
              <Card className="h-100 shadow-sm training-card">
                <Card.Body className="d-flex flex-column">
                  <div className="mb-3">
                    <div className="d-flex justify-content-between align-items-start mb-2">
                      <h5 className="card-title mb-0">{training.title}</h5>
                      <Badge bg={getLanguageBadgeVariant(training.language)}>
                        {t(`languages.${training.language}`)}
                      </Badge>
                    </div>

                    <div className="d-flex gap-2 mb-2">
                      <Badge bg="light" text="dark">
                        <i className="bi bi-folder me-1"></i>
                        {training.trainingArea?.name}
                      </Badge>
                      <Badge bg="light" text="dark">
                        <i className="bi bi-clock me-1"></i>
                        {formatDuration(training.duration)}
                      </Badge>
                    </div>
                  </div>

                  <Card.Text className="flex-grow-1 text-muted">
                    {training.description}
                  </Card.Text>

                  {training.link && (
                    <div className="mt-auto">
                      <Button
                        variant="primary"
                        as="a"
                        href={training.link}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="w-100"
                      >
                        <i className="bi bi-arrow-up-right-square me-2"></i>
                        {t('trainings.user.accessTraining', 'Access Training')}
                      </Button>
                    </div>
                  )}
                </Card.Body>

                <Card.Footer className="text-muted small">
                  <i className="bi bi-calendar3 me-1"></i>
                  {t('trainings.user.addedOn', 'Added')}{' '}
                  {new Date(training.createdAt).toLocaleDateString()}
                </Card.Footer>
              </Card>
            </Col>
          ))
        )}
      </Row>

      {/* Results Summary */}
      {trainings.length > 0 && (
        <Row className="mt-4">
          <Col>
            <div className="text-center text-muted">
              <small>
                {t(
                  'trainings.user.showingResults',
                  'Showing {{count}} training(s)',
                  {
                    count: trainings.length,
                  }
                )}
                {hasActiveFilters && (
                  <span>
                    {' '}
                    •{' '}
                    <Button
                      variant="link"
                      size="sm"
                      className="p-0"
                      onClick={clearFilters}
                    >
                      {t('common.clearFilters')}
                    </Button>
                  </span>
                )}
              </small>
            </div>
          </Col>
        </Row>
      )}
    </Container>
  );
};

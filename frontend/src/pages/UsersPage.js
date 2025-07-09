import React, { useEffect, useState } from 'react';
import {
  Container,
  Row,
  Col,
  Spinner,
  Alert,
  Card,
  Button,
  Badge,
} from 'react-bootstrap';
import { useUserStore } from 'stores/UserStore';
import { UserCard } from 'components/users/UserCard';
import { UserFilters } from 'components/users/UserFilters';
import { useTranslation } from 'react-i18next';

export const UsersPage = () => {
  const { t } = useTranslation();
  const [filters, setFilters] = useState({});
  const users = useUserStore(state => state.users) || [];
  const loading = useUserStore(state => state.loading);
  const error = useUserStore(state => state.error);
  const fetchUsers = useUserStore(state => state.fetchUsers);

  useEffect(() => {
    fetchUsers(filters);
  }, [fetchUsers, JSON.stringify(filters)]);

  return (
    <Container fluid className="py-4">
      {/* Header section */}
      <Row className="mb-4">
        <Col>
          <div className="page-header">
            <h2 className="mb-2">{t('usersPage.title')}</h2>
            <p className="text-muted mb-0">
              {t('usersPage.subtitle', 'Manage and view user profiles')}
            </p>
          </div>
        </Col>
      </Row>

      {/* Filters section */}
      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Body>
              <UserFilters filters={filters} setFilters={setFilters} />
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
                onClick={() => fetchUsers(filters)}
              >
                {t('common.retry', 'Retry')}
              </Button>
            </Alert>
          </Col>
        </Row>
      )}

      {/* Users grid */}
      <Row>
        <Col>
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <div className="d-flex align-items-center">
                <h5 className="mb-0 me-3">{t('usersPage.users', 'Users')}</h5>
                <Badge bg="secondary">
                  {users.length} {t('usersPage.usersCount', 'users')}
                </Badge>
              </div>
            </Card.Header>
            <Card.Body>
              {loading && (
                <div className="text-center py-5">
                  <Spinner
                    animation="border"
                    variant="primary"
                    className="mb-3"
                  />
                  <p>{t('usersPage.loading', 'Loading users...')}</p>
                </div>
              )}

              {users.length === 0 && !loading && !error ? (
                <div className="text-center py-5">
                  <i className="bi bi-people display-4 text-muted"></i>
                  <h5 className="mt-3 text-muted">
                    {t('usersPage.noResults', 'No users found')}
                  </h5>
                  <p className="text-muted">
                    {t('usersPage.noResultsDesc', 'Try adjusting your filters')}
                  </p>
                </div>
              ) : (
                <Row xs={1} md={2} lg={3} className="g-4">
                  {users.map(user => (
                    <Col key={user.id}>
                      <UserCard user={user} />
                    </Col>
                  ))}
                </Row>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

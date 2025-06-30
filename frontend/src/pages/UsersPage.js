import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
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

  const handleFilterChange = e => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: type === 'checkbox' || type === 'switch' ? checked : value,
    }));
  };

  return (
    <Container className="py-4">
      <h2 className="mb-4">{t('usersPage.title')}</h2>
      <UserFilters filters={filters} onChange={handleFilterChange} />
      {loading && <Spinner animation="border" />}
      {error && <Alert variant="danger">{t('usersPage.error')}</Alert>}
      <Row>
        {users.length === 0 && !loading && !error && (
          <Col xs={12} className="text-center text-muted py-4">
            {t('usersPage.noResults')}
          </Col>
        )}
        {users.map(user => (
          <Col key={user.id} md={6} lg={4}>
            <UserCard user={user} />
          </Col>
        ))}
      </Row>
    </Container>
  );
};

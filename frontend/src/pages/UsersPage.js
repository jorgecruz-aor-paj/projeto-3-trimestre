import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { useUserStore } from 'stores/UserStore';
import { UserCard } from 'components/users/UserCard';
import { UserFilters } from 'components/users/UserFilters';

export const UsersPage = () => {
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
      <h2 className="mb-4">Users</h2>
      <UserFilters filters={filters} onChange={handleFilterChange} />
      {loading && <Spinner animation="border" />}
      {error && <Alert variant="danger">{error}</Alert>}
      {!loading && users.length === 0 && (
        <Alert variant="info">No users found with the selected filters.</Alert>
      )}
      <Row>
        {users.map(user => (
          <Col key={user.id} md={6} lg={4}>
            <UserCard user={user} />
          </Col>
        ))}
      </Row>
    </Container>
  );
};


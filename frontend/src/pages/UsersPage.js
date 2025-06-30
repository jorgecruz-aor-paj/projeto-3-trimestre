import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { useUserStore } from 'stores/UserStore';
import { UserCard } from 'components/users/UserCard';
import { UserFilters } from 'components/users/UserFilters';

export const UsersPage = () => {
  const [filters, setFilters] = useState({});
  const { users, loading, error, fetchUsers } = useUserStore();

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleFilterChange = e => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const filteredUsers = users.filter(user => {
    const nameMatch =
      !filters.name ||
      `${user.firstName} ${user.lastName}`
        .toLowerCase()
        .includes(filters.name.toLowerCase());
    const emailMatch =
      !filters.email ||
      user.email.toLowerCase().includes(filters.email.toLowerCase());
    const roleMatch = !filters.role || user.role === filters.role;
    return nameMatch && emailMatch && roleMatch;
  });

  return (
    <Container className="py-4">
      <h2 className="mb-4">Users</h2>
      <UserFilters filters={filters} onChange={handleFilterChange} />
      {loading && <Spinner animation="border" />}
      {error && <Alert variant="danger">{error}</Alert>}
      <Row>
        {filteredUsers.map(user => (
          <Col key={user.id} md={6} lg={4}>
            <UserCard user={user} />
          </Col>
        ))}
      </Row>
    </Container>
  );
};

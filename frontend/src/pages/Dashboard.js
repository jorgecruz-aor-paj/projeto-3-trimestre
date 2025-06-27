import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Container, Row, Col, Alert, Spinner } from 'react-bootstrap';
import { useAuthStore } from 'stores/AuthStore';
import { userService } from 'services/userService';
import { DashboardHeader } from 'components/dashboard/DashboardHeader';
import { AdminDashboard } from 'components/dashboard/AdminDashboard';
import { ManagerDashboard } from 'components/dashboard/ManagerDashboard';
import { UserDashboard } from 'components/dashboard/UserDashboard';
import { WelcomeCard } from 'components/dashboard/WelcomeCard';
import 'styles/pages/Dashboard.css';

/**
 * Dashboard page component that renders different content based on user role
 * @returns {React.Component} Dashboard page
 */
export const Dashboard = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { token, user, setUserInfo } = useAuthStore();

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    // Check if user is authenticated
    if (!token) {
      navigate('/login');
      return;
    }

    // Fetch user info if not available
    const fetchUserInfo = async () => {
      try {
        if (!user?.id || !user?.role) {
          const userResponse = await userService.getMe();
          setUserInfo(userResponse);
          setCurrentUser(userResponse);
        } else {
          setCurrentUser(user);
        }
      } catch (err) {
        console.error('Failed to fetch user info:', err);
        setError(t('dashboard.failedToLoadUser'));
        // If unauthorized, redirect to login
        if (err.code === 'UNAUTHORIZED') {
          navigate('/login');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, [token, user, setUserInfo, navigate, t]);

  // Render loading state
  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" variant="primary" className="mb-3" />
        <p>{t('dashboard.loading')}</p>
      </Container>
    );
  }

  // Render error state
  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          <Alert.Heading>{t('dashboard.errorTitle')}</Alert.Heading>
          <p>{error}</p>
        </Alert>
      </Container>
    );
  }

  // Render dashboard based on user role
  const renderDashboardContent = () => {
    if (!currentUser?.role) {
      return (
        <Alert variant="warning">
          <p>{t('dashboard.noRoleAssigned')}</p>
        </Alert>
      );
    }

    switch (currentUser.role.toUpperCase()) {
      case 'ADMIN':
        return <AdminDashboard user={currentUser} />;
      case 'MANAGER':
        return <ManagerDashboard user={currentUser} />;
      case 'USER':
        return <UserDashboard user={currentUser} />;
      default:
        return (
          <Alert variant="warning">
            <p>{t('dashboard.unknownRole')}</p>
          </Alert>
        );
    }
  };

  return (
    <div className="dashboard-page">
      <Container fluid className="py-4">
        {/* Dashboard Header */}
        <DashboardHeader user={currentUser} />

        {/* Welcome Card */}
        <Row className="mb-4">
          <Col>
            <WelcomeCard user={currentUser} />
          </Col>
        </Row>

        {/* Role-based Dashboard Content */}
        <Row>
          <Col>{renderDashboardContent()}</Col>
        </Row>
      </Container>
    </div>
  );
};

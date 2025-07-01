import React from 'react';
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import {
  Container,
  Row,
  Col,
  Card,
  ButtonGroup,
  Button,
} from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { LoginAuthForm } from '../components/auth/LoginAuthForm';
import { RegisterAuthForm } from '../components/auth/RegisterAuthForm';
import 'styles/pages/auth/LoginPage.css';

export const AuthTogglePage = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const location = useLocation();

  // Determine which form is active based on the route
  const isLogin = location.pathname === '/login';
  const isRegister = location.pathname === '/register';

  const handleToggle = form => {
    if (form === 'login') navigate('/login');
    else navigate('/register');
  };

  return (
    <Container fluid="md" className="py-5 login-page">
      <Row className="justify-content-center">
        <Col xs={12} sm={10} md={8} lg={6} xl={5}>
          <Card className="shadow-sm border-0">
            <Card.Body className="p-4 p-md-5">
              <ButtonGroup className="w-100 mb-4">
                <Button
                  variant={isLogin ? 'primary' : 'outline-primary'}
                  onClick={() => handleToggle('login')}
                >
                  {t('auth.login')}
                </Button>
                <Button
                  variant={isRegister ? 'primary' : 'outline-primary'}
                  onClick={() => handleToggle('register')}
                >
                  {t('auth.register')}
                </Button>
              </ButtonGroup>
              {isLogin && <LoginAuthForm />}
              {isRegister && <RegisterAuthForm />}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

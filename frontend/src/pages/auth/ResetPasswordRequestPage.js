import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Form, Alert, Card, Container, Row, Col } from 'react-bootstrap';
import { FormInput } from 'components/forms/FormInput';
import { SubmitButton } from 'components/forms/SubmitButton';
import { authService } from 'services/authService';

export const ResetPasswordRequestPage = () => {
  const { t } = useTranslation();
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [validationError, setValidationError] = useState(null);

  const handleChange = e => {
    setEmail(e.target.value);
    setValidationError(null);
    setError(null);
  };

  const validate = () => {
    if (!email) return t('validation.emailRequired');
    if (!/\S+@\S+\.\S+/.test(email)) return t('validation.emailInvalid');
    return null;
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    const err = validate();
    if (err) {
      setValidationError(err);
      return;
    }
    setLoading(true);
    try {
      await authService.requestPasswordReset( email );
      setSuccess(t('auth.resetPasswordEmailSent'));
    } catch (err) {
      setError(err.message || t('auth.resetPasswordFailed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container fluid="md" className="py-5 login-page">
      <Row className="justify-content-center">
        <Col xs={12} sm={10} md={8} lg={6} xl={5}>
          <Card className="shadow-sm border-0">
            <Card.Body className="p-4 p-md-5">
              <h2 className="fw-bold mb-3 text-center">
                {t('auth.resetPasswordTitle')}
              </h2>
              <p className="text-muted text-center mb-4">
                {t('auth.resetPasswordSubtitle')}
              </p>
              {success && <Alert variant="success">{success}</Alert>}
              {error && <Alert variant="danger">{error}</Alert>}
              <Form onSubmit={handleSubmit}>
                <FormInput
                  label={t('auth.email')}
                  type="email"
                  name="email"
                  value={email}
                  onChange={handleChange}
                  placeholder={t('auth.emailPlaceholder')}
                  error={validationError}
                  disabled={loading}
                  autoFocus
                />
                <SubmitButton
                  isLoading={loading}
                  label={t('auth.sendResetLink')}
                  className="w-100 py-2 mt-3 mb-4"
                />
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

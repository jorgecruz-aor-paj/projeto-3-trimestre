import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Modal, Form, Button, Alert, Spinner, Row, Col } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

/**
 * Two-Factor Authentication Verification Modal
 * Used during login to verify 2FA codes
 */
export const TwoFactorVerificationModal = ({
  show,
  onHide,
  onVerify,
  loading = false,
  error = null,
}) => {
  const { t } = useTranslation();
  const [code, setCode] = useState('');
  const [isBackupCode, setIsBackupCode] = useState(false);

  const handleSubmit = e => {
    e.preventDefault();
    if (code.trim()) {
      onVerify(code.trim());
    }
  };

  const handleCodeChange = e => {
    const value = e.target.value;

    // For TOTP codes, only allow digits and limit to 6 characters
    // For backup codes, allow alphanumeric and limit to 10 characters
    if (isBackupCode) {
      // Backup codes are typically 8-10 characters, alphanumeric
      setCode(value.replace(/[^a-zA-Z0-9]/g, '').substring(0, 10));
    } else {
      // TOTP codes are 6 digits
      setCode(value.replace(/\D/g, '').substring(0, 6));
    }
  };

  const resetForm = () => {
    setCode('');
    setIsBackupCode(false);
  };

  const handleClose = () => {
    resetForm();
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose} centered backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>{t('auth.2fa.verificationTitle')}</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <div className="text-center mb-4">
            <i
              className="bi bi-shield-check text-primary"
              style={{ fontSize: '3rem' }}
            ></i>
            <h5 className="mt-3 mb-2">{t('auth.2fa.verificationRequired')}</h5>
            <p className="text-muted">
              {isBackupCode
                ? t('auth.2fa.enterBackupCode')
                : t('auth.2fa.enterTotpCode')}
            </p>
          </div>

          {error && (
            <Alert variant="danger" className="mb-3">
              {error}
            </Alert>
          )}

          <Form.Group className="mb-3">
            <Form.Label>
              {isBackupCode
                ? t('auth.2fa.backupCode')
                : t('auth.2fa.authenticatorCode')}
            </Form.Label>
            <Form.Control
              type="text"
              value={code}
              onChange={handleCodeChange}
              placeholder={
                isBackupCode ? t('auth.2fa.backupCodePlaceholder') : '123456'
              }
              className="text-center"
              style={{
                fontSize: '1.2rem',
                letterSpacing: isBackupCode ? '0.1rem' : '0.2rem',
              }}
              maxLength={isBackupCode ? 10 : 6}
              disabled={loading}
              autoFocus
            />
            <Form.Text className="text-muted">
              {isBackupCode
                ? t('auth.2fa.backupCodeHelp')
                : t('auth.2fa.totpCodeHelp')}
            </Form.Text>
          </Form.Group>

          <Row>
            <Col>
              <Button
                variant="link"
                size="sm"
                onClick={() => {
                  setIsBackupCode(!isBackupCode);
                  setCode('');
                }}
                className="p-0"
                disabled={loading}
              >
                {isBackupCode
                  ? t('auth.2fa.useAuthenticatorCode')
                  : t('auth.2fa.useBackupCode')}
              </Button>
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} disabled={loading}>
            {t('common.cancel')}
          </Button>
          <Button
            type="submit"
            variant="primary"
            disabled={
              loading || !code.trim() || (!isBackupCode && code.length !== 6)
            }
          >
            {loading && (
              <Spinner
                as="span"
                animation="border"
                size="sm"
                className="me-2"
              />
            )}
            {t('auth.2fa.verify')}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

TwoFactorVerificationModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  onVerify: PropTypes.func.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string,
};

import React, { useState, useEffect } from 'react';
import {
  Card,
  Row,
  Col,
  Button,
  Alert,
  Modal,
  Form,
  Badge,
  Spinner,
} from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { twoFactorService } from 'services';
import { useAlert } from 'contexts/AlertContext';
import { useErrorMessage } from 'hooks/useErrorMessage';
import QRCode from 'react-qr-code';

/**
 * Two-Factor Authentication Security Settings Component
 * Allows users to enable/disable 2FA, view backup codes, and manage 2FA settings
 */
export const TwoFactorSecuritySettings = () => {
  const { t } = useTranslation();
  const { showAlert } = useAlert();
  const { getErrorMessage } = useErrorMessage();

  // State management
  const [twoFactorEnabled, setTwoFactorEnabled] = useState(false);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);

  // Setup modal states
  const [showSetupModal, setShowSetupModal] = useState(false);
  const [setupData, setSetupData] = useState(null);
  const [verificationCode, setVerificationCode] = useState('');
  const [setupStep, setSetupStep] = useState('qr'); // 'qr' | 'verify' | 'backup'

  // Disable modal states
  const [showDisableModal, setShowDisableModal] = useState(false);
  const [disablePassword, setDisablePassword] = useState('');

  // Backup codes modal
  const [showBackupCodesModal, setShowBackupCodesModal] = useState(false);
  const [backupCodes, setBackupCodes] = useState([]);

  /**
   * Load the current 2FA status for the user
   */
  const loadTwoFactorStatus = async () => {
    try {
      setLoading(true);
      const response = await twoFactorService.getStatus();
      setTwoFactorEnabled(response.enabled || false);
    } catch (error) {
      console.error('Failed to load 2FA status:', error);
      showAlert({
        title: t('security.2fa.error'),
        message: getErrorMessage(error.message || 'Failed to load 2FA status'),
        variant: 'danger',
      });
    } finally {
      setLoading(false);
    }
  };

  // Load 2FA status on component mount
  useEffect(() => {
    loadTwoFactorStatus();
  }, []);

  /**
   * Start the 2FA setup process
   */
  const handleSetupStart = async () => {
    try {
      setActionLoading(true);
      const response = await twoFactorService.setup();
      setSetupData(response);
      setSetupStep('qr');
      setShowSetupModal(true);
    } catch (error) {
      console.error('Failed to start 2FA setup:', error);
      showAlert({
        title: t('security.2fa.setupError'),
        message: getErrorMessage(error.message || 'Failed to start 2FA setup'),
        variant: 'danger',
      });
    } finally {
      setActionLoading(false);
    }
  };

  /**
   * Verify the TOTP code and complete 2FA setup
   */
  const handleSetupVerify = async () => {
    if (!verificationCode || verificationCode.length !== 6) {
      showAlert({
        title: t('security.2fa.verificationError'),
        message: t('security.2fa.invalidCode'),
        variant: 'warning',
      });
      return;
    }

    try {
      setActionLoading(true);
      await twoFactorService.verifyAndEnable(verificationCode);

      setSetupStep('backup');
      setBackupCodes(setupData.backupCodes || []);
      setTwoFactorEnabled(true);

      showAlert({
        title: t('security.2fa.enabledTitle'),
        message: t('security.2fa.enabledMessage'),
        variant: 'success',
      });
    } catch (error) {
      console.error('Failed to verify 2FA code:', error);
      showAlert({
        title: t('security.2fa.verificationError'),
        message: getErrorMessage(error.message || 'Invalid verification code'),
        variant: 'danger',
      });
    } finally {
      setActionLoading(false);
    }
  };

  /**
   * Disable 2FA with password confirmation
   */
  const handleDisable = async () => {
    if (!disablePassword) {
      showAlert({
        title: t('security.2fa.disableError'),
        message: t('security.2fa.passwordRequired'),
        variant: 'warning',
      });
      return;
    }

    try {
      setActionLoading(true);
      await twoFactorService.disable(disablePassword);

      setTwoFactorEnabled(false);
      setShowDisableModal(false);
      setDisablePassword('');

      showAlert({
        title: t('security.2fa.disabledTitle'),
        message: t('security.2fa.disabledMessage'),
        variant: 'success',
      });
    } catch (error) {
      console.error('Failed to disable 2FA:', error);
      showAlert({
        title: t('security.2fa.disableError'),
        message: getErrorMessage(error.message || 'Failed to disable 2FA'),
        variant: 'danger',
      });
    } finally {
      setActionLoading(false);
    }
  };

  /**
   * Regenerate backup codes
   */
  const handleRegenerateBackupCodes = async () => {
    try {
      setActionLoading(true);
      const response = await twoFactorService.regenerateBackupCodes();
      setBackupCodes(response.backupCodes || []);
      setShowBackupCodesModal(true);

      showAlert({
        title: t('security.2fa.backupCodesRegenerated'),
        message: t('security.2fa.backupCodesRegeneratedMessage'),
        variant: 'success',
      });
    } catch (error) {
      console.error('Failed to regenerate backup codes:', error);
      showAlert({
        title: t('security.2fa.backupCodesError'),
        message: getErrorMessage(
          error.message || 'Failed to regenerate backup codes'
        ),
        variant: 'danger',
      });
    } finally {
      setActionLoading(false);
    }
  };

  /**
   * Close setup modal and reset state
   */
  const closeSetupModal = () => {
    setShowSetupModal(false);
    setSetupData(null);
    setVerificationCode('');
    setSetupStep('qr');
  };

  /**
   * Close disable modal and reset state
   */
  const closeDisableModal = () => {
    setShowDisableModal(false);
    setDisablePassword('');
  };

  if (loading) {
    return (
      <Card className="h-100">
        <Card.Header>
          <h5 className="mb-0">{t('security.2fa.title')}</h5>
        </Card.Header>
        <Card.Body className="d-flex align-items-center justify-content-center">
          <Spinner animation="border" size="sm" />
        </Card.Body>
      </Card>
    );
  }

  return (
    <>
      <Card className="h-100">
        <Card.Header className="d-flex align-items-center justify-content-between">
          <h5 className="mb-0">{t('security.2fa.title')}</h5>
          <Badge bg={twoFactorEnabled ? 'success' : 'secondary'}>
            {twoFactorEnabled
              ? t('security.2fa.enabled')
              : t('security.2fa.disabled')}
          </Badge>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col>
              <p className="text-muted mb-3">{t('security.2fa.description')}</p>

              {!twoFactorEnabled ? (
                <Alert variant="info" className="mb-3">
                  <Alert.Heading as="h6">
                    {t('security.2fa.recommendationTitle')}
                  </Alert.Heading>
                  <p className="mb-0">
                    {t('security.2fa.recommendationMessage')}
                  </p>
                </Alert>
              ) : (
                <Alert variant="success" className="mb-3">
                  <Alert.Heading as="h6">
                    {t('security.2fa.protectedTitle')}
                  </Alert.Heading>
                  <p className="mb-0">{t('security.2fa.protectedMessage')}</p>
                </Alert>
              )}

              <div className="d-grid gap-2 d-md-flex">
                {!twoFactorEnabled ? (
                  <Button
                    variant="primary"
                    onClick={handleSetupStart}
                    disabled={actionLoading}
                  >
                    {actionLoading && (
                      <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        className="me-2"
                      />
                    )}
                    {t('security.2fa.enable')}
                  </Button>
                ) : (
                  <>
                    <Button
                      variant="outline-primary"
                      onClick={handleRegenerateBackupCodes}
                      disabled={actionLoading}
                    >
                      {actionLoading && (
                        <Spinner
                          as="span"
                          animation="border"
                          size="sm"
                          className="me-2"
                        />
                      )}
                      {t('security.2fa.regenerateBackupCodes')}
                    </Button>
                    <Button
                      variant="outline-danger"
                      onClick={() => setShowDisableModal(true)}
                      disabled={actionLoading}
                    >
                      {t('security.2fa.disable')}
                    </Button>
                  </>
                )}
              </div>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {/* Setup Modal */}
      <Modal show={showSetupModal} onHide={closeSetupModal} size="lg" centered>
        <Modal.Header closeButton>
          <Modal.Title>{t('security.2fa.setupTitle')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {setupStep === 'qr' && setupData && (
            <div className="text-center">
              <h6 className="mb-3">{t('security.2fa.scanQR')}</h6>
              <div className="mb-3 d-flex justify-content-center">
                <QRCode value={setupData.qrCodeUrl} size={200} />
              </div>
              <Alert variant="info">
                <small>
                  <strong>{t('security.2fa.secretKey')}:</strong>
                  <br />
                  <code className="text-break">{setupData.secretKey}</code>
                </small>
              </Alert>
              <p className="text-muted small">
                {t('security.2fa.setupInstructions')}
              </p>
            </div>
          )}

          {setupStep === 'verify' && (
            <div>
              <h6 className="mb-3">{t('security.2fa.enterCode')}</h6>
              <Form
                onSubmit={e => {
                  e.preventDefault();
                  if (
                    verificationCode &&
                    verificationCode.length === 6 &&
                    !actionLoading
                  ) {
                    handleSetupVerify();
                  }
                }}
              >
                <Form.Group className="mb-3">
                  <Form.Label>{t('security.2fa.verificationCode')}</Form.Label>
                  <Form.Control
                    type="text"
                    maxLength={6}
                    value={verificationCode}
                    onChange={e =>
                      setVerificationCode(e.target.value.replace(/\D/g, ''))
                    }
                    placeholder="123456"
                    className="text-center"
                    style={{ fontSize: '1.2rem', letterSpacing: '0.2rem' }}
                  />
                  <Form.Text className="text-muted">
                    {t('security.2fa.codeInstructions')}
                  </Form.Text>
                </Form.Group>
              </Form>
            </div>
          )}

          {setupStep === 'backup' && (
            <div>
              <Alert variant="warning">
                <Alert.Heading as="h6">
                  {t('security.2fa.backupCodesTitle')}
                </Alert.Heading>
                <p>{t('security.2fa.backupCodesMessage')}</p>
              </Alert>
              <div className="row">
                {backupCodes.map((code, index) => (
                  <div key={index} className="col-6 mb-2">
                    <code className="d-block text-center p-2 bg-light rounded">
                      {code}
                    </code>
                  </div>
                ))}
              </div>
            </div>
          )}
        </Modal.Body>
        <Modal.Footer>
          {setupStep === 'qr' && (
            <>
              <Button variant="secondary" onClick={closeSetupModal}>
                {t('common.cancel')}
              </Button>
              <Button variant="primary" onClick={() => setSetupStep('verify')}>
                {t('security.2fa.continue')}
              </Button>
            </>
          )}
          {setupStep === 'verify' && (
            <>
              <Button variant="secondary" onClick={() => setSetupStep('qr')}>
                {t('common.back')}
              </Button>
              <Button
                variant="primary"
                onClick={handleSetupVerify}
                disabled={
                  actionLoading ||
                  !verificationCode ||
                  verificationCode.length !== 6
                }
              >
                {actionLoading && (
                  <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    className="me-2"
                  />
                )}
                {t('security.2fa.verify')}
              </Button>
            </>
          )}
          {setupStep === 'backup' && (
            <Button variant="primary" onClick={closeSetupModal}>
              {t('security.2fa.finish')}
            </Button>
          )}
        </Modal.Footer>
      </Modal>

      {/* Disable Modal */}
      <Modal show={showDisableModal} onHide={closeDisableModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>{t('security.2fa.disableTitle')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert variant="warning">{t('security.2fa.disableWarning')}</Alert>
          <Form>
            <Form.Group>
              <Form.Label>{t('security.2fa.confirmPassword')}</Form.Label>
              <Form.Control
                type="password"
                value={disablePassword}
                onChange={e => setDisablePassword(e.target.value)}
                placeholder={t('security.2fa.enterPassword')}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={closeDisableModal}>
            {t('common.cancel')}
          </Button>
          <Button
            variant="danger"
            onClick={handleDisable}
            disabled={actionLoading || !disablePassword}
          >
            {actionLoading && (
              <Spinner
                as="span"
                animation="border"
                size="sm"
                className="me-2"
              />
            )}
            {t('security.2fa.disable')}
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Backup Codes Modal */}
      <Modal
        show={showBackupCodesModal}
        onHide={() => setShowBackupCodesModal(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>{t('security.2fa.backupCodesTitle')}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert variant="info">
            {t('security.2fa.newBackupCodesMessage')}
          </Alert>
          <div className="row">
            {backupCodes.map((code, index) => (
              <div key={index} className="col-6 mb-2">
                <code className="d-block text-center p-2 bg-light rounded">
                  {code}
                </code>
              </div>
            ))}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="primary"
            onClick={() => setShowBackupCodesModal(false)}
          >
            {t('common.close')}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

// No propTypes needed - component manages its own state

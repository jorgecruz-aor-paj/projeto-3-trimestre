import React, { createContext, useContext, useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { useTranslate } from 'react-i18next';

const AlertContext = createContext();

export function AlertProvider({ children }) {
  const { t } = useTranslate();
  const [isOpen, setIsOpen] = useState(false);
  const [alertConfig, setAlertConfig] = useState({
    title: '',
    message: '',
    confirmLabel: 'OK',
    onConfirm: () => {},
    variant: 'primary',
    showCancel: false,
    cancelLabel: 'Cancel',
    onCancel: () => {},
    cancelVariant: 'secondary',
  });

  /**
   * Shows an alert or confirmation dialog with customizable options
   * @param {Object} config - Configuration object for the alert
   * @param {string} config.title - Title of the alert modal
   * @param {string} config.message - Message to be displayed in the alert modal
   * @param {string} config.confirmLabel - Label for the confirm button
   * @param {function} config.onConfirm - Callback function to be executed on confirm button click
   * @param {string} config.variant - Bootstrap variant for the confirm button
   * @param {boolean} config.showCancel - Whether to show the cancel button (for confirmations)
   * @param {string} config.cancelLabel - Label for the cancel button
   * @param {function} config.onCancel - Callback function to be executed on cancel button click
   * @param {string} config.cancelVariant - Bootstrap variant for the cancel button
   * @returns {void}
   */
  const showAlert = config => {
    setAlertConfig({
      title: config.title || t('common.alert', 'Alerta'),
      message: config.message || '',
      confirmLabel: config.confirmLabel || t('common.ok', 'OK'),
      onConfirm: config.onConfirm || (() => {}),
      variant: config.variant || 'primary',
      showCancel: config.showCancel || false,
      cancelLabel: config.cancelLabel || t('common.cancel', 'Cancelar'),
      onCancel: config.onCancel || (() => {}),
      cancelVariant: config.cancelVariant || 'secondary',
    });
    setIsOpen(true);
  };

  /**
   * Convenience method for showing a confirmation dialog with Yes/No buttons
   * @param {Object} config - Configuration object similar to showAlert
   * @returns {void}
   */
  const showConfirm = config => {
    showAlert({
      ...config,
      confirmLabel: config.confirmLabel || t('common.yes', 'Sim'),
      variant: config.variant || 'primary',
      showCancel: true,
      cancelLabel: config.cancelLabel || t('common.no', 'Não'),
    });
  };

  const hideAlert = () => {
    setIsOpen(false);
  };

  const handleConfirm = () => {
    hideAlert();
    if (alertConfig.onConfirm) {
      alertConfig.onConfirm();
    }
  };

  const handleCancel = () => {
    hideAlert();
    if (alertConfig.onCancel) {
      alertConfig.onCancel();
    }
  };

  return (
    <AlertContext.Provider value={{ showAlert, showConfirm }}>
      {children}

      <Modal
        show={isOpen}
        onHide={handleCancel}
        backdrop="static"
        keyboard={false}
        centered
      >
        <Modal.Header>
          <Modal.Title>{alertConfig.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>{alertConfig.message}</Modal.Body>
        <Modal.Footer>
          {alertConfig.showCancel && (
            <Button variant={alertConfig.cancelVariant} onClick={handleCancel}>
              {alertConfig.cancelLabel}
            </Button>
          )}
          <Button variant={alertConfig.variant} onClick={handleConfirm}>
            {alertConfig.confirmLabel}
          </Button>
        </Modal.Footer>
      </Modal>
    </AlertContext.Provider>
  );
}

export const useAlert = () => useContext(AlertContext);

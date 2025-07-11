import React from 'react';
import {
  render,
  screen,
  fireEvent,
  waitFor,
  act,
} from '@testing-library/react';
import { AlertProvider, useAlert } from 'contexts/AlertContext';

// Mocks
jest.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key, fallback) => fallback || key,
  }),
}));

function TestComponent({ onAlert, onConfirm }) {
  const { showAlert, showConfirm } = useAlert();
  return (
    <>
      <button
        onClick={() => {
          if (onAlert) {
            showAlert(onAlert);
          } else {
            showAlert({
              title: 'Test Alert',
              message: 'Alert message',
              confirmLabel: 'OK',
            });
          }
        }}
      >
        Show Alert
      </button>
      <button
        onClick={() => {
          if (onConfirm) {
            showConfirm(onConfirm);
          } else {
            showConfirm({
              title: 'Test Confirm',
              message: 'Confirm message',
              confirmLabel: 'Yes',
              cancelLabel: 'No',
            });
          }
        }}
      >
        Show Confirm
      </button>
    </>
  );
}

describe('AlertContext', () => {
  it('shows alert modal with correct title and message', async () => {
    render(
      <AlertProvider>
        <TestComponent />
      </AlertProvider>
    );
    fireEvent.click(screen.getByText('Show Alert'));
    expect(await screen.findByText('Test Alert')).toBeInTheDocument();
    expect(screen.getByText('Alert message')).toBeInTheDocument();
    expect(screen.getByText('OK')).toBeInTheDocument();
  });

  it('calls onConfirm when confirm button is clicked', async () => {
    const onConfirm = jest.fn();
    render(
      <AlertProvider>
        <TestComponent
          onAlert={{ title: 'Confirm', message: 'Confirm?', onConfirm }}
        />
      </AlertProvider>
    );
    fireEvent.click(screen.getByText('Show Alert'));
    const confirmBtn = await screen.findByText('OK');
    fireEvent.click(confirmBtn);
    expect(onConfirm).toHaveBeenCalled();
  });

  it('shows confirm modal with cancel button and calls onCancel', async () => {
    const onCancel = jest.fn();
    render(
      <AlertProvider>
        <TestComponent
          onConfirm={{
            title: 'Confirm',
            message: 'Confirm?',
            onCancel,
            confirmLabel: 'Yes',
            cancelLabel: 'No',
          }}
        />
      </AlertProvider>
    );
    fireEvent.click(screen.getByText('Show Confirm'));
    expect(await screen.findByText('Confirm')).toBeInTheDocument();
    expect(screen.getByText('No')).toBeInTheDocument();
    fireEvent.click(screen.getByText('No'));
    expect(onCancel).toHaveBeenCalled();
  });

  it('makes showGlobalAlert available on window and shows alert', async () => {
    render(
      <AlertProvider>
        <div />
      </AlertProvider>
    );
    await act(async () => {
      window.showGlobalAlert({ title: 'Global', message: 'Global alert!' });
    });
    expect(await screen.findByText('Global')).toBeInTheDocument();
    expect(screen.getByText('Global alert!')).toBeInTheDocument();
  });

  it('makes showGlobalAlert available on window and accepts string message', async () => {
    render(
      <AlertProvider>
        <div />
      </AlertProvider>
    );
    await act(async () => {
      window.showGlobalAlert('String message');
    });
    expect(await screen.findByText('Alerta')).toBeInTheDocument();
    expect(screen.getByText('String message')).toBeInTheDocument();
  });
});

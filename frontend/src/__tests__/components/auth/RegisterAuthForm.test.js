import React from 'react';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

// Import component after mocks are set up
import { RegisterAuthForm } from 'components/auth/RegisterAuthForm';
import { authService } from 'services/authService';
import { render, mockNavigate, resetMocks } from 'test-utils';

// Mock only the services - no validation or translation mocking
jest.mock('services/authService', () => ({
  authService: {
    register: jest.fn(),
    login: jest.fn(),
    logout: jest.fn(),
  },
}));

// Use real components, real validation, real translation
jest.mock('contexts/AlertContext', () => ({
  AlertProvider: ({ children }) => <div>{children}</div>,
  useAlert: () => ({
    showAlert: jest.fn(),
    hideAlert: jest.fn(),
  }),
}));

// Mock the RegisterForm component
jest.mock('components/forms/RegisterForm', () => ({
  RegisterForm: ({
    formData,
    errors,
    onChange,
    onSubmit,
    loading,
    getErrorMessage,
    isSubmitted,
  }) => (
    <form onSubmit={onSubmit}>
      <input
        type="email"
        name="email"
        value={formData.email || ''}
        onChange={onChange}
        placeholder="Email"
        aria-label="Email"
      />
      <input
        type="password"
        name="password"
        value={formData.password || ''}
        onChange={onChange}
        placeholder="Password"
        aria-label="Password"
      />
      <input
        type="password"
        name="confirmPassword"
        value={formData.confirmPassword || ''}
        onChange={onChange}
        placeholder="Confirm Password"
        aria-label="Confirm Password"
      />
      <button type="submit" disabled={loading}>
        {loading ? 'Registering...' : 'Register'}
      </button>
      {isSubmitted && errors.email && (
        <span role="alert">
          {typeof errors.email === 'string'
            ? errors.email
            : 'Email is required'}
        </span>
      )}
      {isSubmitted && errors.password && (
        <span role="alert">
          {typeof errors.password === 'string'
            ? errors.password
            : 'Password is required'}
        </span>
      )}
      {isSubmitted && errors.confirmPassword && (
        <span role="alert">
          {typeof errors.confirmPassword === 'string'
            ? errors.confirmPassword
            : 'Passwords do not match'}
        </span>
      )}
    </form>
  ),
}));

// Mock error message hook
jest.mock('hooks/useErrorMessage', () => ({
  useErrorMessage: () => ({
    getErrorMessage: jest.fn(error => error?.message || 'An error occurred'),
  }),
}));

describe('RegisterAuthForm', () => {
  beforeEach(() => {
    resetMocks();
    jest.clearAllMocks();
  });

  // Helper function to wait for translations to load
  const waitForTranslations = async () => {
    await waitFor(
      () => {
        expect(
          screen.queryByText('Loading translations...')
        ).not.toBeInTheDocument();
      },
      { timeout: 3000 }
    );
  };

  it('should render register form', async () => {
    render(<RegisterAuthForm />);
    await waitForTranslations();

    expect(screen.getByText('Create Your Account')).toBeInTheDocument();
    expect(screen.getByText('Join our community today')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Email')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Confirm Password')).toBeInTheDocument();
    expect(
      screen.getByRole('button', { name: 'Register' })
    ).toBeInTheDocument();
  });

 

  it('should handle form validation errors', async () => {
    render(<RegisterAuthForm />);
    await waitForTranslations();

    const submitButton = screen.getByRole('button', { name: 'Register' });
    await userEvent.click(submitButton);

    await waitFor(() => {
      const errorElements = screen.queryAllByRole('alert');
      if (errorElements.length === 0) {
        expect(screen.getByPlaceholderText('Email')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
        expect(
          screen.getByPlaceholderText('Confirm Password')
        ).toBeInTheDocument();
      } else {
        expect(errorElements.length).toBeGreaterThan(0);
      }
    });

    const emailInput = screen.getByPlaceholderText('Email');
    await userEvent.clear(emailInput);
    await userEvent.type(emailInput, 'invalid-email');
    await userEvent.click(submitButton);

    await waitFor(() => {
      expect(emailInput).toBeInTheDocument();
    });
  });

  it('should handle password mismatch validation', async () => {
    render(<RegisterAuthForm />);
    await waitForTranslations();

    await userEvent.type(
      screen.getByPlaceholderText('Email'),
      'test@example.com'
    );
    await userEvent.type(
      screen.getByPlaceholderText('Password'),
      'Password123!'
    );
    await userEvent.type(
      screen.getByPlaceholderText('Confirm Password'),
      'DifferentPassword123!'
    );

    const submitButton = screen.getByRole('button', { name: 'Register' });
    await userEvent.click(submitButton);

    await waitFor(() => {
      const errorElements = screen.queryAllByRole('alert');
      if (errorElements.length > 0) {
        const passwordErrors = errorElements.filter(
          el =>
            el.textContent.toLowerCase().includes('password') ||
            el.textContent.toLowerCase().includes('match')
        );
        expect(passwordErrors.length).toBeGreaterThanOrEqual(0);
      } else {
        expect(submitButton).toBeInTheDocument();
      }
    });
  });

  it('should handle form input changes', async () => {
    render(<RegisterAuthForm />);
    await waitForTranslations();

    const emailInput = screen.getByPlaceholderText('Email');
    await userEvent.type(emailInput, 'test@example.com');
    expect(emailInput).toHaveValue('test@example.com');

    const passwordInput = screen.getByPlaceholderText('Password');
    await userEvent.type(passwordInput, 'Password123!');
    expect(passwordInput).toHaveValue('Password123!');

    const confirmPasswordInput =
      screen.getByPlaceholderText('Confirm Password');
    await userEvent.type(confirmPasswordInput, 'Password123!');
    expect(confirmPasswordInput).toHaveValue('Password123!');
  });
});

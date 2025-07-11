import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { FormInput } from '../../../components/forms/FormInput';

describe('FormInput', () => {
  it('renders input with label and placeholder', () => {
    render(
      <FormInput
        name="email"
        label="Email"
        type="email"
        value=""
        onChange={() => {}}
        placeholder="Enter your email"
      />
    );
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Enter your email')).toBeInTheDocument();
  });

  it('calls onChange when typing', async () => {
    const handleChange = jest.fn();
    render(
      <FormInput
        name="email"
        label="Email"
        type="email"
        value=""
        onChange={handleChange}
        placeholder="Enter your email"
      />
    );
    await userEvent.type(
      screen.getByPlaceholderText('Enter your email'),
      'test@example.com'
    );
    expect(handleChange).toHaveBeenCalled();
  });

  it('shows error message', () => {
    render(
      <FormInput
        name="email"
        label="Email"
        type="email"
        value=""
        onChange={() => {}}
        placeholder="Enter your email"
        error="Email is required"
      />
    );
    expect(screen.getByText('Email is required')).toBeInTheDocument();
  });

  it('renders a disabled input', () => {
    render(
      <FormInput
        name="email"
        label="Email"
        type="email"
        value=""
        onChange={() => {}}
        placeholder="Enter your email"
        disabled={true}
      />
    );
    expect(screen.getByLabelText('Email')).toBeDisabled();
  });
});

import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { SubmitButton } from '../../../components/forms/SubmitButton';

describe('SubmitButton', () => {
  it('renders with label', () => {
    render(<SubmitButton label="Save" isLoading={false} />);
    expect(screen.getByRole('button', { name: 'Save' })).toBeInTheDocument();
  });

  it('shows loading spinner and disables when isLoading', () => {
    render(<SubmitButton label="Save" isLoading={true} />);
    const button = screen.getByRole('button', { name: 'Save...' });
    expect(button).toBeDisabled();
expect(screen.getByText('Save...')).toBeInTheDocument();
expect(screen.getByRole('button', { name: 'Save...' })).toContainElement(
  screen.getByText('Save...')
);
expect(screen.getByRole('button', { name: 'Save...' }).querySelector('.spinner-border')).toBeInTheDocument();  });

  it('calls onClick when clicked', async () => {
    const handleClick = jest.fn();
    render(
      <SubmitButton label="Save" isLoading={false} onClick={handleClick} />
    );
    await userEvent.click(screen.getByRole('button', { name: 'Save' }));
    expect(handleClick).toHaveBeenCalled();
  });

  it('applies custom class and variant', () => {
    render(
      <SubmitButton
        label="Submit"
        isLoading={false}
        className="my-class"
        variant="success"
      />
    );
    const button = screen.getByRole('button', { name: 'Submit' });
    expect(button).toHaveClass('my-class');
    expect(button).toHaveClass('btn-success');
  });
});

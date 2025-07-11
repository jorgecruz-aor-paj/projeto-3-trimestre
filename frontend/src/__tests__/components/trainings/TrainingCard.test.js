import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { TrainingCard } from '../../../components/trainings/TrainingCard';

const training = {
  id: 1,
  title: 'React Basics',
  description: 'Learn the basics of React.',
  duration: 90,
  language: 'ENGLISH',
  active: true,
  createdAt: '2025-07-11T12:00:00Z',
  link: 'https://example.com',
  trainingArea: { name: 'Frontend' },
};

describe('TrainingCard', () => {
  it('renders training info', () => {
    render(<TrainingCard training={training} />);
    expect(screen.getByText('React Basics')).toBeInTheDocument();
    expect(screen.getByText('Learn the basics of React.')).toBeInTheDocument();
    expect(screen.getByText('Frontend')).toBeInTheDocument();
    expect(screen.getByText('1h 30m')).toBeInTheDocument();
  });

  it('shows active and language badges', () => {
    render(<TrainingCard training={training} />);
    expect(screen.getByText(/active/i)).toBeInTheDocument();
    expect(screen.getByText(/english/i)).toBeInTheDocument();
  });

  it('shows created date in footer', () => {
    render(<TrainingCard training={training} />);
    expect(screen.getByText(/2025/)).toBeInTheDocument();
  });

  it('calls onEdit when edit button is clicked', async () => {
  const onEdit = jest.fn();
  render(<TrainingCard training={training} onEdit={onEdit} />);
  const editBtn = screen.getAllByRole('button').find(btn => btn.className.includes('outline-primary'));
  await userEvent.click(editBtn);
  expect(onEdit).toHaveBeenCalledWith(training);
});

  it('calls onDeactivate when deactivate button is clicked', async () => {
    const onDeactivate = jest.fn();
    render(<TrainingCard training={training} onDeactivate={onDeactivate} />);
    const deactivateBtn = screen
      .getAllByRole('button')
      .find(btn => btn.className.includes('outline-danger'));
    await userEvent.click(deactivateBtn);
    expect(onDeactivate).toHaveBeenCalledWith(training);
  });

  it('shows link button if training.link exists', () => {
    render(<TrainingCard training={training} />);
    const linkBtn = screen
      .getAllByRole('button')
      .find(btn => btn.className.includes('outline-info'));
    expect(linkBtn).toBeInTheDocument();
    expect(linkBtn).toHaveAttribute('href', training.link);
  });

  it('does not show actions if showActions is false', () => {
    render(<TrainingCard training={training} showActions={false} />);
    expect(screen.queryByRole('button')).not.toBeInTheDocument();
  });

  it('shows inactive badge if training is not active', () => {
    const inactiveTraining = { ...training, active: false };
    render(<TrainingCard training={inactiveTraining} />);
    expect(screen.getByText(/inactive/i)).toBeInTheDocument();
  });
});

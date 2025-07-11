import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { FormDropdownInput } from '../../../components/forms/FormDropdownInput';

describe('FormDropdownInput', () => {
  it('renders dropdown with label and options', () => {
    render(
      <FormDropdownInput
        name="country"
        label="Country"
        value=""
        onChange={() => {}}
        options={[
          { value: 'br', label: 'Brazil' },
          { value: 'us', label: 'United States' },
        ]}
        placeholder="Select country"
      />
    );
    expect(screen.getByLabelText('Country')).toBeInTheDocument();
    expect(screen.getByText('Select country')).toBeInTheDocument();
    expect(screen.getByText('Brazil')).toBeInTheDocument();
    expect(screen.getByText('United States')).toBeInTheDocument();
  });

  it('calls onChange when selecting an option', async () => {
    const handleChange = jest.fn();
    render(
      <FormDropdownInput
        name="country"
        label="Country"
        value=""
        onChange={handleChange}
        options={[
          { value: 'br', label: 'Brazil' },
          { value: 'us', label: 'United States' },
        ]}
        placeholder="Select country"
      />
    );
    await userEvent.selectOptions(screen.getByLabelText('Country'), 'us');
    expect(handleChange).toHaveBeenCalled();
  });

  it('shows error message', () => {
    render(
      <FormDropdownInput
        name="country"
        label="Country"
        value=""
        onChange={() => {}}
        options={[
          { value: 'br', label: 'Brazil' },
          { value: 'us', label: 'United States' },
        ]}
        error="Country is required"
        placeholder="Select country"
      />
    );
    expect(screen.getByText('Country is required')).toBeInTheDocument();
  });
});

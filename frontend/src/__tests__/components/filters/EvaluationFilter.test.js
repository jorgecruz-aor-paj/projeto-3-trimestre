import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { EvaluationFilter } from 'components/filters/EvaluationFilter';

// Mocks
jest.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key, fallback) => fallback || key,
    i18n: { language: 'en' },
  }),
}));
jest.mock('hooks/useUserList', () => ({
  useUserList: () => ({
    userList: [
      { id: 1, firstName: 'Alice', lastName: 'Smith' },
      { id: 2, firstName: 'Bob', lastName: 'Jones' },
    ],
  }),
}));
jest.mock('contexts/AlertContext', () => ({
  useAlert: () => ({
    showAlert: jest.fn(),
  }),
}));
jest.mock('services/exportService', () => ({
  exportService: {
    evaluationsToCSVFiltered: jest.fn(() =>
      Promise.resolve({
        data: new Blob(['csvdata'], { type: 'text/csv' }),
        headers: { 'content-disposition': 'attachment; filename="test.csv"' },
      })
    ),
  },
}));
jest.mock('utils/evaluationScaleTranslations', () => ({
  translateEvaluationScale: value => value,
}));

const defaultFilters = {
  searchText: '',
  evaluatorId: '',
  evaluatedId: '',
  status: '',
  evaluationScale: '',
  startDate: '',
  endDate: '',
  evaluationSortField: 'DATE',
  ascending: true,
};

const defaultUser = { id: 1, role: 'ADMIN' };

function setup(props = {}) {
  const setFilters = jest.fn();
  return {
    setFilters,
    ...render(
      <EvaluationFilter
        filters={defaultFilters}
        setFilters={setFilters}
        user={defaultUser}
        displayedUserId={2}
        {...props}
      />
    ),
  };
}

describe('EvaluationFilter', () => {
  it('renders all filter fields', () => {
    const { container } = setup();
    // Search input
    expect(
      screen.getByPlaceholderText(/Search evaluation feedback/)
    ).toBeInTheDocument();
    // Evaluator and Evaluatee inputs (SearchableUserSelect)
    expect(screen.getAllByPlaceholderText('common.all').length).toBe(2);
    // Status select
    expect(screen.getByText('evaluationStatus.title')).toBeInTheDocument();
    // Scale select
    expect(screen.getByText('evaluationScale.title')).toBeInTheDocument();
    // Date inputs
    expect(container.querySelectorAll('input[type="date"]').length).toBe(2);
    // Sort select
    expect(screen.getByText('common.sortBy')).toBeInTheDocument();
    expect(screen.getAllByRole('combobox').length).toBeGreaterThanOrEqual(2);
  });

  it('calls setFilters when changing search text (debounced)', async () => {
    const { setFilters } = setup();
    const input = screen.getByPlaceholderText(/Search evaluation feedback/);
    fireEvent.change(input, { target: { value: 'test search' } });
    await waitFor(
      () => {
        expect(setFilters).toHaveBeenCalledWith(expect.any(Function));
      },
      { timeout: 1000 }
    );
  });

  it('calls setFilters when changing status', () => {
    const { setFilters } = setup();
    // Find the status select by its label text (translation key)
    const statusLabel = screen.getByText('evaluationStatus.title');
    const statusSelect = statusLabel.parentNode.querySelector('select');
    fireEvent.change(statusSelect, { target: { value: 'CLOSED' } });
    expect(setFilters).toHaveBeenCalledWith(expect.any(Function));
  });

  it('calls setFilters when changing scale', () => {
    const { setFilters } = setup();
    const scaleLabel = screen.getByText('evaluationScale.title');
    const scaleSelect = scaleLabel.parentNode.querySelector('select');
    fireEvent.change(scaleSelect, {
      target: { value: 'EXPECTED_CONTRIBUTION' },
    });
    expect(setFilters).toHaveBeenCalledWith(expect.any(Function));
  });

  it('calls setFilters when changing date fields', () => {
    const { setFilters, container } = setup();
    const dateInputs = container.querySelectorAll('input[type="date"]');
    fireEvent.change(dateInputs[0], { target: { value: '2025-07-01' } });
    fireEvent.change(dateInputs[1], { target: { value: '2025-07-10' } });
    expect(setFilters).toHaveBeenCalledWith(expect.any(Function));
  });

  it('calls setFilters when toggling sort order', () => {
    const { setFilters, container } = setup();
    // Find the sort button by its icon class (bi-sort-up or bi-sort-down)
    const sortBtn = Array.from(container.querySelectorAll('button')).find(btn =>
      btn.querySelector('.bi-sort-up, .bi-sort-down')
    );
    fireEvent.click(sortBtn);
    expect(setFilters).toHaveBeenCalledWith(expect.any(Function));
  });

  it('calls setFilters when clearing filters', () => {
    const { setFilters, container } = setup({
      filters: { ...defaultFilters, status: 'CLOSED' },
    });
    // Find the clear filters button by its icon class (bi-x-circle)
    const clearBtn = Array.from(container.querySelectorAll('button')).find(
      btn => btn.querySelector('.bi-x-circle')
    );
    fireEvent.click(clearBtn);
    expect(setFilters).toHaveBeenCalledWith({});
  });

  it('shows active filters summary when filters are set', () => {
    const { container } = setup({
      filters: {
        ...defaultFilters,
        status: 'CLOSED',
        evaluationScale: 'EXPECTED_CONTRIBUTION',
      },
    });
    // The summary label uses the translation key, but may have extra whitespace or be split
    expect(
      Array.from(container.querySelectorAll('small')).some(el =>
        el.textContent.includes('common.activeFilters')
      )
    ).toBe(true);
    // Find badges containing the filter values
    expect(
      Array.from(container.querySelectorAll('.badge')).some(el =>
        el.textContent.includes('status')
      )
    ).toBe(true);
    expect(
      Array.from(container.querySelectorAll('.badge')).some(el =>
        el.textContent.includes('CLOSED')
      )
    ).toBe(true);
    expect(
      Array.from(container.querySelectorAll('.badge')).some(el =>
        el.textContent.includes('evaluationScale')
      )
    ).toBe(true);
    expect(
      Array.from(container.querySelectorAll('.badge')).some(el =>
        el.textContent.includes('EXPECTED_CONTRIBUTION')
      )
    ).toBe(true);
  });

  it('calls exportService.evaluationsToCSVFiltered when clicking CSV button', async () => {
    setup();
    const csvBtn = screen.getByText('CSV');
    fireEvent.click(csvBtn);
    await waitFor(() => {
      expect(
        require('services/exportService').exportService.evaluationsToCSVFiltered
      ).toHaveBeenCalled();
    });
  });

  it('renders extraFields if provided', () => {
    setup({ extraFields: <div data-testid="extra">Extra Field</div> });
    expect(screen.getByTestId('extra')).toBeInTheDocument();
  });
});

import { renderHook, act } from '@testing-library/react';
import { useWorkplaces } from 'hooks/useWorkplaces';

// Mock workplaceService
jest.mock('services/workplaceService', () => ({
  workplaceService: {
    getWorkplaces: jest.fn(),
  },
}));

const mockWorkplaces = [
  { id: 1, name: 'Office A' },
  { id: 2, name: 'Office B' },
];

describe('useWorkplaces', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    sessionStorage.clear();
  });

  it('loads workplaces from service and caches them', async () => {
    require('services/workplaceService').workplaceService.getWorkplaces.mockResolvedValueOnce(
      mockWorkplaces
    );
    const { result } = renderHook(() => useWorkplaces());
    // Initially loading
    expect(result.current.workplacesLoading).toBe(true);
    // Wait for effect
    await act(async () => {
      // Wait for next tick
      await Promise.resolve();
    });
    expect(result.current.workplacesLoading).toBe(false);
    expect(result.current.workplaces).toEqual(mockWorkplaces);
    expect(sessionStorage.getItem('workplaces')).toEqual(
      JSON.stringify(mockWorkplaces)
    );
  });

  it('loads workplaces from cache if available', async () => {
    sessionStorage.setItem('workplaces', JSON.stringify(mockWorkplaces));
    const { result } = renderHook(() => useWorkplaces());
    await act(async () => {
      await Promise.resolve();
    });
    expect(result.current.workplacesLoading).toBe(false);
    expect(result.current.workplaces).toEqual(mockWorkplaces);
    expect(
      require('services/workplaceService').workplaceService.getWorkplaces
    ).not.toHaveBeenCalled();
  });

  it('handles service error gracefully', async () => {
    require('services/workplaceService').workplaceService.getWorkplaces.mockRejectedValueOnce(
      new Error('fail')
    );
    const { result } = renderHook(() => useWorkplaces());
    await act(async () => {
      await Promise.resolve();
    });
    expect(result.current.workplacesLoading).toBe(false);
    expect(result.current.workplaces).toEqual([]);
  });
});

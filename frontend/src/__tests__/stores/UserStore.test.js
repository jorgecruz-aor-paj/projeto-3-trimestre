import { act } from 'react-dom/test-utils';
import { useUserStore } from 'stores/UserStore';

jest.mock('services/apiService', () => ({
  apiService: {
    getAll: jest.fn(),
  },
}));

const mockUsers = [
  { id: 1, name: 'Alice' },
  { id: 2, name: 'Bob' },
];

describe('useUserStore', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('has initial state', () => {
    const store = useUserStore.getState();
    expect(store.users).toEqual([]);
    expect(store.loading).toBe(false);
    expect(store.error).toBe(null);
  });

  it('fetches users successfully', async () => {
    require('services/apiService').apiService.getAll.mockResolvedValueOnce(
      mockUsers
    );
    const store = useUserStore.getState();
    await act(async () => {
      await store.fetchUsers();
    });
    const updated = useUserStore.getState();
    expect(updated.users).toEqual(mockUsers);
    expect(updated.loading).toBe(false);
    expect(updated.error).toBe(null);
  });

  it('handles fetch error', async () => {
  // Reset store to initial state before error test
  useUserStore.setState({ users: [], loading: false, error: null });
  require('services/apiService').apiService.getAll.mockRejectedValueOnce(new Error('fail'));
  const store = useUserStore.getState();
  await act(async () => {
    await store.fetchUsers();
  });
  const updated = useUserStore.getState();
  expect(updated.users).toEqual([]);
  expect(updated.loading).toBe(false);
  expect(updated.error).toBe('fail');
});
  it('passes filters to apiService.getAll', async () => {
    require('services/apiService').apiService.getAll.mockResolvedValueOnce([]);
    const store = useUserStore.getState();
    const filters = {
      workplaceId: 123,
      managerId: 456,
      searchText: 'abc',
      ascending: true,
    };
    await act(async () => {
      await store.fetchUsers(filters);
    });
    expect(
      require('services/apiService').apiService.getAll
    ).toHaveBeenCalledWith(
      expect.anything(),
      expect.objectContaining({
        workplaceId: 123,
        managerId: 456,
        searchText: 'abc',
        ascending: true,
      })
    );
  });
});

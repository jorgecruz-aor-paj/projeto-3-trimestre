import { useEffect, useState } from 'react';
import { userService } from 'services';
import { useTranslation } from 'react-i18next';

export function useUserList(includeAdmins = false) {
  const { t } = useTranslation();
  const [userList, setUserList] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let isMounted = true;
    const fetchData = async () => {
      setLoading(true);
      try {
        const params = includeAdmins ? { includeAdmins: true } : {};
        const response = await userService.getMiniUsers(params);
        if (isMounted) {
          setUserList(response || []);
          setError(null);
        }
      } catch (err) {
        if (isMounted) {
          setUserList([]);
          setError(err.message || t('auth.loginFailed'));
        }
      } finally {
        if (isMounted) setLoading(false);
      }
    };
    fetchData();
    return () => {
      isMounted = false;
    };
  }, [t, includeAdmins]);

  return { userList, loading, error };
}

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { performLogin } from 'pages/login/authActions';

export default function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await performLogin(email, password);
      navigate('/dashboard');
    } catch (err) {
      setError(err.message || 'Login failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="p-4 border rounded shadow-sm bg-light"
    >
      <div className="mb-3">
        <label className="form-label">Email</label>
        <input
          className="form-control"
          type="email"
          value={email}
          disabled={loading}
          onChange={e => setEmail(e.target.value)}
          required
        />
      </div>

      <div className="mb-3">
        <label className="form-label">Password</label>
        <input
          className="form-control"
          type="password"
          value={password}
          disabled={loading}
          onChange={e => setPassword(e.target.value)}
          required
        />
      </div>

      <button
        className="btn btn-primary w-100"
        type="submit"
        disabled={loading}
      >
        {loading ? (
          <>
            <span
              className="spinner-border spinner-border-sm me-2"
              role="status"
              aria-hidden="true"
            ></span>
            Logging in...
          </>
        ) : (
          'Login'
        )}
      </button>

      {error && <div className="mt-3 alert alert-danger">{error}</div>}
    </form>
  );
}

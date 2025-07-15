import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../../stores/AuthStore';

// Protects routes by checking if user is authenticated
const PrivateRoute = ({ redirectPath = '/error/403', children }) => {
  const token = useAuthStore(state => state.token);

  if (!token) {
    return <Navigate to={redirectPath} replace />;
  }

  // If children are provided, render them; else render nested routes (Outlet)
  return children ? children : <Outlet />;
};

export default PrivateRoute;

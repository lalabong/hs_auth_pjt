import { Navigate, useLocation } from "react-router-dom";

import { useAuthStore } from "../store/authStore";

interface GuestGuardProps {
  children: React.ReactNode;
}

const GuestGuard = ({ children }: GuestGuardProps) => {
  const { isAuthenticated } = useAuthStore();

  const location = useLocation();

  const from = location.state?.from?.pathname || "/dashboard";

  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }

  return <>{children}</>;
};

export default GuestGuard;

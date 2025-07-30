import { Navigate, useLocation } from "react-router-dom";

import { useAuthStore } from "../store/authStore";

interface AuthGuardProps {
  children: React.ReactNode;
}

const AuthGuard = ({ children }: AuthGuardProps) => {
  const { isAuthenticated } = useAuthStore();

  const location = useLocation();

  if (!isAuthenticated) {
    // 현재 시도한 페이지 경로를 로그인 후 리다이렉션을 위해 state로 전달
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
};

export default AuthGuard;

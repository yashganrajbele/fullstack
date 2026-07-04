import { Navigate, Outlet } from "react-router-dom";
import { useSelector } from "react-redux";

const PublicRoute = () => {
  const token = useSelector((state) => state.auth.token);

  return token ? <Navigate to="/auth/protected" replace /> : <Outlet />;
};

export default PublicRoute;

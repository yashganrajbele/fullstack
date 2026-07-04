import { Routes, Route, Navigate } from "react-router-dom";
import AuthRoutes from "@/features/auth/AuthRoutes";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/auth/login" replace />} />
      <Route path="/auth/*" element={<AuthRoutes />} />
    </Routes>
  );
};

export default AppRoutes;

import { useAuthStore } from "@/stores/useAuthStore";
import { User } from "@/types";
import { Navigate, Outlet } from "react-router-dom";

interface Props {
  roles: string[];
}

export default function RoleGuard({ roles }: Props) {
  const authStore = useAuthStore();
  const user = authStore.user as User;

  if (!authStore.accessToken || !user.id) {
    return <Navigate to="/login" replace />;
  }
  
  if (user && !roles.includes(user.role)) {
    return <Navigate to="/forbidden" replace />;
  }

  return <Outlet />;
}
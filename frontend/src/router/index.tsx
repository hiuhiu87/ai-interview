import { Navigate, useRoutes } from "react-router-dom";
import { adminRoutes } from "@/router/modules/admin.routes";
import { commonRoutes } from "@/router/modules/common.routes";
import { interviewerRoutes } from "@/router/modules/interviewer.routes";

import RoleGuard from "@/router/guards/RoleGuard";
import { ROUTER_PATH } from "@/constants/path";

export default function AppRouter() {
  const routes = useRoutes([
    {
      path: "/",
      element: <Navigate to="/login" replace />,
    },
    ...commonRoutes,
    {
      path: ROUTER_PATH.admin.path,
      element: (
        <RoleGuard roles={["ADMIN"]} />
      ),
      children: adminRoutes,
    },
    {
      path: ROUTER_PATH.interviewer.path,
      element: (
        <RoleGuard roles={["INTERVIEWER"]} />
      ),
      children: interviewerRoutes,
    }
  ]);

  return routes;
}
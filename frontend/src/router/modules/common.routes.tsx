import { lazy, Suspense } from "react";
import PublicLayout from "../layouts/PublicLayout";
import { ROUTER_PATH } from "@/constants/path";

const Login = lazy(() => import("@/pages/auth/LoginPage"));
const Unauthorized = lazy(() => import("@/pages/exceptions/Unauthorized"));
const Forbidden = lazy(() => import("@/pages/exceptions/Forbidden"));
const Notfound = lazy(() => import("@/pages/exceptions/Notfound"));

const withSuspense = (Component: React.ReactNode) => (
  <Suspense fallback={<div>Loading...</div>}>{Component}</Suspense>
);

export const commonRoutes = [
  {
    path: "/",
    element: <PublicLayout />,
    children: [
      { path: ROUTER_PATH.common.children.login.path, element: withSuspense(<Login />) },
      { path: ROUTER_PATH.common.children.unauthorized.path, element: withSuspense(<Unauthorized />) },
      { path: ROUTER_PATH.common.children.forbidden.path, element: withSuspense(<Forbidden />) },
      { path: ROUTER_PATH.common.children.notfound.path, element: withSuspense(<Notfound />) },
    ],
  },
];
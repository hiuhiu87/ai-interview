import { lazy, Suspense } from "react";
import type { RouteObject } from "react-router-dom";
import AdminLayout from "../layouts/AdminLayout";
import { ROUTER_PATH } from "@/constants/path";

const Dashboard = lazy(() => import("@/pages/admin/dashboard/Dashboard"));
const SkillTree = lazy(() => import("@/pages/admin/skills/Skills"));
const Templates = lazy(() => import("@/pages/admin/templates/Templates"));
const Users = lazy(() => import("@/pages/admin/users/Users"));
const Questions = lazy(() => import("@/pages/admin/questions/Questions"));

const withSuspense = (Component: React.ReactNode) => (
  <Suspense fallback={<div>Loading...</div>}>{Component}</Suspense>
);

export const adminRoutes: RouteObject[] = [
  {
    path: ROUTER_PATH.admin.path,
    element: <AdminLayout />,
    children: [
      { path: ROUTER_PATH.admin.children.dashboard.path, element: withSuspense(<Dashboard />) },
      { path: ROUTER_PATH.admin.children.skills.path, element: withSuspense(<SkillTree />) },
      { path: ROUTER_PATH.admin.children.templates.path, element: withSuspense(<Templates />) },
      { path: ROUTER_PATH.admin.children.users.path, element: withSuspense(<Users />) },
      { path: ROUTER_PATH.admin.children.questions.path, element: withSuspense(<Questions />) },
    ],
    
  }
];

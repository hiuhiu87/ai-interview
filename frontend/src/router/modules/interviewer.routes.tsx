import { lazy, Suspense } from "react";
import InterviewerLayout from "../layouts/InterviewerLayout";
import { ROUTER_PATH } from "@/constants/path";
import { RouteObject } from "react-router-dom";

const Sessions = lazy(() => import("@/pages/interviewer/sessions/Sessions"));

const withSuspense = (Component: React.ReactNode) => (
  <Suspense fallback={<div>Loading...</div>}>{Component}</Suspense>
);

export const interviewerRoutes: RouteObject[] = [
  {
    path: ROUTER_PATH.interviewer.path,
    element: <InterviewerLayout />,
    children: [
      { path: ROUTER_PATH.interviewer.children.session.path, element: withSuspense(<Sessions />) },
    ],
  },
];
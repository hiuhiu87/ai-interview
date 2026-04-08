import {
  LayoutDashboard,
  Sparkles,
  MessageCircleQuestion,
  ClipboardList,
  Video,
  Tags,
} from "lucide-react";
import type { SidebarItemType } from "@/types/sidebar.types";
import { ROUTER_PATH } from "@/constants/path";

const adminPrefix = ROUTER_PATH.admin.path;

export const ADMIN_SIDEBAR_ITEMS: SidebarItemType[] = [
  {
    id: "admin-dashboard",
    label: "Dashboard",
    icon: LayoutDashboard,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.dashboard.path}`,
  },
  {
    id: "admin-skills",
    label: "Skill Tree",
    icon: Sparkles,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.skills.path}`,
  },
  {
    id: "admin-catalog",
    label: "Templates & Tags",
    icon: Tags,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.catalog.path}`,
  },
  {
    id: "admin-questions",
    label: "Question Metadata",
    icon: MessageCircleQuestion,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.questions.path}`,
  },
  {
    id: "admin-rubrics",
    label: "Rubrics",
    icon: ClipboardList,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.rubrics.path}`,
  },
];

export const INTERVIEWER_SIDEBAR_ITEMS: SidebarItemType[] = [
  {
    id: "inter-dashboard",
    label: "Session",
    icon: Video,
    path: "/interviewer/session",
  },
];

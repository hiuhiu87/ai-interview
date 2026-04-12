import {
  LayoutDashboard,
  Sparkles,
  MessageCircleQuestion,
  Video,
  Tags,
  Users,
} from "lucide-react";
import type { SidebarItemType } from "@/types/sidebar.types";
import { ROUTER_PATH } from "@/constants/path";

const adminPrefix = ROUTER_PATH.admin.path;

export const ADMIN_SIDEBAR_ITEMS: SidebarItemType[] = [
  {
    id: "admin-dashboard",
    section: "Workspace",
    label: "Dashboard",
    icon: LayoutDashboard,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.dashboard.path}`,
  },
  {
    id: "admin-skills",
    section: "Content",
    label: "Skill Tree",
    icon: Sparkles,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.skills.path}`,
  },
  {
    id: "admin-templates",
    section: "Content",
    label: "Templates & Tags",
    icon: Tags,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.templates.path}`,
  },
  {
    id: "admin-questions",
    section: "Content",
    label: "Question Metadata",
    icon: MessageCircleQuestion,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.questions.path}`,
  },
  {
    id: "admin-users",
    section: "Access",
    label: "Users",
    icon: Users,
    path: `${adminPrefix}/${ROUTER_PATH.admin.children.users.path}`,
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

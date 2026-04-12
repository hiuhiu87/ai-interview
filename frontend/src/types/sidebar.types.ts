import type { LucideIcon } from "lucide-react";

export interface SidebarItemType {
  id: string;
  section?: string;
  label: string;
  icon: LucideIcon;
  path: string;
}

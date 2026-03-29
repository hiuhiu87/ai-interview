import type { MouseEventHandler } from "react";
import type { SidebarItemType } from "@/types/sidebar.types";

interface SidebarItemProps {
  item: SidebarItemType;
  isActive: boolean;
  collapsed: boolean;
  onClick: MouseEventHandler<HTMLButtonElement>;
}

export function SidebarItem({ item, isActive, collapsed, onClick }: SidebarItemProps) {
  const ActiveClass = isActive
    ? "bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-200"
    : "text-gray-700 dark:text-gray-200";

  const baseClass =
    "flex items-center gap-3 rounded-lg px-3 py-2 transition-all duration-150 hover:bg-blue-50 hover:text-blue-700 dark:hover:bg-blue-950/40 dark:hover:text-blue-200 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-blue-500";

  return (
    <button type="button" onClick={onClick} className={`${baseClass} ${ActiveClass} w-full text-left`}>
      <item.icon className="h-5 w-5" />
      {!collapsed && <span className="truncate text-sm font-medium">{item.label}</span>}
    </button>
  );
}

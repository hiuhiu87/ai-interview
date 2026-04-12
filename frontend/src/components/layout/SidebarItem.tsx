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
    ? "bg-[#171717] text-white dark:bg-white dark:text-[#171717]"
    : "text-[rgba(23,23,23,0.72)] dark:text-white/80";

  const baseClass =
    "flex items-center gap-3 rounded-lg px-3 py-2 transition-all duration-150 hover:bg-[rgba(23,23,23,0.05)] hover:text-[#171717] dark:hover:bg-white/10 dark:hover:text-white focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[rgba(23,23,23,0.2)]";

  return (
    <button type="button" onClick={onClick} className={`${baseClass} ${ActiveClass} w-full text-left`}>
      <item.icon className="h-5 w-5" />
      {!collapsed && <span className="truncate text-sm font-medium">{item.label}</span>}
    </button>
  );
}

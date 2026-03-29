import { useState } from "react";
import { SidebarItem } from "@/components/layout/SidebarItem";
import { useNavigate } from "react-router-dom";
import { SidebarItemType } from "@/types/sidebar.types";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface SidebarProps {
  onItemClick?: (id: string) => void;
  menuItems: SidebarItemType[];
}

export function Sidebar({ onItemClick, menuItems }: SidebarProps) {
  const [activeItemId, setActiveItemId] = useState<string>("admin-dashboard");
  const [collapsed, setCollapsed] = useState<boolean>(false);
  const sidebarWidthClass = collapsed ? "w-20" : "w-20 md:w-60";
  const navigate = useNavigate();

  return (
    <aside
      className={`h-screen flex flex-col border-r border-gray-200 bg-white p-3 dark:border-gray-800 dark:bg-slate-950 transition-all duration-300 ease-in-out ${sidebarWidthClass}`}
    >
      <div
        className={`mb-4 px-1 text-sm font-semibold text-slate-500 dark:text-slate-300 ${
          collapsed ? "hidden" : "hidden md:block"
        }`}
      >
        AI Interview
      </div>

      <nav className="flex-1 overflow-y-auto flex flex-col gap-2">
        {menuItems.map((item) => (
          <SidebarItem
            key={item.id}
            item={item}
            isActive={item.id === activeItemId}
            collapsed={collapsed}
            onClick={() => {
              setActiveItemId(item.id);
              onItemClick?.(item.id);
              navigate(item.path);
            }}
          />
        ))}
      </nav>

      <div className="pt-3 flex justify-center">
        <button
          type="button"
          onClick={() => setCollapsed((prev) => !prev)}
          className="rounded-md border border-gray-200 bg-white px-3 py-2 text-sm text-gray-700 transition hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-900 dark:text-gray-100 dark:hover:bg-gray-800"
        >
          {collapsed ? <ChevronRight/> : <ChevronLeft/> }
        </button>
      </div>
    </aside>
  );
}

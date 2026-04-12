import { useMemo, useState } from "react";
import { SidebarItem } from "@/components/layout/SidebarItem";
import { useLocation, useNavigate } from "react-router-dom";
import { SidebarItemType } from "@/types/sidebar.types";
import { PanelLeft } from "lucide-react";

interface SidebarProps {
  onItemClick?: (id: string) => void;
  menuItems: SidebarItemType[];
}

export function Sidebar({ onItemClick, menuItems }: SidebarProps) {
  const [collapsed, setCollapsed] = useState<boolean>(false);
  const sidebarWidthClass = collapsed ? "w-20" : "w-20 md:w-60";
  const navigate = useNavigate();
  const location = useLocation();

  const activeItemId = useMemo(() => {
    const currentPath = location.pathname.replace(/\/+$/, "") || "/";

    const matchedItem = menuItems.find((item) => {
      const itemPath = item.path.replace(/\/+$/, "") || "/";
      return currentPath === itemPath || currentPath.startsWith(`${itemPath}/`);
    });

    return matchedItem?.id ?? "";
  }, [location.pathname, menuItems]);

  return (
    <aside
      className={`h-screen flex flex-col border-r border-[rgba(23,23,23,0.12)] bg-white p-3 dark:border-white/10 dark:bg-[#171717] transition-all duration-300 ease-in-out ${sidebarWidthClass}`}
    >
      <div className="flex items-center justify-between gap-2 px-1">
        <div
          className={`text-sm font-semibold text-[rgba(23,23,23,0.56)] dark:text-white/70 ${
            collapsed ? "hidden" : "hidden md:block"
          }`}
        >
          AI Interview
        </div>

        <button
          type="button"
          onClick={() => setCollapsed((prev) => !prev)}
          className="ml-auto inline-flex size-8 items-center justify-center rounded-md border border-[rgba(23,23,23,0.12)] bg-white text-[#171717] transition hover:bg-[rgba(23,23,23,0.05)] dark:border-white/10 dark:bg-[#171717] dark:text-white dark:hover:bg-white/8 !ml-1"
        >
          <PanelLeft className={`size-4 transition-transform ${collapsed ? "rotate-180" : ""}`} />
        </button>
      </div>

      <nav className="flex-1 overflow-y-auto flex flex-col gap-2">
        {menuItems.map((item, index) => {
          const previousSection = index > 0 ? menuItems[index - 1]?.section : undefined;
          const showSection = !collapsed && item.section && item.section !== previousSection;

          return (
            <div key={item.id} className="flex flex-col gap-2">
              {showSection && (
                <div className="px-2 pt-3">
                  <div className="mb-2 h-px bg-[rgba(23,23,23,0.08)] dark:bg-white/10" />
                  <div className="text-[11px] font-semibold uppercase tracking-[0.16em] text-[rgba(23,23,23,0.42)] dark:text-white/45">
                    {item.section}
                  </div>
                </div>
              )}

              <SidebarItem
                item={item}
                isActive={item.id === activeItemId}
                collapsed={collapsed}
                onClick={() => {
                  onItemClick?.(item.id);
                  if (location.pathname !== item.path) {
                    navigate(item.path);
                  }
                }}
              />
            </div>
          );
        })}
      </nav>

    </aside>
  );
}

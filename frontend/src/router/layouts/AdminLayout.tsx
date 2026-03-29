import Header from "@/components/layout/Header";
import { Sidebar } from "@/components/layout/Sidebar";
import { Spinner } from "@/components/ui/spinner";
import { ADMIN_SIDEBAR_ITEMS } from "@/constants/sidebar.menu";
import { useAuthStore } from "@/stores/useAuthStore";
import { User } from "@/types";
import { useState } from "react";
import { Outlet } from "react-router-dom";

export default function AdminLayout() {
  const user = useAuthStore((state) => state.user) as User;
  const [isLoading, setIsLoading] = useState(false);

  const handleClick = (id: string) => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
    }, 300);
  }

  return (
    <div className="flex w-screen min-h-screen">
      <Sidebar menuItems={ADMIN_SIDEBAR_ITEMS} onItemClick={handleClick}></Sidebar>
      <div className="flex-1">
        <Header
          user={user}
          onLogout={() => {
            console.log("Logout...");
          }}
        />
        {isLoading && 
          <div className="fixed inset-0 flex items-center justify-center bg-black/10 z-50">
            <Spinner/>
          </div>
        }
        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
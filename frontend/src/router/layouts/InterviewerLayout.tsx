import Header from "@/components/layout/Header";
import { Sidebar } from "@/components/layout/Sidebar";
import { ADMIN_SIDEBAR_ITEMS, INTERVIEWER_SIDEBAR_ITEMS } from "@/constants/sidebar.menu";
import { useAuthStore } from "@/stores/useAuthStore";
import { User } from "@/types";
import { useEffect, useState } from "react";
import { Outlet, Link } from "react-router-dom";

export default function InterviewerLayout() {
  const authStore = useAuthStore();
  const [user, setUser] = useState({} as User);

  useEffect(() => {
    console.log("Component mounted");
    setUser(authStore.user || {} as User);

  }, [authStore]);

  return (
    <div className="flex w-screen min-h-screen">
      <Sidebar menuItems={INTERVIEWER_SIDEBAR_ITEMS}></Sidebar>
      <div className="flex-1">
        <Header
          user={user}
          onLogout={() => {
            console.log("Logout...");
          }}
        />

        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
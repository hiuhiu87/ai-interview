import { ROUTER_PATH } from "@/constants/path";
import { User } from "@/types";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";


interface HeaderProps {
  user: User;
  onLogout?: () => void;
}

export default function Header({ user, onLogout }: HeaderProps) {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const handleLogout = () => {
    setOpen(false);
    onLogout?.();
    navigate("/" + ROUTER_PATH.common.children.login.path);
  }

  const handleRedirectProfile = () => {
    setOpen(false);
    toast.success("Thông báo", {
      description: "handleRedirectProfile",
    });
  }

  return (
    <header className="flex items-center justify-end h-16 px-6 border-b bg-white dark:bg-slate-900 dark:border-slate-800">
      <div className="relative">
        <button
          onClick={() => setOpen((prev) => !prev)}
          className="flex align-bottom items-center gap-3 rounded-full hover:bg-gray-100 dark:hover:bg-slate-800 px-2 py-1 transition"
        >
          <img
            src={ "https://ui-avatars.com/api/?name=" + user.name }
            alt="avatar"
            className="w-9 h-9 rounded-full object-cover border"
          />
          <div className="hidden md:flex flex-col text-left">
            <span className="text-sm font-medium text-slate-800 dark:text-white">
              {user.name}
            </span>
            <span className="text-xs text-slate-500">{user.role}</span>
          </div>
        </button>

        {open && (
          <div className="absolute right-0 mt-2 w-48 rounded-xl border bg-white shadow-lg dark:bg-slate-900 dark:border-slate-800">
            <button 
              onClick={handleRedirectProfile}
              className="w-full px-4 py-2 text-left text-sm hover:bg-gray-100 dark:hover:bg-slate-800">
              Profile
            </button>
            <button
              onClick={handleLogout}
              className="w-full px-4 py-2 text-left text-sm text-red-500 hover:bg-gray-100 dark:hover:bg-slate-800"
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  );
}
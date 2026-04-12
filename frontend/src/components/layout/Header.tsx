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
    toast.success("Notice", {
      description: "handleRedirectProfile",
    });
  }

  return (
    <header className="flex h-16 items-center justify-end border-b border-[rgba(23,23,23,0.12)] bg-white px-6 dark:border-white/10 dark:bg-[#171717]">
      <div className="relative">
        <button
          onClick={() => setOpen((prev) => !prev)}
          className="flex items-center gap-3 rounded-full px-2 py-1 transition hover:bg-[rgba(23,23,23,0.04)] dark:hover:bg-white/8"
        >
          <img
            src={ "https://ui-avatars.com/api/?name=" + user.name }
            alt="avatar"
            className="w-9 h-9 rounded-full object-cover border"
          />
          <div className="hidden md:flex flex-col text-left">
            <span className="text-sm font-medium text-[#171717] dark:text-white">
              {user.name}
            </span>
            <span className="text-xs text-[rgba(23,23,23,0.56)] dark:text-white/60">{user.role}</span>
          </div>
        </button>

        {open && (
          <div className="absolute right-0 mt-2 w-48 rounded-xl border border-[rgba(23,23,23,0.12)] bg-white shadow-[0_12px_32px_rgba(23,23,23,0.12)] dark:border-white/10 dark:bg-[#171717]">
            <button 
              onClick={handleRedirectProfile}
              className="w-full px-4 py-2 text-left text-sm text-[#171717] hover:bg-[rgba(23,23,23,0.04)] dark:text-white dark:hover:bg-white/8">
              Profile
            </button>
            <button
              onClick={handleLogout}
              className="w-full px-4 py-2 text-left text-sm text-[#171717] hover:bg-[rgba(23,23,23,0.04)] dark:text-white dark:hover:bg-white/8"
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  );
}

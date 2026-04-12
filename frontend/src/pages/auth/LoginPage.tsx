import { ROUTER_PATH } from "@/constants/path";
import { useAuthStore } from "@/stores/useAuthStore";
import { User } from "@/types";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  const [form, setForm] = useState({
    username: "",
    password: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const navigate = useNavigate();
  const authStore = useAuthStore();

  useEffect(() => {
    authStore.clearAuth();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    const adminFake = {
      id: "1",
      name: "Hung Vu",
      email: "hungvt@gmail.com",
      role: "ADMIN"
    } as User;
    const interviewerFake = {
      id: "2",
      name: "Hung Vu",
      email: "hungvt@gmail.com",
      role: "INTERVIEWER"
    } as User;
    const tokenFake = "tokenFake"

    try {
      if (form.username === "admin") {
        authStore.setAuth(adminFake, tokenFake);
        navigate(`${ROUTER_PATH.admin.path}/${ROUTER_PATH.admin.children.dashboard.path}`);
      } else if (form.username === "interviewer") {
        authStore.setAuth(interviewerFake, tokenFake);
        navigate(`${ROUTER_PATH.interviewer.path}/${ROUTER_PATH.interviewer.children.session.path}`);
      } else {
        throw new Error("Invalid credentials");
      }
    } catch (err: any) {
      setError(err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex w-screen min-h-screen items-center justify-center bg-white px-4 dark:bg-[#171717]">
      <div className="w-full max-w-md rounded-2xl border border-[rgba(23,23,23,0.12)] bg-white p-6 shadow-[0_20px_60px_rgba(23,23,23,0.08)] dark:border-white/10 dark:bg-[#171717]">

        <h2 className="text-2xl font-semibold text-center text-[#171717] dark:text-white">
          Login
        </h2>

        {error && (
          <div className="mt-4 rounded-md border border-[rgba(23,23,23,0.12)] bg-[rgba(23,23,23,0.04)] px-3 py-2 text-sm text-[#171717] dark:border-white/10 dark:bg-white/5 dark:text-white">
            {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleLogin} className="mt-6 space-y-4">

          <input
            type="text"
            name="username"
            placeholder="Username"
            value={form.username}
            onChange={handleChange}
            className="w-full rounded-md border border-[rgba(23,23,23,0.14)] bg-white px-3 py-2 text-sm text-[#171717] shadow-[0_1px_2px_rgba(23,23,23,0.04)] focus:outline-none focus:ring-4 focus:ring-[rgba(23,23,23,0.08)] focus:border-[#171717] dark:border-white/12 dark:bg-[#171717] dark:text-white"
            required
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            className="w-full rounded-md border border-[rgba(23,23,23,0.14)] bg-white px-3 py-2 text-sm text-[#171717] shadow-[0_1px_2px_rgba(23,23,23,0.04)] focus:outline-none focus:ring-4 focus:ring-[rgba(23,23,23,0.08)] focus:border-[#171717] dark:border-white/12 dark:bg-[#171717] dark:text-white"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-[#171717] py-2 text-sm font-medium text-white hover:bg-[#2a2a2a] disabled:opacity-50 dark:bg-white dark:text-[#171717] dark:hover:bg-white/90"
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

      </div>
    </div>
  );
}

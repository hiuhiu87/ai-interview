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
      name: "Hùng Vũ",
      email: "hungvt@gmail.com",
      role: "ADMIN"
    } as User;
    const interviewerFake = {
      id: "2",
      name: "Hùng Vũ",
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
    <div className="flex w-screen min-h-screen items-center justify-center bg-gray-50 dark:bg-slate-950 px-4">
      <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-md dark:bg-slate-900">

        <h2 className="text-2xl font-semibold text-center text-gray-800 dark:text-white">
          Login
        </h2>

        {error && (
          <div className="mt-4 rounded-md bg-red-100 px-3 py-2 text-sm text-red-600">
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
            className="w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-slate-800 dark:border-gray-700"
            required
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            className="w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-slate-800 dark:border-gray-700"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-md bg-blue-600 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

      </div>
    </div>
  );
}
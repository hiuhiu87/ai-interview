import { ROUTER_PATH } from "@/constants/path";
import { useAuthStore } from "@/stores/useAuthStore";
import { useNavigate } from "react-router-dom";

export default function Notfound() {
  const navigate = useNavigate();
  const authStore = useAuthStore();

  const handleGoHome = () => {
    const role = authStore.user?.role;
    if (role === "ADMIN") {
      navigate(ROUTER_PATH.admin.path + "/" + ROUTER_PATH.admin.children.dashboard.path);
    } else if (role === "INTERVIEWER") {
      navigate(ROUTER_PATH.interviewer.path + "/" + ROUTER_PATH.interviewer.children.session.path);
    } else {
      navigate("/" + ROUTER_PATH.common.children.login.path);
    }
  }

  return (
    <div className="flex w-screen min-h-screen flex-col items-center justify-center bg-gray-50 dark:bg-slate-950 px-4 text-center">
      
      <h1 className="text-7xl font-bold text-gray-800 dark:text-white">
        404
      </h1>

      <h2 className="mt-4 text-2xl font-semibold text-gray-700 dark:text-gray-300">
        Page not found
      </h2>

      <p className="mt-2 text-gray-500 max-w-md">
        The page you are looking for doesn’t exist or has been moved.
      </p>

      <div className="mt-6 flex gap-3">
        <button
          onClick={() => navigate(-1)}
          className="rounded-md border px-4 py-2 text-sm hover:bg-gray-100 dark:hover:bg-slate-800"
        >
          Go Back
        </button>

        <button
          onClick={handleGoHome}
          className="rounded-md bg-blue-600 px-4 py-2 text-sm text-white hover:bg-blue-700"
        >
          Go Home
        </button>
      </div>
    </div>
  );
}
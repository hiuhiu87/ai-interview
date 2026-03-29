import { useNavigate } from "react-router-dom";

export default function Unauthorized() {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 dark:bg-slate-950 px-4 text-center">
      
      <h1 className="text-7xl font-bold text-blue-600">
        401
      </h1>

      <h2 className="mt-4 text-2xl font-semibold text-gray-800 dark:text-white">
        Unauthorized
      </h2>

      <p className="mt-2 max-w-md text-gray-500">
        You need to log in to access this page.
      </p>

      <div className="mt-6 flex gap-3">
        <button
          onClick={() => navigate("/login")}
          className="rounded-md bg-blue-600 px-4 py-2 text-sm text-white hover:bg-blue-700"
        >
          Go to Login
        </button>
      </div>
    </div>
  );
}
import { useNavigate } from "react-router-dom";

export default function Forbidden() {
  const navigate = useNavigate();

  const handleGoHome = () => {
    
  }

  return (
    <div className="flex w-screen min-h-screen flex-col items-center justify-center bg-gray-50 dark:bg-slate-950 px-4 text-center">
      
      <h1 className="text-7xl font-bold text-red-600">
        403
      </h1>

      <h2 className="mt-4 text-2xl font-semibold text-gray-800 dark:text-white">
        Access Denied
      </h2>

      <p className="mt-2 max-w-md text-gray-500">
        You don’t have permission to access this page.
      </p>

      <div className="mt-6 flex gap-3">
        <button
          onClick={() => navigate(-1)}
          className="rounded-md border px-4 py-2 text-sm hover:bg-gray-100 dark:hover:bg-slate-800"
        >
          Go Back
        </button>

        <button
          onClick={() => navigate("/")}
          className="rounded-md bg-red-600 px-4 py-2 text-sm text-white hover:bg-red-700"
        >
          Go Home
        </button>
      </div>
    </div>
  );
}
import { useNavigate } from "react-router-dom";

export default function ServerError() {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 dark:bg-slate-950 px-4 text-center">
      
      {/* Code */}
      <h1 className="text-7xl font-bold text-orange-600">
        500
      </h1>

      {/* Title */}
      <h2 className="mt-4 text-2xl font-semibold text-gray-800 dark:text-white">
        Something went wrong
      </h2>

      {/* Description */}
      <p className="mt-2 max-w-md text-gray-500">
        The server encountered an error. Please try again later.
      </p>

      {/* Actions */}
      <div className="mt-6 flex gap-3">
        <button
          onClick={() => window.location.reload()}
          className="rounded-md border px-4 py-2 text-sm hover:bg-gray-100 dark:hover:bg-slate-800"
        >
          Retry
        </button>

        <button
          onClick={() => navigate("/")}
          className="rounded-md bg-orange-600 px-4 py-2 text-sm text-white hover:bg-orange-700"
        >
          Go Home
        </button>
      </div>
    </div>
  );
}
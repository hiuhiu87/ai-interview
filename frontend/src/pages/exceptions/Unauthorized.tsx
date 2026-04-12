import { useNavigate } from "react-router-dom";

export default function Unauthorized() {
  const navigate = useNavigate();

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-white dark:bg-[#171717] px-4 text-center">
      
      <h1 className="text-7xl font-bold text-[#171717] dark:text-white">
        401
      </h1>

      <h2 className="mt-4 text-2xl font-semibold text-[#171717] dark:text-white">
        Unauthorized
      </h2>

      <p className="mt-2 max-w-md text-[rgba(23,23,23,0.62)] dark:text-white/70">
        You need to log in to access this page.
      </p>

      <div className="mt-6 flex gap-3">
        <button
          onClick={() => navigate("/login")}
          className="rounded-md bg-[#171717] px-4 py-2 text-sm text-white hover:bg-[#2a2a2a] dark:bg-white dark:text-[#171717]"
        >
          Go to Login
        </button>
      </div>
    </div>
  );
}

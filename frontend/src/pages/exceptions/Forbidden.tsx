import { useNavigate } from "react-router-dom";

export default function Forbidden() {
  const navigate = useNavigate();

  const handleGoHome = () => {
    
  }

  return (
    <div className="flex w-screen min-h-screen flex-col items-center justify-center bg-white dark:bg-[#171717] px-4 text-center">
      
      <h1 className="text-7xl font-bold text-[#171717] dark:text-white">
        403
      </h1>

      <h2 className="mt-4 text-2xl font-semibold text-[#171717] dark:text-white">
        Access Denied
      </h2>

      <p className="mt-2 max-w-md text-[rgba(23,23,23,0.62)] dark:text-white/70">
        You don’t have permission to access this page.
      </p>

      <div className="mt-6 flex gap-3">
        <button
          onClick={() => navigate(-1)}
          className="rounded-md border border-[rgba(23,23,23,0.12)] px-4 py-2 text-sm text-[#171717] hover:bg-[rgba(23,23,23,0.04)] dark:border-white/12 dark:text-white dark:hover:bg-white/8"
        >
          Go Back
        </button>

        <button
          onClick={() => navigate("/")}
          className="rounded-md bg-[#171717] px-4 py-2 text-sm text-white hover:bg-[#2a2a2a] dark:bg-white dark:text-[#171717]"
        >
          Go Home
        </button>
      </div>
    </div>
  );
}

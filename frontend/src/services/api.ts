import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
import { useLoadingStore } from "@/stores/useLoadingStore";

const BASE_URL = (import.meta.env.VITE_API_URL ||
  import.meta.env.VITE_API_BASE_URL ||
  "http://localhost:8080") as string;

const axiosClient = axios.create({
  baseURL: `${BASE_URL}/cms/api/v1`,
  timeout: 10_000,
});

// ###################################################################
// ############################# REQUEST #############################
axiosClient.interceptors.request.use((config) => {
  const { startLoading } = useLoadingStore.getState();
  startLoading();
  const token = useAuthStore.getState().accessToken;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ###################################################################
// ############################# RESPONSE ############################
let isRefreshing = false;

axiosClient.interceptors.response.use(
  (res) => {
    const { stopLoading } = useLoadingStore.getState();
    stopLoading();
    return res;
  },
  async (error) => {
    const { stopLoading } = useLoadingStore.getState();
    const { clearAuth, setToken } = useAuthStore.getState();
    const originalRequest = error.config;

    if (error.response?.status === 401 && !isRefreshing) {
      isRefreshing = true;
      try {
        const res = await axios.post(`${BASE_URL}/auth/refresh`);
        const newToken = res.data.accessToken;
        setToken(newToken);
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return axiosClient(originalRequest);
      } catch (e) {
        clearAuth();
      } finally {
        stopLoading();
        isRefreshing = false;
      }
    }

    stopLoading();
    return Promise.reject(error);
  },
);

export default axiosClient;

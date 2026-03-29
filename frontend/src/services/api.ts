import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
import { useLoadingStore } from "@/stores/useLoadingStore";

const BASE_URL = import.meta.env.VITE_API_URL as string;

const axiosClient = axios.create({
  baseURL: `${BASE_URL}/api/v1`,
  timeout: 10_000,
});

const { startLoading, stopLoading } = useLoadingStore();
const { clearAuth, setToken } = useAuthStore();

// ###################################################################
// ############################# REQUEST #############################
axiosClient.interceptors.request.use((config) => {
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
    stopLoading();
    return res;
  },
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !isRefreshing) {
      isRefreshing = true;
      try {
        const res = await axios.post("/auth/refresh");
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

    return Promise.reject(error);
  },
);

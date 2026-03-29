import { create } from "zustand";

interface LoadingState {
  loadingCount: number;
  startLoading: () => void;
  stopLoading: () => void;
}

export const useLoadingStore = create<LoadingState>((set) => ({
  loadingCount: 0,
  startLoading: () =>
    set((state) => ({ loadingCount: state.loadingCount + 1 })),
  stopLoading: () => set(() => ({ loadingCount: 0 })),
}));

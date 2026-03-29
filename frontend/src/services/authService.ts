// services/authService.ts
import type { LoginPayload, AuthResponse } from "../types";

// export const authService = {
//   login: async (payload: LoginPayload): Promise<AuthResponse> => {
//     const res = await fetch(`${BASE_URL}/auth/login`, {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(payload),
//     });
//     if (!res.ok) throw new Error("Login failed");
//     return res.json();
//   },
// };

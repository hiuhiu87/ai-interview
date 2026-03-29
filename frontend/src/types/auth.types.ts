export interface User {
  id: string;
  name: string;
  email: string;
  role: "ADMIN" | "INTERVIEWER";
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface AuthResponse {
  user: User;
  token: string;
}

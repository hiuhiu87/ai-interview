export interface DefaultResponse<T> {
  data: T;
  success: Boolean;
  message: String;
  timestamp: Number;
}

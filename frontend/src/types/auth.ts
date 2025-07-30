export interface User {
  userId: number;
  email: string;
  nickname: string;
  name: string;
  phoneNumber: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignUpRequest {
  email: string;
  password: string;
  confirmPassword: string;
  nickname: string;
  name: string;
  phoneNumber: string;
}

export interface UpdateProfileRequest {
  nickname: string;
  name: string;
  phoneNumber: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

export interface JwtResponse {
  accessToken: string;
  userInfo: User;
}

export interface ApiResponse<T> {
  status: number;
  message?: string;
  data?: T;
}
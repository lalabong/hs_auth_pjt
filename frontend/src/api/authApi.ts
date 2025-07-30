import { apiClient } from "./axiosInstance";
import {
  LoginRequest,
  SignUpRequest,
  UpdateProfileRequest,
  ChangePasswordRequest,
  JwtResponse,
  ApiResponse,
  User,
} from "../types/auth";

// 회원가입
export const signUp = async (
  data: SignUpRequest
): Promise<ApiResponse<User>> => {
  const response = await apiClient.post("/auth/signup", data);
  return response.data;
};

// 로그인
export const login = async (
  data: LoginRequest
): Promise<ApiResponse<JwtResponse>> => {
  const response = await apiClient.post("/auth/login", data);
  return response.data;
};

// 로그아웃
export const logout = async (): Promise<ApiResponse<void>> => {
  const response = await apiClient.post("/auth/logout");
  return response.data;
};

// 프로필 업데이트
export const updateProfile = async (
  data: UpdateProfileRequest
): Promise<ApiResponse<User>> => {
  const response = await apiClient.put("/auth/profile", data);
  return response.data;
};

// 비밀번호 변경
export const changePassword = async (
  data: ChangePasswordRequest
): Promise<ApiResponse<void>> => {
  const response = await apiClient.put("/auth/password", data);
  return response.data;
};

// 토큰 갱신
export const refreshToken = async (): Promise<ApiResponse<JwtResponse>> => {
  const response = await apiClient.post("/auth/refresh");
  return response.data;
};

// 비밀번호 재설정 요청
export const requestPasswordReset = async (email: string) => {
  const response = await apiClient.post("/auth/password/reset-request", {
    email,
  });
  return response.data;
};

// 새 비밀번호 설정
export const resetPassword = async (token: string, newPassword: string) => {
  const response = await apiClient.post("/auth/password/reset", {
    token,
    newPassword,
    confirmPassword: newPassword,
  });
  return response.data;
};

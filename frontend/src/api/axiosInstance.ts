import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

// Axios 인스턴스 생성
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// 요청 인터셉터: 액세스 토큰 자동 추가
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("auth-storage");
    if (token) {
      try {
        const authData = JSON.parse(token);
        if (authData.state?.accessToken) {
          config.headers.Authorization = `Bearer ${authData.state.accessToken}`;
        }
      } catch (error) {
        console.error("Failed to parse auth token:", error);
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터: 토큰 만료 처리
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      const requestUrl = error.config?.url || "";
      const currentPath = window.location.pathname;

      // 로그인/회원가입 요청이거나 이미 로그인 페이지에 있으면 리다이렉트하지 않음
      if (
        !requestUrl.includes("/auth/login") &&
        !requestUrl.includes("/auth/signup") &&
        currentPath !== "/login" &&
        currentPath !== "/signup"
      ) {
        // 토큰 만료 시 로그아웃 처리
        localStorage.removeItem("auth-storage");
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

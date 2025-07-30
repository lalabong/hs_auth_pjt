import { useState } from "react";

import { useLocation, Link, useNavigate } from "react-router-dom";

import { useAuthStore } from "../store/authStore";
import { login as apiLogin } from "../api/authApi";
import { LoginRequest } from "../types/auth";

const LoginPage = () => {
  const navigate = useNavigate();

  const location = useLocation();
  const from = location.state?.from?.pathname || "/dashboard";

  const { login, setLoading, setError, isLoading, error, clearError } =
    useAuthStore();

  const [formData, setFormData] = useState<LoginRequest>({
    email: "",
    password: "",
  });

  // 로그인 버튼 클릭
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    clearError();

    try {
      const response = await apiLogin(formData);
      if (response.data) {
        login(response.data.userInfo, response.data.accessToken);
        navigate(from, { replace: true });
      }
    } catch (error: any) {
      setError(error.response?.data?.message || "로그인에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // 이메일, 비밀번호 입력 변경
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <main className="max-w-md mx-auto py-8">
      <article className="bg-white rounded-lg shadow-md p-8">
        <header>
          <h1 className="text-2xl font-bold text-center mb-8">로그인</h1>
        </header>

        {error && (
          <section
            aria-label="에러 메시지"
            className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg"
          >
            <p role="alert" className="error-text">
              {error}
            </p>
          </section>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <fieldset>
            <legend className="sr-only">로그인 정보 입력</legend>
            <div className="space-y-6">
              <div>
                <label htmlFor="email" className="form-label">
                  이메일
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  className="form-input"
                  placeholder="이메일을 입력해주세요."
                  required
                  aria-required="true"
                />
              </div>

              <div>
                <label htmlFor="password" className="form-label">
                  비밀번호
                </label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleInputChange}
                  className="form-input"
                  placeholder="비밀번호를 입력해주세요. (6자 이상)"
                  required
                  aria-required="true"
                />
              </div>
            </div>
          </fieldset>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full btn-primary disabled:opacity-50"
            aria-busy={isLoading}
          >
            {isLoading ? "로그인 중..." : "로그인"}
          </button>
        </form>

        <nav className="text-center mt-6">
          <ul className="flex items-center justify-between">
            <li className="text-sm">
              <Link
                to="/signup"
                className="font-medium text-primary-600 hover:text-primary-700"
              >
                회원가입
              </Link>
            </li>
            <li className="text-sm">
              <Link
                to="/forgot-password"
                className="font-medium text-primary-600 hover:text-primary-700"
              >
                비밀번호 찾기
              </Link>
            </li>
          </ul>
        </nav>
      </article>
    </main>
  );
};

export default LoginPage;

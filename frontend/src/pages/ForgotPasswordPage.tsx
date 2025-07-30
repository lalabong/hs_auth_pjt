import React, { useState } from "react";

import { useNavigate } from "react-router-dom";

import { requestPasswordReset } from "../api/authApi";

const ForgotPasswordPage = () => {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // 비밀번호 재설정 이메일 받기
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    setIsLoading(true);

    try {
      await requestPasswordReset(email);
      setMessage("이메일이 발송되었습니다. 메일함을 확인해주세요.");
      setTimeout(() => {
        navigate("/login");
      }, 3000);
    } catch (error: any) {
      setMessage(
        error.response?.data?.message ||
          "비밀번호 재설정 요청 중 오류가 발생했습니다."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-2xl font-bold text-center mb-8">비밀번호 찾기</h1>
        <p className="text-sm text-gray-600 text-center mb-6">
          가입하신 이메일을 입력해주세요.
        </p>

        {message && (
          <div
            className={`mb-6 p-3 rounded-lg ${
              message.includes("오류")
                ? "bg-red-50 border border-red-200 text-red-700"
                : "bg-green-50 border border-green-200 text-green-700"
            }`}
          >
            <p>{message}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="email" className="form-label">
              이메일
            </label>
            <input
              id="email"
              name="email"
              type="email"
              required
              className="form-input"
              placeholder="이메일을 입력해주세요."
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full btn-primary disabled:opacity-50"
          >
            {isLoading ? "처리중..." : "비밀번호 재설정 이메일 받기"}
          </button>

          <div className="text-center">
            <button
              type="button"
              onClick={() => navigate("/login")}
              className="text-sm font-medium text-primary-600 hover:text-primary-700"
            >
              로그인 페이지로 돌아가기
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ForgotPasswordPage;

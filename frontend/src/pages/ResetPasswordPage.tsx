import React, { useState } from "react";

import { useNavigate, useSearchParams } from "react-router-dom";

import { resetPassword } from "../api/authApi";

const ResetPasswordPage = () => {
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const validatePassword = (password: string) => {
    return password.length >= 6;
  };

  // 비밀번호 재설정 버튼 클릭
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!token) {
      setMessage("유효하지 않은 토큰입니다.");
      return;
    }

    if (!validatePassword(newPassword)) {
      setMessage("비밀번호는 6자 이상이어야 합니다.");
      return;
    }

    if (newPassword !== confirmPassword) {
      setMessage("비밀번호가 일치하지 않습니다.");
      return;
    }

    setIsLoading(true);
    try {
      await resetPassword(token, newPassword);
      setMessage("비밀번호가 성공적으로 변경되었습니다.");
      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message;
      if (errorMessage?.includes("password.size")) {
        setMessage("비밀번호는 6자 이상이어야 합니다.");
      } else {
        setMessage(errorMessage || "비밀번호 재설정 중 오류가 발생했습니다.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  if (!token) {
    return (
      <div className="max-w-md mx-auto">
        <div className="bg-white rounded-lg shadow-md p-8">
          <div className="mb-6 p-3 rounded-lg bg-red-50 border border-red-200 text-red-700">
            <p>
              유효하지 않은 접근입니다. 비밀번호 재설정 링크를 다시
              확인해주세요.
            </p>
          </div>
          <div className="text-center">
            <button
              onClick={() => navigate("/forgot-password")}
              className="text-sm font-medium text-primary-600 hover:text-primary-700"
            >
              비밀번호 찾기 페이지로 이동
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-2xl font-bold text-center mb-8">
          새 비밀번호 설정
        </h1>
        <p className="text-sm text-gray-600 text-center mb-6">
          새로운 비밀번호를 입력해주세요.
        </p>

        {message && (
          <div
            className={`mb-6 p-3 rounded-lg ${
              message.includes("성공")
                ? "bg-green-50 border border-green-200 text-green-700"
                : "bg-red-50 border border-red-200 text-red-700"
            }`}
          >
            <p>{message}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="newPassword" className="form-label">
              새 비밀번호
            </label>
            <input
              id="newPassword"
              name="newPassword"
              type="password"
              required
              className="form-input"
              placeholder="새 비밀번호를 입력해주세요. (6자 이상)"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="confirmPassword" className="form-label">
              비밀번호 확인
            </label>
            <input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              required
              className="form-input"
              placeholder="새 비밀번호를 다시 입력해주세요."
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full btn-primary disabled:opacity-50"
          >
            {isLoading ? "처리중..." : "비밀번호 변경"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default ResetPasswordPage;

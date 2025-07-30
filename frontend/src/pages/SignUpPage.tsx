import { useState, FormEvent } from "react";

import { Link, useNavigate } from "react-router-dom";

import { useAuthStore } from "../store/authStore";
import { signUp } from "../api/authApi";
import { SignUpRequest } from "../types/auth";

function SignUpPage() {
  const navigate = useNavigate();

  const { setLoading, setError, isLoading, error, clearError } = useAuthStore();

  const [formData, setFormData] = useState<SignUpRequest>({
    email: "",
    password: "",
    confirmPassword: "",
    nickname: "",
    name: "",
    phoneNumber: "",  
  });

  // 회원가입 버튼 클릭
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    clearError();

    if (formData.password !== formData.confirmPassword) {
      setError("비밀번호가 일치하지 않습니다.");
      setLoading(false);
      return;
    }

    // 전화번호 형식 검증
    const phoneRegex = /^01[0-9]-\d{4}-\d{4}$/;
    if (!phoneRegex.test(formData.phoneNumber)) {
      setError("전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)");
      setLoading(false);
      return;
    }

    try {
      await signUp(formData);
      alert("회원가입이 완료되었습니다. 로그인해주세요.");
      navigate("/login");
    } catch (error: any) {
      setError(error.response?.data?.message || "회원가입에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // 이메일, 이름, 닉네임, 전화번호, 비밀번호, 비밀번호 확인 입력 변경  
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // 전화번호 입력 변경
  const handlePhoneNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value.replace(/[^0-9]/g, "");

    if (value.length <= 11) {
      if (value.length >= 7) {
        value = value.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
      } else if (value.length >= 3) {
        value = value.replace(/(\d{3})(\d+)/, "$1-$2");
      }
    }

    setFormData((prev) => ({ ...prev, phoneNumber: value }));
  };

  return (
    <main className="max-w-md mx-auto py-8">
      <article className="bg-white rounded-lg shadow-md p-8">
        <header>
          <h1 className="text-2xl font-bold text-center mb-8">회원가입</h1>
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
            <legend className="sr-only">회원 정보 입력</legend>
            <div className="space-y-6">
              <div>
                <label htmlFor="email" className="form-label">
                  이메일 <span aria-label="필수 입력">*</span>
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  placeholder="이메일을 입력해주세요."
                  className="form-input"
                  required
                  aria-required="true"
                />
              </div>

              <div>
                <label htmlFor="name" className="form-label">
                  이름 <span aria-label="필수 입력">*</span>
                </label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  placeholder="이름을 입력해주세요."
                  className="form-input"
                  required
                  aria-required="true"
                />
              </div>

              <div>
                <label htmlFor="nickname" className="form-label">
                  닉네임 <span aria-label="필수 입력">*</span>
                </label>
                <input
                  type="text"
                  id="nickname"
                  name="nickname"
                  value={formData.nickname}
                  onChange={handleInputChange}
                  placeholder="닉네임을 입력해주세요. (2자 이상 20자 이하)"
                  className="form-input"
                  required
                  aria-required="true"
                  minLength={2}
                  maxLength={20}
                />
              </div>

              <div>
                <label htmlFor="phoneNumber" className="form-label">
                  전화번호 <span aria-label="필수 입력">*</span>
                </label>
                <input
                  type="tel"
                  id="phoneNumber"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handlePhoneNumberChange}
                  className="form-input"
                  placeholder="전화번호를 입력해주세요. (예: 010-1234-5678)"
                  maxLength={13}
                  required
                  aria-required="true"
                  pattern="^01[0-9]-\d{4}-\d{4}$"
                />
              </div>

              <div>
                <label htmlFor="password" className="form-label">
                  비밀번호 <span aria-label="필수 입력">*</span>
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
                  minLength={6}
                />
              </div>

              <div>
                <label htmlFor="confirmPassword" className="form-label">
                  비밀번호 확인 <span aria-label="필수 입력">*</span>
                </label>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  className="form-input"
                  placeholder="비밀번호를 다시 입력해주세요. (6자 이상)"
                  required
                  aria-required="true"
                  minLength={6}
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
            {isLoading ? "가입 중..." : "회원가입"}
          </button>
        </form>

        <footer className="text-center mt-6">
          <p className="text-sm text-gray-600">
            이미 계정이 있으신가요?{" "}
            <Link
              to="/login"
              className="text-primary-600 hover:text-primary-700"
            >
              로그인
            </Link>
          </p>
        </footer>
      </article>
    </main>
  );
}

export default SignUpPage;

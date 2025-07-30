import { useState, FormEvent } from "react";

import { useAuthStore } from "../store/authStore";
import { updateProfile, changePassword } from "../api/authApi";
import { UpdateProfileRequest, ChangePasswordRequest } from "../types/auth";

function ProfilePage() {
  const { user, setUser, setLoading, isLoading } = useAuthStore();

  const [profileData, setProfileData] = useState<UpdateProfileRequest>({
    nickname: user?.nickname || "",
    name: user?.name || "",
    phoneNumber: user?.phoneNumber || "",
  });

  const [passwordData, setPasswordData] = useState<ChangePasswordRequest>({
    currentPassword: "",
    newPassword: "",
    confirmNewPassword: "",
  });

  const [message, setMessage] = useState<{
    type: "success" | "error";
    text: string;
  } | null>(null);

  // 프로필 업데이트
  const handleProfileSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    // 전화번호 형식 검증
    const phoneRegex = /^01[0-9]-\d{4}-\d{4}$/;
    if (!phoneRegex.test(profileData.phoneNumber)) {
      setMessage({
        type: "error",
        text: "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)",
      });
      setLoading(false);
      return;
    }

    try {
      const response = await updateProfile(profileData);
      if (response.data) {
        setUser(response.data);
        setMessage({ type: "success", text: "프로필이 업데이트되었습니다." });
      }
    } catch (error: any) {
      setMessage({
        type: "error",
        text:
          error.response?.data?.message || "프로필 업데이트에 실패했습니다.",
      });
    } finally {
      setLoading(false);
    }
  };

  // 비밀번호 변경
  const handlePasswordSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    if (passwordData.newPassword !== passwordData.confirmNewPassword) {
      setMessage({ type: "error", text: "새 비밀번호가 일치하지 않습니다." });
      setLoading(false);
      return;
    }

    try {
      await changePassword(passwordData);
      alert("비밀번호가 성공적으로 변경되었습니다.");
      setPasswordData({
        currentPassword: "",
        newPassword: "",
        confirmNewPassword: "",
      });
    } catch (error: any) {
      setMessage({
        type: "error",
        text: error.response?.data?.message || "비밀번호 변경에 실패했습니다.",
      });
    } finally {
      setLoading(false);
    }
  };

  // 프로필 정보 입력 변경
  const handleProfileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfileData((prev) => ({ ...prev, [name]: value }));
  };

  // 비밀번호 입력 변경
  const handlePasswordInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = e.target;
    setPasswordData((prev) => ({ ...prev, [name]: value }));
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

    setProfileData((prev) => ({ ...prev, phoneNumber: value }));
  };

  return (
    <main className="max-w-2xl mx-auto py-8">
      <h1 className="text-3xl font-bold mb-8">마이페이지</h1>

      {message && (
        <section
          aria-label={
            message.type === "success" ? "성공 메시지" : "에러 메시지"
          }
          className={`mb-6 p-4 rounded-lg ${
            message.type === "success"
              ? "bg-green-50 border border-green-200 text-green-700"
              : "bg-red-50 border border-red-200 text-red-700"
          }`}
        >
          <p role="alert">{message.text}</p>
        </section>
      )}

      <div className="space-y-8">
        <section className="bg-white rounded-lg shadow-md p-6">
          <header className="flex items-baseline justify-between mb-6">
            <h2 className="text-xl font-semibold">프로필 정보</h2>
            {user?.updatedAt && (
              <time dateTime={user.updatedAt} className="text-xs text-gray-500">
                마지막 업데이트:{" "}
                {new Date(user.updatedAt).toLocaleDateString("ko-KR")}
              </time>
            )}
          </header>

          <form
            onSubmit={handleProfileSubmit}
            className="space-y-4 flex flex-col gap-2"
          >
            <fieldset>
              <legend className="sr-only">기본 프로필 정보</legend>
              <div className="space-y-4">
                <div>
                  <label htmlFor="email" className="form-label">
                    이메일
                  </label>
                  <input
                    type="email"
                    id="email"
                    value={user?.email || ""}
                    className="form-input bg-gray-50"
                    disabled
                    aria-readonly="true"
                  />
                  <p className="text-xs text-gray-500 mt-1">
                    이메일은 변경할 수 없습니다.
                  </p>
                </div>

                <div>
                  <label htmlFor="name" className="form-label">
                    이름 <span aria-label="필수 입력">*</span>
                  </label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={profileData.name}
                    onChange={handleProfileInputChange}
                    className="form-input"
                    placeholder="이름을 입력해주세요."
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
                    value={profileData.nickname}
                    onChange={handleProfileInputChange}
                    className="form-input"
                    placeholder="닉네임을 입력해주세요. (2자 이상 20자 이하)"
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
                    value={profileData.phoneNumber}
                    onChange={handlePhoneNumberChange}
                    className="form-input"
                    placeholder="전화번호를 입력해주세요.(예: 010-1234-5678)"
                    maxLength={13}
                    required
                    aria-required="true"
                    pattern="^01[0-9]-\d{4}-\d{4}$"
                  />
                </div>
              </div>
            </fieldset>

            <button
              type="submit"
              disabled={isLoading}
              className="btn-primary disabled:opacity-50"
              aria-busy={isLoading}
            >
              {isLoading ? "업데이트 중..." : "프로필 업데이트"}
            </button>
          </form>
        </section>

        <section className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-xl font-semibold mb-6">비밀번호 변경</h2>

          <form
            onSubmit={handlePasswordSubmit}
            className="space-y-4 flex flex-col gap-2"
          >
            <fieldset>
              <legend className="sr-only">비밀번호 변경</legend>
              <div className="space-y-4">
                <div>
                  <label htmlFor="currentPassword" className="form-label">
                    현재 비밀번호 <span aria-label="필수 입력">*</span>
                  </label>
                  <input
                    type="password"
                    id="currentPassword"
                    name="currentPassword"
                    placeholder="현재 비밀번호를 입력해주세요.(6자 이상)"
                    value={passwordData.currentPassword}
                    onChange={handlePasswordInputChange}
                    className="form-input"
                    required
                    aria-required="true"
                    minLength={6}
                  />
                </div>

                <div>
                  <label htmlFor="newPassword" className="form-label">
                    새 비밀번호 <span aria-label="필수 입력">*</span>
                  </label>
                  <input
                    type="password"
                    id="newPassword"
                    name="newPassword"
                    placeholder="새 비밀번호를 입력해주세요.(6자 이상)"
                    value={passwordData.newPassword}
                    onChange={handlePasswordInputChange}
                    className="form-input"
                    required
                    aria-required="true"
                    minLength={6}
                  />
                </div>

                <div>
                  <label htmlFor="confirmNewPassword" className="form-label">
                    새 비밀번호 확인 <span aria-label="필수 입력">*</span>
                  </label>
                  <input
                    type="password"
                    id="confirmNewPassword"
                    name="confirmNewPassword"
                    placeholder="새 비밀번호를 다시 입력해주세요.(6자 이상)"
                    value={passwordData.confirmNewPassword}
                    onChange={handlePasswordInputChange}
                    className="form-input"
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
              className="btn-primary disabled:opacity-50"
              aria-busy={isLoading}
            >
              {isLoading ? "변경 중..." : "비밀번호 변경"}
            </button>
          </form>
        </section>
      </div>
    </main>
  );
}

export default ProfilePage;

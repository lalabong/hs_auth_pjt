package com.hs.auth.constants;

public final class AppConstants {

    private AppConstants() {
    }

    // ===== JWT 관련 상수 =====
    public static final class JWT {
        public static final String TOKEN_TYPE = "Bearer";
        public static final long REFRESH_TOKEN_EXPIRATION_MS = 604800000L; // 7일

        private JWT() {
        }
    }

    // ===== 보안 관련 상수 =====
    public static final class Security {
        public static final int BCRYPT_STRENGTH = 12;
        public static final int JWT_SECRET_MIN_LENGTH = 32;

        private Security() {
        }
    }

    // ===== 입력 검증 상수 =====
    public static final class Validation {
        // 비밀번호 규칙
        public static final int PASSWORD_MIN_LENGTH = 6;
        public static final int PASSWORD_MAX_LENGTH = 20;

        // 닉네임 규칙
        public static final int NICKNAME_MIN_LENGTH = 2;
        public static final int NICKNAME_MAX_LENGTH = 20;

        // 이름 규칙
        public static final int NAME_MIN_LENGTH = 2;
        public static final int NAME_MAX_LENGTH = 10;

        // 전화번호 규칙
        public static final String PHONE_NUMBER_PATTERN = "^01[0-9]-\\d{4}-\\d{4}$";
        public static final int PHONE_NUMBER_MAX_LENGTH = 13;

        // 이메일 최대 길이
        public static final int EMAIL_MAX_LENGTH = 100;

        private Validation() {
        }
    }

    // ===== 메시지 상수 =====
    public static final class Messages {
        // 성공 메시지
        public static final String SIGNUP_SUCCESS = "회원가입이 완료되었습니다.";
        public static final String PROFILE_UPDATE_SUCCESS = "프로필이 수정되었습니다.";
        public static final String PASSWORD_CHANGE_SUCCESS = "비밀번호가 변경되었습니다.";
        public static final String LOGOUT_SUCCESS = "로그아웃이 완료되었습니다.";

        // 에러 메시지
        public static final String PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다.";
        public static final String NEW_PASSWORD_MISMATCH = "새 비밀번호가 일치하지 않습니다.";
        public static final String CURRENT_PASSWORD_INVALID = "현재 비밀번호가 일치하지 않습니다.";
        public static final String LOGIN_FAILED = "이메일 또는 비밀번호가 올바르지 않습니다.";
        public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다: ";
        public static final String UNAUTHORIZED_USER = "인증되지 않은 사용자입니다.";
        public static final String INVALID_TOKEN = "유효하지 않거나 만료된 토큰입니다.";

        // JWT 관련 메시지
        public static final String JWT_TOKEN_MISSING = "JWT 토큰이 없습니다.";
        public static final String JWT_TOKEN_INVALID = "JWT 토큰이 잘못되었습니다.";
        public static final String JWT_TOKEN_EXPIRED = "만료된 JWT 토큰입니다.";
        public static final String JWT_TOKEN_UNSUPPORTED = "지원되지 않는 JWT 토큰입니다.";
        public static final String JWT_SIGNATURE_INVALID = "잘못된 JWT 서명입니다.";
        public static final String REFRESH_TOKEN_MISSING = "Refresh token이 없습니다.";
        public static final String REFRESH_TOKEN_INVALID = "유효하지 않은 refresh token입니다.";

        // 중복 에러 메시지
        public static final String DUPLICATE_EMAIL = "이미 사용 중인 이메일입니다: ";
        public static final String DUPLICATE_NICKNAME = "이미 사용 중인 닉네임입니다: ";
        public static final String DUPLICATE_PHONE_NUMBER = "이미 사용 중인 전화번호입니다: ";

        // API 응답 메시지
        public static final String VALIDATION_FAILED = "입력 데이터가 올바르지 않습니다.";
        public static final String ACCESS_DENIED = "접근 권한이 없습니다.";
        public static final String INTERNAL_SERVER_ERROR = "서버 내부 오류가 발생했습니다.";

        private Messages() {
        }
    }

    // ===== URL 패턴 상수 =====
    public static final class Urls {
        public static final String H2_CONSOLE_PATTERN = "/h2-console/**";
        public static final String AUTH_SIGNUP = "/auth/signup";
        public static final String AUTH_LOGIN = "/auth/login";
        public static final String AUTH_REFRESH = "/auth/refresh";
        public static final String AUTH_PASSWORD_RESET_REQUEST = "/auth/password/reset-request";
        public static final String AUTH_PASSWORD_RESET = "/auth/password/reset";

        private Urls() {
        }
    }

    // ===== HTTP 관련 상수 =====
    public static final class Http {
        public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String USER_ID_CLAIM = "userId";

        private Http() {
        }
    }
}
package com.hs.auth.util;

import com.hs.auth.constants.AppConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {

    private CookieUtil() {
        // 유틸리티 클래스는 인스턴스화 방지
    }

    // Refresh Token을 HTTP-only Cookie로 설정
    public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(AppConstants.Http.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 개발환경에서는 false, 운영환경에서는 true
        refreshTokenCookie.setPath("/auth");
        refreshTokenCookie.setMaxAge((int) (AppConstants.JWT.REFRESH_TOKEN_EXPIRATION_MS / 1000));
        response.addCookie(refreshTokenCookie);

        log.debug("Refresh Token Cookie 설정 완료");
    }

    // Refresh Token Cookie 삭제
    public static void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(AppConstants.Http.REFRESH_TOKEN_COOKIE_NAME, null);
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/auth");
        refreshTokenCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(refreshTokenCookie);

        log.debug("Refresh Token Cookie 삭제 완료");
    }

    // Cookie에서 Refresh Token 추출
    public static String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AppConstants.Http.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    log.debug("Cookie에서 Refresh Token 추출 성공");
                    return cookie.getValue();
                }
            }
        }

        log.debug("Cookie에서 Refresh Token을 찾을 수 없음");
        return null;
    }

    // 특정 이름의 Cookie 값 추출
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Cookie 생성 (범용)
    public static Cookie createCookie(String name, String value, int maxAge, String path, boolean httpOnly,
            boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        return cookie;
    }
}
package com.hs.auth.common.exception;

import com.hs.auth.constants.AppConstants;

// 인증 관련 예외
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AuthenticationException loginFailed() {
        return new AuthenticationException(AppConstants.Messages.LOGIN_FAILED);
    }

    public static AuthenticationException passwordMismatch() {
        return new AuthenticationException(AppConstants.Messages.PASSWORD_MISMATCH);
    }

    public static AuthenticationException newPasswordMismatch() {
        return new AuthenticationException(AppConstants.Messages.NEW_PASSWORD_MISMATCH);
    }

    public static AuthenticationException currentPasswordInvalid() {
        return new AuthenticationException(AppConstants.Messages.CURRENT_PASSWORD_INVALID);
    }
}
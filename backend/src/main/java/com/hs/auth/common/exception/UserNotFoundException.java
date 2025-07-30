package com.hs.auth.common.exception;

import com.hs.auth.constants.AppConstants;

// 사용자를 찾을 수 없을 때 발생하는 예외
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException(AppConstants.Messages.USER_NOT_FOUND + email);
    }

    public static UserNotFoundException byId(Long userId) {
        return new UserNotFoundException(AppConstants.Messages.USER_NOT_FOUND + userId);
    }
}
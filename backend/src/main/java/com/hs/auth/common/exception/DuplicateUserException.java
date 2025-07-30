package com.hs.auth.common.exception;

import com.hs.auth.constants.AppConstants;

// 중복된 사용자 정보로 인해 발생하는 예외
public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DuplicateUserException email(String email) {
        return new DuplicateUserException(AppConstants.Messages.DUPLICATE_EMAIL + email);
    }

    public static DuplicateUserException nickname(String nickname) {
        return new DuplicateUserException(AppConstants.Messages.DUPLICATE_NICKNAME + nickname);
    }

    public static DuplicateUserException phoneNumber(String phoneNumber) {
        return new DuplicateUserException(AppConstants.Messages.DUPLICATE_PHONE_NUMBER + phoneNumber);
    }
}
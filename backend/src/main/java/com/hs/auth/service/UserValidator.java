package com.hs.auth.service;

import com.hs.auth.common.exception.AuthenticationException;
import com.hs.auth.common.exception.DuplicateUserException;
import com.hs.auth.dto.request.SignUpRequest;
import com.hs.auth.dto.request.UpdateProfileRequest;
import com.hs.auth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 사용자 관련 검증 로직
@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;

    // 비밀번호 확인 검증
    public void validatePasswordConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw AuthenticationException.passwordMismatch();
        }
    }

    // 로그인 검증
    public User validateLogin(String email, String password) {
        User user = userQueryService.findByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw AuthenticationException.loginFailed();
        }

        return user;
    }

    // 현재 비밀번호 검증
    public void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw AuthenticationException.currentPasswordInvalid();
        }
    }

    // 중복 사용자 검증 (회원가입 시)
    public void validateDuplicateUser(SignUpRequest request) {
        if (userQueryService.existsByEmail(request.getEmail())) {
            throw DuplicateUserException.email(request.getEmail());
        }
        if (userQueryService.existsByNickname(request.getNickname())) {
            throw DuplicateUserException.nickname(request.getNickname());
        }
    }

    // 중복 검증 (프로필 업데이트 시 - 본인 제외)
    public void validateDuplicateForUpdate(User currentUser, UpdateProfileRequest request) {
        // 닉네임 중복 체크 (본인 제외)
        if (!currentUser.getNickname().equals(request.getNickname()) &&
                userQueryService.existsByNickname(request.getNickname())) {
            throw DuplicateUserException.nickname(request.getNickname());
        }

    }
}
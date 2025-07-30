package com.hs.auth.controller;

import com.hs.auth.common.dto.ApiResponse;
import com.hs.auth.dto.request.ChangePasswordRequest;
import com.hs.auth.dto.request.LoginRequest;
import com.hs.auth.dto.request.SignUpRequest;
import com.hs.auth.dto.request.UpdateProfileRequest;
import com.hs.auth.dto.request.ResetPasswordRequest;
import com.hs.auth.dto.request.NewPasswordRequest;
import com.hs.auth.dto.response.JwtResponse;
import com.hs.auth.dto.response.UserResponse;
import com.hs.auth.entity.User;
import com.hs.auth.mapper.UserMapper;
import com.hs.auth.service.UserService;
import com.hs.auth.constants.AppConstants;
import com.hs.auth.util.CookieUtil;
import com.hs.auth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        User user = userService.signUp(request);
        UserResponse userResponse = userMapper.toResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(AppConstants.Messages.SIGNUP_SUCCESS, userResponse));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        JwtResponse jwtResponse = userService.login(request);

        // Refresh Token을 HTTP-only Cookie로 설정
        CookieUtil.setRefreshTokenCookie(response, jwtResponse.getRefreshToken());

        // 응답에서는 refreshToken 제거 (Cookie로만 전송)
        JwtResponse responseDto = JwtResponse.builder()
                .accessToken(jwtResponse.getAccessToken())
                .userInfo(jwtResponse.getUserInfo())
                .build();

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    // 프로필 수정
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request,
            HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getCurrentUserIdFromRequest(httpRequest);
        User user = userService.updateProfile(userId, request);
        UserResponse userResponse = userMapper.toResponse(user);

        return ResponseEntity.ok(ApiResponse.success(AppConstants.Messages.PROFILE_UPDATE_SUCCESS, userResponse));
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        Long userId = jwtUtil.getCurrentUserIdFromRequest(httpRequest);
        userService.changePassword(userId, request);

        return ResponseEntity.ok(ApiResponse.success(AppConstants.Messages.PASSWORD_CHANGE_SUCCESS, null));
    }

    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = CookieUtil.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new IllegalArgumentException(AppConstants.Messages.REFRESH_TOKEN_MISSING);
        }

        JwtResponse jwtResponse = userService.refreshToken(refreshToken);

        // 새 Refresh Token을 Cookie로 설정
        CookieUtil.setRefreshTokenCookie(response, jwtResponse.getRefreshToken());

        // 응답에서는 refreshToken 제거 (Cookie로만 전송)
        JwtResponse responseDto = JwtResponse.builder()
                .accessToken(jwtResponse.getAccessToken())
                .userInfo(jwtResponse.getUserInfo())
                .build();

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        // Refresh Token Cookie 삭제
        CookieUtil.clearRefreshTokenCookie(response);

        log.info("로그아웃 성공");
        return ResponseEntity.ok(ApiResponse.success(AppConstants.Messages.LOGOUT_SUCCESS, null));
    }

    // 비밀번호 재설정 요청
    @PostMapping("/password/reset-request")
    public ApiResponse<?> requestPasswordReset(@Valid @RequestBody ResetPasswordRequest request) {
        userService.requestPasswordReset(request);
        return ApiResponse.success("비밀번호 재설정 이메일이 발송되었습니다.");
    }

    // 비밀번호 재설정
    @PostMapping("/password/reset")
    public ApiResponse<?> resetPassword(@Valid @RequestBody NewPasswordRequest request) {
        userService.resetPassword(request);
        return ApiResponse.success("비밀번호가 성공적으로 재설정되었습니다.");
    }
}
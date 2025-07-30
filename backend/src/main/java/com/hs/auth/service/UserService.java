package com.hs.auth.service;

import com.hs.auth.constants.AppConstants;
import com.hs.auth.dto.request.ChangePasswordRequest;
import com.hs.auth.dto.request.LoginRequest;
import com.hs.auth.dto.request.SignUpRequest;
import com.hs.auth.dto.request.UpdateProfileRequest;
import com.hs.auth.dto.request.ResetPasswordRequest;
import com.hs.auth.dto.request.NewPasswordRequest;
import com.hs.auth.dto.response.JwtResponse;
import com.hs.auth.entity.User;
import com.hs.auth.entity.PasswordResetToken;
import com.hs.auth.repository.PasswordResetTokenRepository;
import com.hs.auth.mapper.UserMapper;
import com.hs.auth.repository.UserRepository;
import com.hs.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// 사용자 관련 서비스
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final UserValidator userValidator;
    private final UserQueryService userQueryService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    // 회원가입
    @Transactional
    public User signUp(SignUpRequest request) {
        // 비밀번호 확인 검증
        userValidator.validatePasswordConfirmation(request.getPassword(), request.getConfirmPassword());

        // 중복 검증
        userValidator.validateDuplicateUser(request);

        // MapStruct로 기본 필드 매핑
        User user = userMapper.toEntity(request);

        // 비밀번호 암호화 후 설정
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.updatePassword(encodedPassword);

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: {} (ID: {})", savedUser.getEmail(), savedUser.getUserId());

        return savedUser;
    }

    // 로그인
    public JwtResponse login(LoginRequest request) {
        log.info("로그인 요청: {}", request.getEmail());

        // 사용자 조회 및 비밀번호 검증
        User user = userValidator.validateLogin(request.getEmail(), request.getPassword());

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), user.getEmail());

        log.info("로그인 성공: {} (ID: {})", user.getEmail(), user.getUserId());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userInfo(userMapper.toResponse(user))
                .build();
    }

    // 토큰 갱신
    public JwtResponse refreshToken(String refreshToken) {
        // Refresh token 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException(AppConstants.Messages.REFRESH_TOKEN_INVALID);
        }

        // 토큰에서 사용자 정보 추출
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String email = jwtUtil.getUsernameFromToken(refreshToken);

        // 새 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(userId, email);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, email);

        // 사용자 정보 조회
        User user = userQueryService.findById(userId);

        log.info("토큰 갱신 성공: {} (ID: {})", email, userId);

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userInfo(userMapper.toResponse(user))
                .build();
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 새 비밀번호 확인 검증
        userValidator.validatePasswordConfirmation(request.getNewPassword(), request.getConfirmNewPassword());

        User user = userQueryService.findById(userId);

        // 현재 비밀번호 확인
        userValidator.validateCurrentPassword(user, request.getCurrentPassword());

        // 새 비밀번호 암호화 및 저장
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.updatePassword(encodedNewPassword);

        log.info("비밀번호 변경 완료: 사용자 ID {}", userId);
    }

    // 프로필 업데이트
    @Transactional
    public User updateProfile(Long userId, UpdateProfileRequest request) {

        User user = userQueryService.findById(userId);

        // 중복 검증 (본인 제외)
        userValidator.validateDuplicateForUpdate(user, request);

        user.updateProfile(request.getNickname(), request.getName(), request.getPhoneNumber());

        User savedUser = userRepository.saveAndFlush(user);

        log.info("프로필 업데이트 완료: 사용자 ID {}", userId);
        return savedUser;
    }

    // 사용자 조회 (ID)
    public User findById(Long userId) {
        return userQueryService.findById(userId);
    }

    // 사용자 조회 (이메일)
    public User findByEmail(String email) {
        return userQueryService.findByEmail(email);
    }

    // 비밀번호 재설정 요청
    @Transactional
    public void requestPasswordReset(ResetPasswordRequest request) {
        String email = request.getEmail();
        User user = userQueryService.findByEmail(email);

        // 30분 동안 유효한 임시 토큰 생성
        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime tokenExpiry = LocalDateTime.now().plusMinutes(30);

        // 새로운 비밀번호 재설정 토큰 생성
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(tokenValue)
                .user(user)
                .expiryDate(tokenExpiry)
                .build();

        user.addPasswordResetToken(resetToken);
        userRepository.save(user);

        // 이메일 발송 (비동기)
        emailService.sendPasswordResetEmail(email, tokenValue)
                .thenAccept(success -> {
                    if (success) {
                        log.info("비밀번호 재설정 요청 처리 완료: {}", email);
                    } else {
                        log.warn("비밀번호 재설정 이메일 발송은 실패했지만, 토큰은 생성됨: {}", email);
                    }
                });
    }

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(NewPasswordRequest request) {
        // 비밀번호 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException(AppConstants.Messages.PASSWORD_MISMATCH);
        }

        // 유효한 토큰 찾기
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findValidToken(request.getToken(), LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException(AppConstants.Messages.INVALID_TOKEN));

        User user = resetToken.getUser();

        // 비밀번호 업데이트
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.updatePassword(encodedPassword);

        // 토큰 사용 완료 표시
        resetToken.markAsUsed();

        userRepository.save(user);
        log.info("비밀번호 재설정 완료: {}", user.getEmail());
    }
}
package com.hs.auth.scheduler;

import com.hs.auth.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 */1 * * *") // 매 시간마다 실행
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        log.info("만료된 비밀번호 재설정 토큰 정리 시작: {}", now);

        try {
            passwordResetTokenRepository.deleteByExpiryDateBeforeAndUsedAtIsNull(now);
            log.info("만료된 비밀번호 재설정 토큰 정리 완료");
        } catch (Exception e) {
            log.error("만료된 비밀번호 재설정 토큰 정리 중 오류 발생", e);
        }
    }
}
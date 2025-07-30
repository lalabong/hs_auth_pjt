package com.hs.auth.repository;

import com.hs.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.token = :token AND t.expiryDate > :now AND t.usedAt IS NULL")
    Optional<PasswordResetToken> findValidToken(String token, LocalDateTime now);

    void deleteByExpiryDateBeforeAndUsedAtIsNull(LocalDateTime date);
}
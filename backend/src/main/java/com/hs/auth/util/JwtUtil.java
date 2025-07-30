package com.hs.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hs.auth.constants.AppConstants;

import javax.crypto.SecretKey;
import java.util.Date;

// JWT 토큰 유틸리티 클래스
@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpiration,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpiration = jwtExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // JWT 액세스 토큰 생성 (사용자 ID와 이메일 포함)
    public String generateAccessToken(Long userId, String email) {
        return generateTokenWithUserId(userId, email, jwtExpiration);
    }

    // JWT 리프레시 토큰 생성 (사용자 ID와 이메일 포함)
    public String generateRefreshToken(Long userId, String email) {
        return generateTokenWithUserId(userId, email, refreshTokenExpiration);
    }

    // JWT 토큰 생성 (사용자 ID 포함)
    private String generateTokenWithUserId(Long userId, String email, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email) // subject는 이메일로 유지 (호환성)
                .claim(AppConstants.Http.USER_ID_CLAIM, userId) // 커스텀 claim으로 사용자 ID 추가
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // JWT 토큰에서 사용자명 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // JWT 토큰에서 사용자 ID 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get(AppConstants.Http.USER_ID_CLAIM, Long.class);
    }

    // HTTP 요청에서 JWT 토큰 추출
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstants.Http.AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(AppConstants.Http.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // HTTP 요청에서 현재 사용자 ID 추출
    public Long getCurrentUserIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new SecurityException(AppConstants.Messages.JWT_TOKEN_MISSING);
        }
        return getUserIdFromToken(token);
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error(AppConstants.Messages.JWT_SIGNATURE_INVALID);
        } catch (ExpiredJwtException e) {
            log.error(AppConstants.Messages.JWT_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error(AppConstants.Messages.JWT_TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.error(AppConstants.Messages.JWT_TOKEN_INVALID);
        }
        return false;
    }
}
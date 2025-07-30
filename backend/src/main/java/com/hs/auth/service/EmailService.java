package com.hs.auth.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Async("emailExecutor")
    public CompletableFuture<Boolean> sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("비밀번호 재설정 링크");

            String resetLink = String.format("%s/reset-password?token=%s", frontendUrl, token);
            String htmlContent = String.format("""
                    <div style="font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif;">
                        <h2 style="color: #2C3E50;">비밀번호 재설정</h2>
                        <p style="color: #34495E;">안녕하세요.</p>
                        <p style="color: #34495E;">비밀번호 재설정 링크가 생성되었습니다.</p>
                        <div style="margin: 30px 0;">
                            <a href="%s"
                               style="background-color: #3498DB;
                                      color: white;
                                      padding: 10px 20px;
                                      text-decoration: none;
                                      border-radius: 5px;
                                      display: inline-block;">
                                비밀번호 재설정하러가기
                            </a>
                        </div>
                        <p style="color: #7F8C8D; font-size: 0.9em;">이 링크는 30분 동안 유효합니다.</p>
                        <p style="color: #7F8C8D; font-size: 0.9em;">비밀번호 재설정을 요청하지 않았다면 이 이메일을 무시하세요.</p>
                    </div>
                    """, resetLink);

            helper.setText(htmlContent, true);

            emailSender.send(message);
            log.info("비밀번호 재설정 이메일 발송 완료: {}", to);
            return CompletableFuture.completedFuture(true);
        } catch (MessagingException e) {
            log.error("비밀번호 재설정 이메일 발송 실패: {}", to, e);
            return CompletableFuture.completedFuture(false);
        }
    }
}
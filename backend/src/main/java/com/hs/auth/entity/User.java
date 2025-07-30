package com.hs.auth.entity;

import com.hs.auth.constants.AppConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, length = AppConstants.Validation.EMAIL_MAX_LENGTH)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = AppConstants.Validation.NICKNAME_MAX_LENGTH)
    private String nickname;

    @Column(nullable = false, length = AppConstants.Validation.NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false, length = AppConstants.Validation.PHONE_NUMBER_MAX_LENGTH)
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 비밀번호 업데이트
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    // 프로필 업데이트
    public void updateProfile(String nickname, String name, String phoneNumber) {
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void addPasswordResetToken(PasswordResetToken token) {
        this.passwordResetTokens.add(token);
    }
}
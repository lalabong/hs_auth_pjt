package com.hs.auth.dto.request;

import com.hs.auth.constants.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 비밀번호 변경 요청 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "{user.currentPassword.required}")
    private String currentPassword;

    @NotBlank(message = "{user.newPassword.required}")
    @Size(min = AppConstants.Validation.PASSWORD_MIN_LENGTH, max = AppConstants.Validation.PASSWORD_MAX_LENGTH, message = "{user.newPassword.size}")
    private String newPassword;

    @NotBlank(message = "{user.confirmNewPassword.required}")
    private String confirmNewPassword;
}
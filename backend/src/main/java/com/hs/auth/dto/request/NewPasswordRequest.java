package com.hs.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequest {

    @NotBlank(message = "{token.not.blank}")
    private String token;

    @NotBlank(message = "{password.not.blank}")
    @Size(min = 6, message = "{password.size}")
    private String newPassword;

    @NotBlank(message = "{password.confirm.not.blank}")
    private String confirmPassword;
}
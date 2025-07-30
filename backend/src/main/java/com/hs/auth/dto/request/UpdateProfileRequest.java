package com.hs.auth.dto.request;

import com.hs.auth.constants.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 프로필 업데이트 요청 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

      @NotBlank(message = "{user.nickname.required}")
      @Size(min = AppConstants.Validation.NICKNAME_MIN_LENGTH, max = AppConstants.Validation.NICKNAME_MAX_LENGTH, message = "{user.nickname.size}")
      private String nickname;

      @NotBlank(message = "{user.name.required}")
      @Size(min = AppConstants.Validation.NAME_MIN_LENGTH, max = AppConstants.Validation.NAME_MAX_LENGTH, message = "{user.name.size}")
      private String name;

      @NotBlank(message = "{user.phoneNumber.required}")
      @Pattern(regexp = AppConstants.Validation.PHONE_NUMBER_PATTERN, message = "{user.phoneNumber.pattern}")
      private String phoneNumber;
}
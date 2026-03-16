package com.bank.gugu.user.service.dto.request;

import com.bank.gugu.global.regex.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record JoinRequest(
        @Schema(description = "아이디", example = "testId1")
        @Pattern(regexp = Regex.ID, message = "아이디 형식을 확인해주세요.")
        @NotBlank(message = "이이디는 필수입니다.")
        String userId,

        @Schema(description = "비밀번호", example = "zxc123!@#")
        @Pattern(regexp = Regex.PASSWORD, message = "비밀번호 형식을 확인해주세요.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Schema(description = "비밀번호 확인", example = "zxc123!@#")
        @Pattern(regexp = Regex.PASSWORD, message = "비밀번호 형식을 확인해주세요.")
        @NotBlank(message = "비밀번호 확인은 필수입니다.")
        String passwordCheck,

        @Schema(description = "이메일", example = "chanhan12@naver.com")
        @Pattern(regexp = Regex.EMAIL, message = "이메일 형식을 확인해주세요.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email
) {

}

package com.bank.gugu.user.service.dto.response;

import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    @Schema(description = "아이디")
    private String userId;

    @Schema(description = "이메일")
    private String email;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
    }
}

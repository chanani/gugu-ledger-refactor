package com.bank.gugu.user.service.dto.response;

import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindUserIdResponse(
        @Schema(description = "회원 아이디")
        String userId
) {

    public static FindUserIdResponse from(User user) {
        String userId = user.getUserId();
        return new FindUserIdResponse(
                userId.substring(0, userId.length() - 3).concat("***")
        );
    }
}

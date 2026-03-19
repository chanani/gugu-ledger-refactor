package com.bank.gugu.user.service.dto.request;

import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindUserIdResponse(
        @Schema(description = "회원 아이디")
        String userId
) {

    public static FindUserIdResponse from(User user) {
        return new FindUserIdResponse(
                user.getUserId()
                        .substring(0, user.getUserId().length() - 3)
                        .concat("***")
        );
    }
}

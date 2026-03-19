package com.bank.gugu.user.service.dto.request;

import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindUserIdRequest(
        @Schema(description = "회원 아이디")
        String userId
) {

    public static FindUserIdRequest from (User user) {
        return new FindUserIdRequest(
                user.getUserId()
                .substring(0, user.getUserId().length() - 3)
                .concat("***")
        );
    }
}

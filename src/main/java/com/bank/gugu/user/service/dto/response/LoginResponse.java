package com.bank.gugu.user.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "accessToken")
        String accessToken,

        @Schema(description = "refreshToken")
        String refreshToken
) {
}

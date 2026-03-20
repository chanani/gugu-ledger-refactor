package com.bank.gugu.icon.service.dto.response;

import com.bank.gugu.icon.model.Icon;
import io.swagger.v3.oas.annotations.media.Schema;

public record IconsResponse(
        @Schema(description = "아이콘 ID")
        Long id,

        @Schema(description = "아이콘 경로")
        String path
){
    public static IconsResponse from(Icon icon) {
        return new IconsResponse(icon.getId(), icon.getPath());
    }
}

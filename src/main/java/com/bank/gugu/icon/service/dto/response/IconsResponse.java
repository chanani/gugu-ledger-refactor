package com.bank.gugu.icon.service.dto.response;

import com.bank.gugu.icon.model.Icon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class IconsResponse {

    @Schema(description = "아이콘 ID")
    private Long id;

    @Schema(description = "아이콘 경로")
    private String path;

    public IconsResponse(Icon icon) {
        this.id = icon.getId();
        this.path = icon.getPath();
    }
}

package com.bank.gugu.assets.service.request;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.common.model.constant.BooleanYn;
import io.swagger.v3.oas.annotations.media.Schema;

public record AssetsUpdateRequest(
        @Schema(description = "명칭", example = "연금저축계좌")
        String name,

        @Schema(description = "색상", example = "#666666")
        String color,

        @Schema(description = "자산 총합 여부", example = "true")
        boolean totalActive
) {

    public Assets toEntity(){
        return Assets.builder()
                .name(this.name)
                .color(this.color)
                .totalActive(totalActive ? BooleanYn.Y : BooleanYn.N)
                .build();
    }
}

package com.bank.gugu.assets.service.response;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.common.model.constant.BooleanYn;
import io.swagger.v3.oas.annotations.media.Schema;

public record AssetsResponse(

        @Schema(description = "자산 ID")
        Long id,

        @Schema(description = "자산명")
        String name,

        @Schema(description = "색상")
        String color,

        @Schema(description = "잔고")
        Integer balance,

        @Schema(description = "순서")
        Integer order,

        @Schema(description = "자산 총합 여부")
        BooleanYn totalActive
) {

    public AssetsResponse(Assets assets){
        this(assets.getId(), assets.getName(), assets.getColor(), assets.getBalance(), assets.getOrders(), assets.getTotalActive());
    }
}

package com.bank.gugu.assets.service.response;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.common.model.constant.BooleanYn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AssetsResponse {

    @Schema(description = "자산 ID")
    private Long id;

    @Schema(description = "자산명")
    private String name;

    @Schema(description = "색상")
    private String color;

    @Schema(description = "잔고")
    private Integer balance;

    @Schema(description = "순서")
    private Integer order;

    @Schema(description = "자산 총합 여부")
    private BooleanYn totalActive;

    public AssetsResponse(Assets assets) {
        this.id = assets.getId();
        this.name = assets.getName();
        this.color = assets.getColor();
        this.balance = assets.getBalance();
        this.order = assets.getOrders();
        this.totalActive = assets.getTotalActive();
    }
}

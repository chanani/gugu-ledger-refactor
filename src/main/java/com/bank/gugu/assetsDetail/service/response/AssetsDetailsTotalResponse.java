package com.bank.gugu.assetsDetail.service.response;

import com.bank.gugu.assets.model.Assets;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

public record AssetsDetailsTotalResponse(
        @Schema(description = "자산 상세정보 ID")
        Long id,
        @Schema(description = "이름")
        String name,
        @Schema(description = "색상")
        String color,
        @Schema(description = "금액")
        Integer balance,
        @Schema(description = "계좌 내역 상세 목록")
        Slice<AssetsDetailsResponse> assetsDetails
) {
    public static AssetsDetailsTotalResponse from(Assets assets, Slice<AssetsDetailsResponse> assetsDetails) {
        return new AssetsDetailsTotalResponse(
                assets.getId(),
                assets.getName(),
                assets.getColor(),
                assets.getBalance(),
                assetsDetails
        );
    }
}

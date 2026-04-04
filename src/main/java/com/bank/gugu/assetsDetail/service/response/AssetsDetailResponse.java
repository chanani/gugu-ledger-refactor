package com.bank.gugu.assetsDetail.service.response;

import com.bank.gugu.assetsDetail.model.AssetsDetail;
import com.bank.gugu.common.model.constant.PriceType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AssetsDetailResponse(
        @Schema(description = "자산 상세정보 ID")
        Long id,
        @Schema(description = "타입")
        String type,
        @Schema(description = "금액")
        Integer price,
        @Schema(description = "변경 후 잔액")
        Integer balance,
        @Schema(description = "날짜")
        LocalDate useDate,
        @Schema(description = "메모")
        String memo,
        @Schema(description = "활성화 여부")
        boolean active,
        @Schema(description = "결제 유형")
        PriceType priceType,
        @Schema(description = "카테고리 ID")
        Long categoryId
) {

    public static AssetsDetailResponse from(AssetsDetail assetsDetail) {
        return new AssetsDetailResponse(
                assetsDetail.getId(),
                String.valueOf(assetsDetail.getType()),
                assetsDetail.getPrice(),
                assetsDetail.getBalance(),
                assetsDetail.getUseDate(),
                assetsDetail.getMemo(),
                assetsDetail.isActiveY(),
                assetsDetail.getPriceType(),
                assetsDetail.getCategoryId()
        );
    }
}

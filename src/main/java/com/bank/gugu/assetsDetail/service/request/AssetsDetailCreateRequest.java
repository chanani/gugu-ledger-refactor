package com.bank.gugu.assetsDetail.service.request;

import com.bank.gugu.common.model.constant.PriceType;
import com.bank.gugu.common.model.constant.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssetsDetailCreateRequest(
        @Schema(description = "자산 그룹 ID", example = "5")
        @NotNull(message = "자산 그룹 번호는 필수입니다.")
        Long assetsId,

        @Schema(description = "입/출금 타입", example = "DEPOSIT")
        @NotNull(message = "입/출금 타입은 필수입니다.")
        RecordType type,

        @Schema(description = "금액", example = "20000")
        @NotNull(message = "금액은 필수입니다.")
        Integer price,

        @Schema(description = "날짜(yyyy-mm-dd)", example = "2025-06-25")
        @NotBlank(message = "날짜는 필수입니다.")
        String useDate,

        @Schema(description = "카테고리", example = "30")
        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId,

        @Schema(description = "결제 유형", example = "CHECK_CARD")
        @NotNull(message = "결제 유형은 필수입니다.")
        PriceType priceType,

        @Schema(description = "기록에 표시 여부", example = "true")
        @NotNull(message = "기록에 표시 여부는 필수입니다.")
        boolean active,

        @Schema(description = "메모", example = "늦은 입금")
        String memo
) {

    public boolean isType(RecordType type) {
        return this.type.equals(type);
    }

}

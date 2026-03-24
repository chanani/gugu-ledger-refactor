package com.bank.gugu.assetsDetail.service.request;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.assetsDetail.model.AssetsDetail;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.BooleanYn;
import com.bank.gugu.common.model.constant.PriceType;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.record.model.Records;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AssetsDetailUpdateRequest(

        @Schema(description = "입/출금 타입", example = "DEPOSIT")
        RecordType type,

        @Schema(description = "금액", example = "20000")
        Integer price,

        @Schema(description = "날짜(yyyy-mm-dd)", example = "2025-06-25")
        String useDate,

        @Schema(description = "기록에 표시 여부", example = "true")
        boolean active,

        @Schema(description = "메모", example = "늦은 입금")
        String memo,

        @Schema(description = "카테고리", example = "30")
        Long categoryId,

        @Schema(description = "결제 유형", example = "CHECK_CARD")
        PriceType priceType
) {

    public boolean isType(RecordType type) {
        return this.type.equals(type);
    }
}

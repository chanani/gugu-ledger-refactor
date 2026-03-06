package com.bank.gugu.recordsFavorite.service.dto.request;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.PriceType;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.recordsFavorite.model.RecordsFavorite;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecordsFavoriteCreateRequest(
        @Schema(description = "즐겨찾기명", example = "title")
        @NotBlank(message = "즐겨찾기명은 필수입니다.")
        String title,

        @Schema(description = "입/출금 타입", example = "DEPOSIT")
        @NotNull(message = "입/출급 타입은 필수입니다.")
        RecordType type,

        @Schema(description = "수단", example = "CHECK_CARD")
        @NotNull(message = "수단은 필수입니다.")
        PriceType priceType,

        @Schema(description = "금액", example = "10000")
        @NotNull(message = "금액은 필수입니다.")
        Integer price,

        @Schema(description = "할부", example = "2")
        Integer monthly,

        @Schema(description = "메모", example = "마트 장보기")
        String memo,

        @Schema(description = "카테고리 ID", example = "30")
        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId,

        @Schema(description = "자산그룹 ID", example = "3")
        Long assetsId
) {

    public RecordsFavorite toEntity(User user, Category category, Assets assets) {
        return RecordsFavorite.builder()
                .user(user)
                .category(category)
                .assets(assets)
                .title(this.title)
                .type(this.type)
                .price(type.equals(RecordType.DEPOSIT) ? this.price : -this.price)
                .priceType(this.priceType)
                .monthly(this.priceType.equals(PriceType.CARD) ? this.monthly : null)
                .memo(this.memo)
                .build();
    }
}

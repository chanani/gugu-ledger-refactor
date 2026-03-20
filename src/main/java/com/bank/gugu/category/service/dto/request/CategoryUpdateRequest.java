package com.bank.gugu.category.service.dto.request;

import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.icon.model.Icon;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequest(
        @Schema(description = "카테고리명", example = "기타")
        @NotBlank(message = "카테고리명은 필수입니다.")
        String name,

        @Schema(description = "타입(WITHDRAW, DEPOSIT)", example = "WITHDRAW")
        @NotNull(message = "타입은 필수입니다.")
        RecordType type,

        @Schema(description = "아이콘")
        Integer icon
) {
}

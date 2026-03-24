package com.bank.gugu.assets.service.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AssetsSummaryResponse(
        @Schema(description = "총자산")
        Integer totalAssets,
        @Schema(description = "자산 그룹 목록")
        List<AssetsResponse> assets
) {
}

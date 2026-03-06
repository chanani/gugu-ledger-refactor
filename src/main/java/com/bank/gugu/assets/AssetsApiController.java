package com.bank.gugu.assets;

import com.bank.gugu.assets.service.AssetsService;
import com.bank.gugu.assets.service.request.AssetsCreateRequest;
import com.bank.gugu.assets.service.request.AssetsUpdateRequest;
import com.bank.gugu.assets.service.response.AssetsPageResponse;
import com.bank.gugu.assets.service.response.AssetsResponse;
import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Assets API Controller", description = "자산 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class AssetsApiController implements AssetsControllerDocs {

    private final AssetsService assetsService;

    @PostMapping("/api/v1/user/assets")
    @Override
    public ResponseEntity<ApiResponse> addAssets(
            @Valid @RequestBody AssetsCreateRequest request,
            @AuthUser User user
    ) {
        assetsService.addAssets(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PutMapping("/api/v1/user/assets/{assetsId}")
    @Override
    public ResponseEntity<ApiResponse> updateAssets(
            @PathVariable(name = "assetsId") Long assetsId,
            @Valid @RequestBody AssetsUpdateRequest request
    ) {
        assetsService.updateAssets(request, assetsId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/api/v1/user/assets/{assetsId}")
    @Override
    public ResponseEntity<ApiResponse> deleteAssets(@PathVariable(name = "assetsId") Long assetsId) {
        assetsService.deleteAssets(assetsId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/user/assets")
    @Override
    public ResponseEntity<DataResponse<AssetsPageResponse>> getAssets(@AuthUser User user) {
        AssetsPageResponse assets = assetsService.getAssetsList(user);
        return ResponseEntity.ok(DataResponse.send(assets));
    }

    @GetMapping("/api/v1/user/assets/{assetsId}")
    @Override
    public ResponseEntity<DataResponse<AssetsResponse>> getAssets(@PathVariable(name = "assetsId") Long assetsId) {
        AssetsResponse assets = assetsService.getAssets(assetsId);
        return ResponseEntity.ok(DataResponse.send(assets));
    }
}

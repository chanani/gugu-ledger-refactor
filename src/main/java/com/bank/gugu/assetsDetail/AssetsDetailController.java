package com.bank.gugu.assetsDetail;

import com.bank.gugu.assetsDetail.input.AssetsDetailsInput;
import com.bank.gugu.assetsDetail.service.AssetsDetailService;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailCreateRequest;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailUpdateRequest;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailResponse;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailsTotalResponse;
import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.page.PageInput;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Assets Detail API Controller", description = "자산 상세 정보 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class AssetsDetailController implements AssetsDetailControllerDocs{

    private final AssetsDetailService assetsDetailService;

    @PostMapping("/api/v1/user/assets-detail")
    @Override
    public ResponseEntity<ApiResponse> addAssetsDetail(
            @Valid @RequestBody AssetsDetailCreateRequest request,
            @AuthUser User user
    ) {
        assetsDetailService.addAssetsDetail(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PutMapping("/api/v1/user/assets-detail/{assetsDetailId}")
    @Override
    public ResponseEntity<ApiResponse> updateAssetsDetail(
            @PathVariable(name = "assetsDetailId") Long assetsDetailId,
            @Valid @RequestBody AssetsDetailUpdateRequest request,
            @AuthUser User user
    ) {
        assetsDetailService.updateAssetsDetail(assetsDetailId, request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/api/v1/user/assets-detail/{assetsDetailId}")
    @Override
    public ResponseEntity<ApiResponse> deleteAssetsDetail(@PathVariable(name = "assetsDetailId") Long assetsDetailId) {
        assetsDetailService.deleteAssetsDetail(assetsDetailId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/user/assets-details")
    @Override
    public ResponseEntity<DataResponse<AssetsDetailsTotalResponse>> getAssetsDetails(
            @Parameter(hidden = true) User user,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @ParameterObject @ModelAttribute AssetsDetailsInput input
    ) {
        PageInput pageInput = PageInput.builder().page(page).size(size).build();
        AssetsDetailsTotalResponse assetsDetails = assetsDetailService.getAssetsDetails(pageInput, input, user);
        return ResponseEntity.ok(DataResponse.send(assetsDetails));
    }

    @GetMapping("/api/v1/user/assets-details/{assetsDetailId}")
    @Override
    public ResponseEntity<DataResponse<AssetsDetailResponse>> getAssetsDetail(@PathVariable(name = "assetsDetailId") Long assetsDetailId) {
        AssetsDetailResponse assetsDetails = assetsDetailService.getAssetsDetail(assetsDetailId);
        return ResponseEntity.ok(DataResponse.send(assetsDetails));
    }

}

package com.bank.gugu.assets;

import com.bank.gugu.assets.service.request.AssetsCreateRequest;
import com.bank.gugu.assets.service.request.AssetsUpdateRequest;
import com.bank.gugu.assets.service.response.AssetsSummaryResponse;
import com.bank.gugu.assets.service.response.AssetsResponse;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;

public interface AssetsControllerDocs {


    @Operation(
            summary = "자산그룹 생성",
            description = "자산 그룹을 생성합니다.",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AssetsCreateRequest.class))),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산그룹 생성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> addAssets(
            AssetsCreateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "자산그룹 수정",
            description = "자산 그룹을 수정합니다.",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AssetsUpdateRequest.class))),
            parameters = {
                    @Parameter(
                            name = "assetsId",
                            description = "자산그룹 ID",
                            required = true,
                            example = "1"
                    ),
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산그룹 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateAssets(
            Long assetsId,
            AssetsUpdateRequest request
    );

    @Operation(
            summary = "자산그룹 삭제",
            description = "자산 그룹을 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "assetsId",
                            description = "자산그룹 ID",
                            required = true,
                            example = "1"
                    ),
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산그룹 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> deleteAssets(Long assetsId);

    @Operation(
            summary = "자산그룹 목록 조회",
            description = "자산 그룹 목록을 조회합니다.",
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산그룹 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {AssetsSummaryResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<AssetsSummaryResponse>> getAssets(@Parameter(hidden = true) User user);

    @Operation(
            summary = "자산 정보 상세 조회",
            description = "자산 정보를 상세 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "assetsId",
                            description = "자산그룹 ID",
                            required = true,
                            example = "1"
                    ),
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산 정보 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {AssetsResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<AssetsResponse>> getAssets(Long assetsId);

}

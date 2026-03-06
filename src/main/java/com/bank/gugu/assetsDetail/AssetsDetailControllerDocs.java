package com.bank.gugu.assetsDetail;

import com.bank.gugu.assetsDetail.input.AssetsDetailsInput;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailCreateRequest;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailUpdateRequest;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailResponse;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailsTotalResponse;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public interface AssetsDetailControllerDocs {

    @Operation(
            summary = "자산 상세내역 생성",
            description = "자산 상세내역을 생성합니다.",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AssetsDetailCreateRequest.class))),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산 상세내역 생성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> addAssetsDetail(
            AssetsDetailCreateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "자산 상세내역 수정",
            description = "자산 상세내역을 수정합니다.",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AssetsDetailUpdateRequest.class))),
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
                    description = "자산 상세내역 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateAssetsDetail(
            Long assetsId,
            AssetsDetailUpdateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "자산 상세내역 삭제",
            description = "자산 상세내역을 삭제합니다.",
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
                    description = "자산 상세내역 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> deleteAssetsDetail(Long assetsId);

    @Operation(
            summary = "자산 상세내역 목록 조회",
            description = """
                    자산 상세내역 목록을 조회합니다.
                    무한스크롤로 제작되었으며, 요청한 사이즈보다 1개 더 반환됩니다.
                    화면에서는 size 만큼 화면에 노출시키고, size 보다 1이클 때 다음 페이지를 요청해주세요.
                    """,
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호", example = "1"),
                    @Parameter(name = "size", description = "페이지 사이즈", example = "10")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산 상세내역 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {AssetsDetailsTotalResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<AssetsDetailsTotalResponse>> getAssetsDetails(
            @Parameter(hidden = true) User user,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @ParameterObject @ModelAttribute AssetsDetailsInput input
    );

    @Operation(
            summary = "자산 상세내역 조회",
            description = "자산 상세내역을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "assetsDetailId",
                            description = "자산 상세내역 ID",
                            required = true,
                            example = "1"
                    ),
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산 상세내역 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {AssetsDetailResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<AssetsDetailResponse>> getAssetsDetail(Long assetsId);

}

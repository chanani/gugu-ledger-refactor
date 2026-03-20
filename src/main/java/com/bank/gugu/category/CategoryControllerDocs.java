package com.bank.gugu.category;

import com.bank.gugu.category.service.dto.request.CategoryCreateRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateOrderRequest;
import com.bank.gugu.category.service.dto.request.CategoryUpdateRequest;
import com.bank.gugu.category.service.dto.response.CategoriesResponse;
import com.bank.gugu.category.service.dto.response.CategoryResponse;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryControllerDocs {

    @Operation(
            summary = "카테고리 생성",
            description = "카테고리를 생성합니다.",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryCreateRequest.class))),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 생성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> addCategory(
            CategoryCreateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "카테고리 수정",
            description = "카테고리를 수정합니다.",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryUpdateRequest.class))),
            parameters = {
                    @Parameter(
                            name = "categoryId",
                            description = "카테고리 ID",
                            required = true,
                            example = "1"
                    ),
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateCategory(
            Long categoryId,
            CategoryUpdateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "카테고리 삭제",
            description = "카테고리를 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "categoryId",
                            description = "카테고리 ID",
                            required = true,
                            example = "1"
                    ),
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> deleteCategory(
            Long categoryId,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "카테고리 목록 조회",
            description = "카테고리 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "type", description = "타입", example = "ALL")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "자산 상세내역 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {CategoriesResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<List<CategoriesResponse>>> getCategories(
            @Parameter(hidden = true) User user,
            @Parameter(name = "type") RecordType type
    );

    @Operation(
            summary = "카테고리 상세 정보 조회",
            description = "카테고리 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "categoryId",
                            description = "카테고리 ID",
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
                                    subTypes = {CategoryResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<CategoryResponse>> getCategory(Long categoryId);

    @Operation(
            summary = "카테고리 순서 변경",
            description = "카테고리 순서를 변경합니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryUpdateOrderRequest.class))
                    )
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 순서 변경 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateCategoryOrder(
            List<CategoryUpdateOrderRequest> request,
            @Parameter(hidden = true) User user
    );

}

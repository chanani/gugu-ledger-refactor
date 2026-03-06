package com.bank.gugu.recordsFavorite;


import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.recordsFavorite.service.dto.request.RecordsFavoriteCreateRequest;
import com.bank.gugu.recordsFavorite.service.dto.request.RecordsFavoriteUpdateRequest;
import com.bank.gugu.recordsFavorite.service.dto.respnose.RecordsFavoriteResponse;
import com.bank.gugu.recordsFavorite.service.dto.respnose.RecordsFavoritesResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RecordsFavoriteControllerDocs {

    @Operation(
            summary = "입/출금 내역 즐겨찾기 등록",
            description = "입/출금 내역 즐겨찾기를 등록합니다.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = RecordsFavoriteCreateRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 등록 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> addRecordsFavorite(
            RecordsFavoriteCreateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "입/출금 내역 즐겨찾기 삭제",
            description = "입/출금 내역 즐겨찾기를 삭제합니다.",
            parameters = {
                    @Parameter(name = "recordsFavoriteId", description = "즐겨찾기 ID", required = true, example = "1")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> deleteRecordsFavorite(Long recordsFavoriteId);

    @Operation(
            summary = "입/출금 내역 즐겨찾기 수정",
            description = "입/출금 내역 즐겨찾기를 수정합니다.",
            parameters = {
                    @Parameter(name = "recordsFavoriteId", description = "즐겨찾기 ID", required = true, example = "1")
            },
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = RecordsFavoriteUpdateRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateRecordsFavorite(
            Long recordsFavoriteId,
            RecordsFavoriteUpdateRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "입/출금 내역 즐겨찾기 목록 조회",
            description = "입/출금 내역 즐겨찾기의 목록을 조회합니다.",
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordsFavoritesResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<List<RecordsFavoritesResponse>>> getRecordsFavorites(
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "입/출금 내역 즐겨찾기 상세 조회",
            description = "입/출금 내역 즐겨찾기 내용을 상세조회합니다.",
            parameters = {
                    @Parameter(name = "recordsFavoriteId", description = "즐겨찾기 ID", required = true, example = "1")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordsFavoriteResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<RecordsFavoriteResponse>> getRecordsFavorite(Long recordsFavoriteId);
}
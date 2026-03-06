package com.bank.gugu.recordsImage;

import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.recordsImage.service.response.RecordsImagesResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface RecordsImageControllerDocs {

    @Operation(
            summary = "입/출금 내역 이미지 파일 삭제",
            description = "입/출금 내역에 등록된 이미지 파일을 삭제합니다.",
            parameters = {
                    @Parameter(name = "recordsImageId", description = "이미지 ID", required = true, example = "1")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이미지 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> deleteRecord(Long recordsImageId);

    @Operation(
            summary = "입/출금 내역 이미지 파일 등록",
            description = "입/출금 내역에 등록된 이미지 파일을 등록합니다.",
            parameters = {
                    @Parameter(name = "recordsId", description = "내역 ID", required = true, example = "1")
            },
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object", requiredProperties = {"uploadImage"})
                    )
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이미지 등록 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> addRecord(
            Long recordsId,
            @Parameter(description = "업로드 이미지 파일", required = true) MultipartFile inputFile,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "입/출금 내역 이미지 파일 조회",
            description = "입/출금 내역에 등록된 월별 이미지 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "yearMonth", description = "조회 년월", required = true, example = "2026-03")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "월별 이미지 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordsImagesResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<List<RecordsImagesResponse>>> getRecordsImage(
            String yearMonth,
            @Parameter(hidden = true) User user
    );

}

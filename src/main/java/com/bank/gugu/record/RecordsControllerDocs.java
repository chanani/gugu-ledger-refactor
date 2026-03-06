package com.bank.gugu.record;

import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.record.service.dto.request.RecordCreateRequest;
import com.bank.gugu.record.service.dto.request.RecordUpdateMemoRequest;
import com.bank.gugu.record.service.dto.request.RecordUpdateRequest;
import com.bank.gugu.record.service.dto.response.RecordResponse;
import com.bank.gugu.record.service.dto.response.RecordsCalendarResponse;
import com.bank.gugu.record.service.dto.response.RecordsCurrentResponse;
import com.bank.gugu.record.service.dto.response.RecordsResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface RecordsControllerDocs {

    @Operation(
            summary = "입/출금 내역 등록",
            description = "입/출금 내역을 등록합니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = RecordCreateRequest.class)
                    )
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "입/출금 내역 등록 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> addRecord(
            RecordCreateRequest request,
            @Parameter(hidden = true) User user,
            @Parameter(description = "첨부 파일 목록", required = false) List<MultipartFile> inputFiles
    ) throws IOException;

    @Operation(
            summary = "입/출금 내역 삭제",
            description = "입/출금 내역을 삭제합니다.",
            parameters = {
                    @Parameter(name = "recordsId", description = "내역 ID", required = true, example = "1")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "입/출금 내역 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> deleteRecord(Long recordsId);

    @Operation(
            summary = "입/출금 내역 수정",
            description = "입/출금 내역을 수정합니다.",
            parameters = {
                    @Parameter(name = "recordsId", description = "내역 ID", required = true, example = "1")
            },
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = RecordUpdateRequest.class)
                    )
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "입/출금 내역 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateRecord(
            RecordUpdateRequest request,
            Long recordsId,
            @Parameter(description = "첨부 파일 목록", required = false) List<MultipartFile> inputFiles,
            @Parameter(hidden = true) User user
    ) throws IOException;

    @Operation(
            summary = "입/출금 하루 내역 조회",
            description = "하루의 입/출금 내역을 조회합니다.",
            parameters = {
                    @Parameter(name = "currentDate", description = "조회 날짜", required = true, example = "2026-03-06")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "하루 내역 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordsCurrentResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<List<RecordsCurrentResponse>>> getRecordsCurrent(
            String currentDate,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "입/출금 상세 내역 조회",
            description = "입/출금 상세 내역을 조회합니다.",
            parameters = {
                    @Parameter(name = "recordsId", description = "내역 ID", required = true, example = "1")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "상세 내역 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<RecordResponse>> getRecord(Long recordsId);

    @Operation(
            summary = "입/출금 한달 내역 조회",
            description = "한달 입/출금 내역을 조회합니다.",
            parameters = {
                    @Parameter(name = "yearMonth", description = "조회 년월", required = true, example = "2026-03")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "한달 내역 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordsResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<List<RecordsResponse>>> getRecords(
            String yearMonth,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "입/출금 메모 수정",
            description = "입/출금 메모를 수정합니다.",
            parameters = {
                    @Parameter(name = "recordsId", description = "내역 ID", required = true, example = "1")
            },
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = RecordUpdateMemoRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "메모 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateMemo(Long recordsId, RecordUpdateMemoRequest request);

    @Operation(
            summary = "캘린더의 입/출금 내역 및 총 금액 조회",
            description = "캘린더의 입/출금 내역 및 총 금액을 조회합니다.",
            parameters = {
                    @Parameter(name = "yearMonth", description = "조회 년월", required = true, example = "2025-06")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "캘린더 내역 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {RecordsCalendarResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<RecordsCalendarResponse>> getCalendarRecords(
            String yearMonth,
            @Parameter(hidden = true) User user
    );


}

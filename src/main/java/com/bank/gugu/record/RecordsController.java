package com.bank.gugu.record;

import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.record.service.RecordsService;
import com.bank.gugu.record.service.dto.request.RecordCreateRequest;
import com.bank.gugu.record.service.dto.request.RecordUpdateMemoRequest;
import com.bank.gugu.record.service.dto.request.RecordUpdateRequest;
import com.bank.gugu.record.service.dto.response.RecordResponse;
import com.bank.gugu.record.service.dto.response.RecordsCalendarResponse;
import com.bank.gugu.record.service.dto.response.RecordsCurrentResponse;
import com.bank.gugu.record.service.dto.response.RecordsResponse;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Records API Controller", description = "입/출금 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class RecordsController implements RecordsControllerDocs{

    private final RecordsService recordsService;

    @PostMapping(value = "/api/v1/user/records", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    @Override
    public ResponseEntity<ApiResponse> addRecord(
            @Valid @RequestPart RecordCreateRequest request,
            @AuthUser User user,
            @RequestPart(value = "files", required = false) List<MultipartFile> inputFiles
    ) throws IOException {
        recordsService.addRecord(request, user, inputFiles);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/api/v1/user/records/{recordsId}")
    @Override
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable(name = "recordsId") Long recordsId) {
        recordsService.deleteRecord(recordsId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PutMapping(value = "/api/v1/user/records/{recordsId}", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    @Override
    public ResponseEntity<ApiResponse> updateRecord(
            @Valid @RequestPart RecordUpdateRequest request,
            @PathVariable(name = "recordsId") Long recordsId,
            @RequestPart(value = "files", required = false) List<MultipartFile> inputFiles,
            @Parameter(hidden = true) User user
    ) throws IOException {
        recordsService.updateRecord(request, recordsId, inputFiles, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/user/records-current")
    @Override
    public ResponseEntity<DataResponse<List<RecordsCurrentResponse>>> getRecordsCurrent(
            @Parameter(name = "currentDate") String currentDate, @Parameter(hidden = true) User user
    ) {
        List<RecordsCurrentResponse> records = recordsService.getCurrentRecord(LocalDate.parse(currentDate), user);
        return ResponseEntity.ok(DataResponse.send(records));
    }

    @GetMapping("/api/v1/user/records/{recordsId}")
    @Override
    public ResponseEntity<DataResponse<RecordResponse>> getRecord(@PathVariable(name = "recordsId") Long recordsId) {
        RecordResponse record = recordsService.getRecord(recordsId);
        return ResponseEntity.ok(DataResponse.send(record));
    }

    @GetMapping("/api/v1/user/records")
    @Override
    public ResponseEntity<DataResponse<List<RecordsResponse>>> getRecords(
            @Parameter(name = "yearMonth") String yearMonth, @Parameter(hidden = true) User user
    ) {
        List<RecordsResponse> records = recordsService.getMonthRecord(yearMonth, user);
        return ResponseEntity.ok(DataResponse.send(records));
    }

    @PutMapping("/api/v1/user/records-memo/{recordsId}")
    @Override
    public ResponseEntity<ApiResponse> updateMemo(
            @PathVariable(name = "recordsId") Long recordsId,
            @Valid @RequestBody RecordUpdateMemoRequest request
    ) {
        recordsService.updateMemo(recordsId, request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/user/records-calendar")
    @Override
    public ResponseEntity<DataResponse<RecordsCalendarResponse>> getCalendarRecords(
            @Parameter(name = "yearMonth", example = "2025-06") String yearMonth, @Parameter(hidden = true) User user
    ) {
        RecordsCalendarResponse records = recordsService.getCalendarRecord(yearMonth, user);
        return ResponseEntity.ok(DataResponse.send(records));
    }

}

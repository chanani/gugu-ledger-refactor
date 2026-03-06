package com.bank.gugu.recordsImage;

import com.bank.gugu.recordsImage.service.RecordsImageService;
import com.bank.gugu.recordsImage.service.response.RecordsImagesResponse;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "Records Image API Controller", description = "입/출금 이미지 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class RecordsImageController implements RecordsImageControllerDocs {

    private final RecordsImageService recordsImageService;

    @DeleteMapping(value = "/api/v1/user/records-image/{recordsImageId}")
    @Override
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable(name = "recordsImageId") Long recordsImageId) {
        recordsImageService.deleteRecordImage(recordsImageId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PostMapping(value = "/api/v1/user/records-image/{recordsId}", consumes = {"multipart/form-data"})
    @Override
    public ResponseEntity<ApiResponse> addRecord(
            @PathVariable(name = "recordsId") Long recordsId,
            @RequestPart(value = "uploadImage") MultipartFile inputFile,
            @Parameter(hidden = true) User user
    ) {
        recordsImageService.addRecordImage(recordsId, inputFile, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping(value = "/api/v1/user/records-image")
    @Override
    public ResponseEntity<DataResponse<List<RecordsImagesResponse>>> getRecordsImage(
            @Parameter(name = "yearMonth") String yearMonth,
            @Parameter(hidden = true) User user
    ) {
        List<RecordsImagesResponse> recordsImages = recordsImageService.getRecordsImages(yearMonth, user);
        return ResponseEntity.ok(DataResponse.send(recordsImages));
    }


}

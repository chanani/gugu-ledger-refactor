package com.bank.gugu.recordsFavorite;

import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.recordsFavorite.service.RecordsFavoriteService;
import com.bank.gugu.recordsFavorite.service.dto.request.RecordsFavoriteCreateRequest;
import com.bank.gugu.recordsFavorite.service.dto.request.RecordsFavoriteUpdateRequest;
import com.bank.gugu.recordsFavorite.service.dto.respnose.RecordsFavoriteResponse;
import com.bank.gugu.recordsFavorite.service.dto.respnose.RecordsFavoritesResponse;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Records Favorite API Controller", description = "입/출금 기록 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class RecordsFavoriteController implements RecordsFavoriteControllerDocs {

    private final RecordsFavoriteService recordsFavoriteService;

    @PostMapping(value = "/api/v1/user/records-favorite")
    @Override
    public ResponseEntity<ApiResponse> addRecordsFavorite(
            @Valid @RequestBody RecordsFavoriteCreateRequest request,
            @AuthUser User user
    ) {
        recordsFavoriteService.addRecordsFavorite(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping(value = "/api/v1/user/records-favorite/{recordsFavoriteId}")
    @Override
    public ResponseEntity<ApiResponse> deleteRecordsFavorite(@PathVariable(name = "recordsFavoriteId") Long recordsFavoriteId) {
        recordsFavoriteService.deleteRecordsFavorite(recordsFavoriteId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PutMapping(value = "/api/v1/user/records-favorite/{recordsFavoriteId}")
    @Override
    public ResponseEntity<ApiResponse> updateRecordsFavorite(
            @PathVariable(name = "recordsFavoriteId") Long recordsFavoriteId,
            @Valid @RequestBody RecordsFavoriteUpdateRequest request,
            @Parameter(hidden = true) User user
    ) {
        recordsFavoriteService.updateRecordsFavorite(recordsFavoriteId, request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping(value = "/api/v1/user/records-favorite")
    @Override
    public ResponseEntity<DataResponse<List<RecordsFavoritesResponse>>> getRecordsFavorites(@Parameter(hidden = true) User user) {
        List<RecordsFavoritesResponse> favorites = recordsFavoriteService.getRecordsFavorites(user);
        return ResponseEntity.ok(DataResponse.send(favorites));
    }

    @GetMapping(value = "/api/v1/user/records-favorite/{recordsFavoriteId}")
    @Override
    public ResponseEntity<DataResponse<RecordsFavoriteResponse>> getRecordsFavorite(@PathVariable(name = "recordsFavoriteId") Long recordsFavoriteId) {
        RecordsFavoriteResponse favorites = recordsFavoriteService.getRecordsFavorite(recordsFavoriteId);
        return ResponseEntity.ok(DataResponse.send(favorites));
    }
}

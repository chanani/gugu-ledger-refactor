package com.bank.gugu.recordsFavorite.service;

import com.bank.gugu.assets.repository.AssetsRepository;
import com.bank.gugu.category.repository.CategoryRepository;
import com.bank.gugu.recordsFavorite.repository.RecordsFavoriteRepository;
import com.bank.gugu.recordsFavorite.service.dto.request.RecordsFavoriteCreateRequest;
import com.bank.gugu.recordsFavorite.service.dto.request.RecordsFavoriteUpdateRequest;
import com.bank.gugu.recordsFavorite.service.dto.respnose.RecordsFavoriteResponse;
import com.bank.gugu.recordsFavorite.service.dto.respnose.RecordsFavoritesResponse;
import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.recordsFavorite.model.RecordsFavorite;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultRecordsFavoriteService implements RecordsFavoriteService {

    private final RecordsFavoriteRepository recordsFavoriteRepository;
    private final CategoryRepository categoryRepository;
    private final AssetsRepository assetsRepository;

    @Override
    @Transactional
    public void addRecordsFavorite(RecordsFavoriteCreateRequest request, User user) {
        // 카테고리 Entity 조회
        Category findCategory = categoryRepository.findByIdAndStatus(request.categoryId(), StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
        // 자산 Entity 조회
        Assets findAssets = null;
        if(request.assetsId() != null){
            findAssets = assetsRepository.findByIdAndStatus(request.assetsId(), StatusType.ACTIVE)
                    .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ASSETS));
        }
        // dto to Entity
        RecordsFavorite newEntity = request.toEntity(user, findCategory, findAssets);
        recordsFavoriteRepository.save(newEntity);
    }

    @Override
    @Transactional
    public void deleteRecordsFavorite(Long recordsFavoriteId) {
        // 즐겨찾기 조회
        RecordsFavorite findRecordsFavorite = recordsFavoriteRepository.findByIdAndStatus(recordsFavoriteId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_RECORDS_FAVORITE));
        // 소프트 삭제
        findRecordsFavorite.remove();
    }

    @Override
    @Transactional
    public void updateRecordsFavorite(Long recordsFavoriteId, RecordsFavoriteUpdateRequest request, User user) {
        // 즐겨찾기 조회
        RecordsFavorite findRecordsFavorite = recordsFavoriteRepository.findByIdAndStatus(recordsFavoriteId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_RECORDS_FAVORITE));
        // 카테고리 Entity 조회
        Category findCategory = categoryRepository.findByIdAndStatus(request.categoryId(), StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
        // 자산 Entity 조회
        Assets findAssets = null;
        if(request.assetsId() != null){
            findAssets = assetsRepository.findByIdAndStatus(request.assetsId(), StatusType.ACTIVE)
                    .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ASSETS));
        }
        // dto to Entity
        RecordsFavorite newEntity = request.toEntity(user, findCategory, findAssets);
        // update
        findRecordsFavorite.update(newEntity);
    }

    @Override
    public List<RecordsFavoritesResponse> getRecordsFavorites(User user) {
        return recordsFavoriteRepository.findAllByUserAndStatusOrderById(user, StatusType.ACTIVE)
                 .stream()
                 .map(RecordsFavoritesResponse::new)
                 .toList();

    }

    @Override
    public RecordsFavoriteResponse getRecordsFavorite(Long recordsFavoriteId) {
        RecordsFavorite recordsFavorite = recordsFavoriteRepository.findByIdAndStatus(recordsFavoriteId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_RECORDS_FAVORITE));
        return new  RecordsFavoriteResponse(recordsFavorite);
    }
}

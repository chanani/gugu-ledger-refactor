package com.bank.gugu.assetsDetail.service;

import com.bank.gugu.assets.repository.AssetsRepository;
import com.bank.gugu.assetsDetail.input.AssetsDetailsInput;
import com.bank.gugu.assetsDetail.mapper.AssetsDetailMapper;
import com.bank.gugu.assetsDetail.repository.AssetsDetailRepository;
import com.bank.gugu.assetsDetail.repository.condition.AssetsCondition;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailCreateRequest;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailUpdateRequest;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailResponse;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailsResponse;
import com.bank.gugu.assetsDetail.service.response.AssetsDetailsTotalResponse;
import com.bank.gugu.category.repository.CategoryRepository;
import com.bank.gugu.record.mapper.RecordsMapper;
import com.bank.gugu.record.repository.RecordsRepository;
import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.assetsDetail.model.AssetsDetail;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.record.model.Records;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import com.bank.gugu.global.page.PageInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultAssetsDetailService implements AssetsDetailService {

    private final AssetsRepository assetsRepository;
    private final AssetsDetailRepository assetsDetailRepository;
    private final RecordsRepository recordsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void addAssetsDetail(AssetsDetailCreateRequest request, User user) {
        Assets assets = findAssetOrThrow(request.assetsId());
        Category category = findCategoryOrThrow(request.categoryId());
        AssetsDetail assetsDetail = AssetsDetailMapper.fromCreateRequest(request, user, assets, category);
        AssetsDetail saveAssetsDetail = assetsDetailRepository.save(assetsDetail);
        assets.updateBalance(saveAssetsDetail);

        Records records = RecordsMapper.fromAssetsDetailCreateRequest(request, user, assets, category);
        if (!request.active()) {
            records.inactive(); // 활성화 안할 경우 비노출
        }

        Records saveRecord = recordsRepository.save(records);
        saveAssetsDetail.updateRecordId(saveRecord);
    }


    @Override
    @Transactional
    public void updateAssetsDetail(Long assetsDetailId, AssetsDetailUpdateRequest request, User user) {
        AssetsDetail assertDetail = findAssetsDetailOrThrow(assetsDetailId);
        Assets assets = findAssetOrThrow(assertDetail.getAssetsId());
        Category category = findCategoryOrThrow(request.categoryId());
        AssetsDetail toEntity = AssetsDetailMapper.fromUpdateRequest(request, assertDetail, assets, category);

        Records records = findRecordOrThrow(assertDetail);
        Records newRecordEntity = RecordsMapper.fromAssetsDetailUpdateRequest(request, category);
        assets.updateRecordBalance(records, newRecordEntity);
        records.update(newRecordEntity);
        records.updateActive(request.active());
        assertDetail.update(toEntity);
    }

    @Override
    @Transactional
    public void deleteAssetsDetail(Long assetsDetailId) {
        AssetsDetail assetsDetail = findAssetsDetailOrThrow(assetsDetailId);
        assetsDetail.remove();

        if (assetsDetail.isActiveY()) {
            Records records = findRecordOrThrow(assetsDetail);
            records.remove();
        }
    }

    @Override
    public AssetsDetailsTotalResponse getAssetsDetails(PageInput pageInput, AssetsDetailsInput input, User user) {
        Assets assets = findAssetOrThrow(input.getAssetsId());
        AssetsCondition condition = input.toCondition();
        Pageable pageable = Pageable.ofSize(pageInput.size() + 1).withPage(pageInput.page() - 1);
        List<AssetsDetailsResponse> assetsDetails = assetsDetailRepository.findByQuery(pageable, condition, user).stream()
                .map(AssetsDetailsResponse::new)
                .toList();

        Pageable returnPageable = pageable.withPage(pageInput.page());
        Slice<AssetsDetailsResponse> pointDetailSlice = new SliceImpl<>(assetsDetails, returnPageable, hasNextPage(assetsDetails, pageable.getPageSize()));
        return AssetsDetailsTotalResponse.from(assets, pointDetailSlice);
    }

    @Override
    public AssetsDetailResponse getAssetsDetail(Long assetsDetailId) {
        AssetsDetail assetsDetail = findAssetsDetailOrThrow(assetsDetailId);
        return AssetsDetailResponse.from(assetsDetail);
    }

    /**
     * Slice에서 사용할 메서드
     */
    private boolean hasNextPage(List<AssetsDetailsResponse> pointDetails, int pageSize) {
        if (pointDetails.size() > pageSize) {
            pointDetails.remove(pageSize);
            return true;
        }
        return false;
    }

    private Assets findAssetOrThrow(Long assetsId) {
        return assetsRepository.findByIdAndStatus(assetsId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ASSETS));
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findByIdAndStatus(categoryId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_CATEGORY));
    }

    private AssetsDetail findAssetsDetailOrThrow(Long assetsDetailId) {
        return assetsDetailRepository.findByIdAndStatus(assetsDetailId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ASSETS_DETAIL));
    }

    private Records findRecordOrThrow(AssetsDetail findAssetsDetail) {
        return recordsRepository.findById(findAssetsDetail.getRecordId())
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_RECORDS));
    }

}

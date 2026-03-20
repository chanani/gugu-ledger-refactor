package com.bank.gugu.assets.service;

import com.bank.gugu.assets.mapper.AssetsMapper;
import com.bank.gugu.assets.repository.AssetsRepository;
import com.bank.gugu.assets.service.request.AssetsCreateRequest;
import com.bank.gugu.assets.service.request.AssetsUpdateRequest;
import com.bank.gugu.assets.service.response.AssetsPageResponse;
import com.bank.gugu.assets.service.response.AssetsResponse;
import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.common.model.constant.BooleanYn;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultAssetsService implements AssetsService {

    private final AssetsRepository assetsRepository;

    @Override
    @Transactional
    public void addAssets(AssetsCreateRequest request, User user) {
        Assets assets = AssetsMapper.fromCreateRequest(request, user);
        assetsRepository.save(assets);
    }

    @Override
    @Transactional
    public void updateAssets(AssetsUpdateRequest request, Long assetsId) {
        Assets findAssets = findAssetsOrThrow(assetsId);
        Assets assets = AssetsMapper.fromUpdateRequest(request);
        findAssets.update(assets);
    }

    @Override
    @Transactional
    public void deleteAssets(Long assetsId) {
        Assets assets = findAssetsOrThrow(assetsId);
        assets.remove();
    }

    @Override
    public AssetsPageResponse getAssetsList(User user) {
        // 자산 목록 조회
        List<AssetsResponse> findAssets = assetsRepository.findAllByUserIdAndStatusOrderByOrdersAsc(user.getId(), StatusType.ACTIVE).stream()
                .map(AssetsResponse::new)
                .toList();
        // 총 자산 계산
        int totalAssets = findAssets.stream()
                .filter(dto -> dto.getTotalActive().equals(BooleanYn.Y))
                .mapToInt(AssetsResponse::getBalance).sum();

        return new AssetsPageResponse(totalAssets, findAssets);
    }

    @Override
    public AssetsResponse getAssets(Long assetsId) {
        Assets findAssets = findAssetsOrThrow(assetsId);
        return new AssetsResponse(findAssets);
    }

    private Assets findAssetsOrThrow(Long assetsId) {
        return assetsRepository.findByIdAndStatus(assetsId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ASSETS));
    }
}

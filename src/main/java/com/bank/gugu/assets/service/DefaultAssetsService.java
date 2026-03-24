package com.bank.gugu.assets.service;

import com.bank.gugu.assets.mapper.AssetsMapper;
import com.bank.gugu.assets.repository.AssetsRepository;
import com.bank.gugu.assets.service.request.AssetsCreateRequest;
import com.bank.gugu.assets.service.request.AssetsUpdateRequest;
import com.bank.gugu.assets.service.response.AssetsSummaryResponse;
import com.bank.gugu.assets.service.response.AssetsResponse;
import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.common.model.constant.BooleanYn;
import com.bank.gugu.common.model.constant.StatusType;
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
    public AssetsSummaryResponse getAssetsList(User user) {
        List<AssetsResponse> assetsDto = assetsRepository.findAllByUserIdAndStatusOrderByOrdersAsc(user.getId(), StatusType.ACTIVE).stream()
                .map(AssetsResponse::new)
                .toList();

        int totalAssets = calculateTotalAssets(assetsDto);
        return new AssetsSummaryResponse(totalAssets, assetsDto);
    }

    private static int calculateTotalAssets(List<AssetsResponse> findAssets) {
        return findAssets.stream()
                .filter(dto -> dto.totalActive().equals(BooleanYn.Y))
                .mapToInt(AssetsResponse::balance).sum();
    }

    @Override
    public AssetsResponse getAssets(Long assetsId) {
        Assets assets = findAssetsOrThrow(assetsId);
        return new AssetsResponse(assets);
    }

    private Assets findAssetsOrThrow(Long assetsId) {
        return assetsRepository.findByIdAndStatus(assetsId, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_ASSETS));
    }
}

package com.bank.gugu.record.mapper;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailCreateRequest;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailUpdateRequest;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.record.model.Records;
import com.bank.gugu.user.model.User;

import java.time.LocalDate;

public class RecordsMapper {

    public static Records fromAssetsDetailCreateRequest(
            AssetsDetailCreateRequest request,
            User user,
            Assets assets,
            Category category
    ) {
        return Records.builder()
                .user(user)
                .category(category)
                .assets(assets)
                .type(request.type())
                .price(request.price())
                .priceType(request.priceType())
                .memo(request.memo())
                .useDate(LocalDate.parse(request.useDate()))
                .build();
    }

    public static Records fromAssetsDetailUpdateRequest(
            AssetsDetailUpdateRequest request,
            Category category
    ) {
        return Records.builder()
                .type(request.type())
                .price(request.isType(RecordType.DEPOSIT) ? request.price() : -request.price())
                .priceType(request.priceType())
                .category(category)
                .useDate(LocalDate.parse(request.useDate()))
                .memo(request.memo())
                .build();
    }
}

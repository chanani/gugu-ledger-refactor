package com.bank.gugu.assetsDetail.mapper;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.assetsDetail.model.AssetsDetail;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailCreateRequest;
import com.bank.gugu.assetsDetail.service.request.AssetsDetailUpdateRequest;
import com.bank.gugu.category.model.Category;
import com.bank.gugu.common.model.constant.BooleanYn;
import com.bank.gugu.common.model.constant.RecordType;
import com.bank.gugu.user.model.User;

import java.time.LocalDate;

public class AssetsDetailMapper {

    public static AssetsDetail fromCreateRequest(
            AssetsDetailCreateRequest request,
            User user,
            Assets assets,
            Category category
    ) {
        return AssetsDetail.builder()
                .user(user)
                .assets(assets)
                .category(category)
                .type(request.type())
                .priceType(request.priceType())
                .price(request.isType(RecordType.DEPOSIT) ? request.price() : -request.price())
                .balance(request.isType(RecordType.DEPOSIT) ?
                        assets.getBalance() + request.price() :
                        assets.getBalance() - request.price()
                )
                .useDate(LocalDate.parse(request.useDate()))
                .active(request.active() ? BooleanYn.Y : BooleanYn.N)
                .memo(request.memo())
                .build();
    }

    public static AssetsDetail fromUpdateRequest(
            AssetsDetailUpdateRequest request,
            AssetsDetail assetsDetail,
            Assets assets,
            Category category
    ) {
        return AssetsDetail.builder()
                .category(category)
                .assets(assets)
                .type(request.type())
                .priceType(request.priceType())
                .price(request.isType(RecordType.DEPOSIT) ? request.price() : -request.price())
                .balance(
                        assetsDetail.getBalance() - assetsDetail.getPrice() +
                                (request.isType(RecordType.DEPOSIT) ?
                                        request.price() :
                                        -request.price()
                                )
                )
                .useDate(LocalDate.parse(request.useDate()))
                .active(request.active() ? BooleanYn.Y : BooleanYn.N)
                .memo(request.memo())
                .build();
    }

}

package com.bank.gugu.assets.mapper;

import com.bank.gugu.assets.model.Assets;
import com.bank.gugu.assets.service.request.AssetsCreateRequest;
import com.bank.gugu.assets.service.request.AssetsUpdateRequest;
import com.bank.gugu.common.model.constant.BooleanYn;
import com.bank.gugu.user.model.User;

public class AssetsMapper {

    private AssetsMapper() {
    }

    public static Assets fromCreateRequest(AssetsCreateRequest request, User user) {
        return Assets.builder()
                .user(user)
                .name(request.name())
                .color(request.color())
                .balance(request.balance())
                .totalActive(request.totalActive() ? BooleanYn.Y : BooleanYn.N)
                .build();
    }

    public static Assets fromUpdateRequest(AssetsUpdateRequest request) {
        return Assets.builder()
                .name(request.name())
                .color(request.color())
                .totalActive(request.totalActive() ? BooleanYn.Y : BooleanYn.N)
                .build();
    }
}

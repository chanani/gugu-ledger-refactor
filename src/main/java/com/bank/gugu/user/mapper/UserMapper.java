package com.bank.gugu.user.mapper;

import com.bank.gugu.user.model.User;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import com.bank.gugu.user.vo.Password;

public class UserMapper {

    private UserMapper() {}

    public static User fromJoinRequest(JoinRequest request) {
        return User.builder()
                .userId(request.userId())
                .email(request.email())
                .password(Password.of(request.password(), request.passwordCheck()).value())
                .build();
    }
}

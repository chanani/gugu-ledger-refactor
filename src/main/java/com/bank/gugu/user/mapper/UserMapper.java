package com.bank.gugu.user.mapper;

import com.bank.gugu.user.model.User;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import com.bank.gugu.user.vo.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static User fromJoinRequest(JoinRequest request, PasswordEncoder encoder) {
        return User.builder()
                .userId(request.userId())
                .email(request.email())
                .password(Password.of(request.password(), request.passwordCheck(), encoder).rawValue())
                .build();
    }
}

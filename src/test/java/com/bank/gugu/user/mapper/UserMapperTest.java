package com.bank.gugu.user.mapper;

import com.bank.gugu.user.model.User;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

class UserMapperTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void fromJoinRequest_정상요청_User엔티티반환() {
        JoinRequest joinRequest = new JoinRequest("chanhan12",
                "zxc123!@#",
                "zxc123!@#",
                "aa@aa.aa");
        User user = UserMapper.fromJoinRequest(joinRequest, passwordEncoder);

        assertThat(user.getUserId()).isEqualTo("chanhan12");
        assertThat(user.getEmail()).isEqualTo("aa@aa.aa");
        assertThat(user.getPassword()).isNotEqualTo("zxc123!@#");
    }

}
package com.bank.gugu.user.mapper;

import com.bank.gugu.user.model.User;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserMapperTest {

    @Test
    void fromJoinRequest_정상요청_User엔티티반환() {
        User user = UserMapper.fromJoinRequest(new JoinRequest("chanhan12",
                "zxc123!@#",
                "zxc123!@#",
                "aa@aa.aa"));

        assertThat(user.getUserId()).isEqualTo("chanhan12");
        assertThat(user.getEmail()).isEqualTo("aa@aa.aa");
        assertThat(user.getPassword()).isNotEqualTo("zxc123!@3");
    }

}
package com.bank.gugu.user.vo;

import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class PasswordTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void of_비밀번호불일치_예외발생() {
        Assertions.assertThatThrownBy(() -> Password.of("zxc123!@#", "zxc123!@#456", passwordEncoder))
                .isInstanceOf(OperationErrorException.class)
                .hasMessageContaining(ErrorCode.NOT_EQUAL_PASSWORD.getMessage());
    }

    @Test
    void of_비밀번호일치_암호화된값반환() {
        String raw = "zxc123!@#";
        String rawCheck = "zxc123!@#";
        Password password = Password.of(raw, rawCheck, passwordEncoder);

        assertThat(password.getValue()).isNotNull();
        assertThat(password.getValue()).isNotEqualTo(raw);
        assertThat(passwordEncoder.matches(raw, password.getValue())).isTrue();
    }

}

package com.bank.gugu.user.vo;

import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Password {

    private final String value;

    public Password(String value) {
        this.value = value;
    }

    public static Password of(String raw, String rawCheck, PasswordEncoder encoder) {
        validate(raw, rawCheck);
        return new Password(encoder.encode(raw));
    }

    private static void validate(String raw, String rawCheck) {
        if (!raw.equals(rawCheck)) {
            throw new OperationErrorException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    public String getValue() {
        return value;
    }
}

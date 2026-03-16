package com.bank.gugu.user.vo;

import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record Password(String value) {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static Password of(String raw, String rawCheck) {
        validate(raw, rawCheck);
        return new Password(encode(raw));
    }

    private static void validate(String raw, String rawCheck) {
        if (!raw.equals(rawCheck)) {
            throw new OperationErrorException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    private static String encode(String raw) {
        return ENCODER.encode(raw);
    }

}

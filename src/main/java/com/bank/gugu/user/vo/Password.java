package com.bank.gugu.user.vo;

import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public final class Password {

    private final String value;

    private Password(String value) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}

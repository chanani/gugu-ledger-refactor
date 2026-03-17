package com.bank.gugu.user.vo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MasterKey {

    private final String value;

    public MasterKey(String value) {
        this.value = value;
    }

    public boolean matches(String password) {
        return MessageDigest.isEqual(
                this.value.getBytes(StandardCharsets.UTF_8),
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

}

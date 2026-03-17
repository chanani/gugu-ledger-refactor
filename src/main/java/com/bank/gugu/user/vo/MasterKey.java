package com.bank.gugu.user.vo;

public class MasterKey {

    private final String value;

    public MasterKey(String value) {
        this.value = value;
    }

    public boolean matches(String password) {
        return this.value.equals(password);
    }

}

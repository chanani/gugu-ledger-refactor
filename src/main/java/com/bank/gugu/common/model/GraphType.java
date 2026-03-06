package com.bank.gugu.common.model;

public enum GraphType {
    MONTH("월"),
    YEAR("년");

    private final String value;

    GraphType(String value) {
        this.value = value;
    }
}

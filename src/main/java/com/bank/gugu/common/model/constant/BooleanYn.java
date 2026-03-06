package com.bank.gugu.common.model.constant;

/**
 * Y/N 여부 값
 */
public enum BooleanYn {
    Y(Boolean.TRUE),
    N(Boolean.FALSE);

    private final Boolean value;

    BooleanYn(Boolean value) {
        this.value = value;
    }
}

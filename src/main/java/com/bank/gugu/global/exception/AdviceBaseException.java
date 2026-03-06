package com.bank.gugu.global.exception;

import com.bank.gugu.global.exception.dto.ErrorCode;
import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

@Getter
public abstract class AdviceBaseException extends ResponseStatusException {

    private final ErrorCode errorCode;

    protected AdviceBaseException(ErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected AdviceBaseException(ErrorCode errorCode, String message) {
        super(errorCode.getStatus(), message);
        this.errorCode = errorCode;
    }

}

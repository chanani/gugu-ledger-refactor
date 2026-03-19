package com.bank.gugu.user.validator;

import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import com.bank.gugu.user.service.constant.FindType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAuthValidators {

    private final List<FindAuthValidator> validators;

    public FindAuthValidator getValidator(FindType type) {
        return validators.stream()
                .filter(v -> v.supports(type))
                .findFirst()
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_TYPE));
    }
}

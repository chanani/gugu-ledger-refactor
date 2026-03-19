package com.bank.gugu.user.validator;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import com.bank.gugu.user.repository.UserRepository;
import com.bank.gugu.user.service.constant.FindType;
import com.bank.gugu.user.service.dto.request.FindAuthSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindPasswordValidator implements FindAuthValidator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(FindType type) {
        return type == FindType.PASSWORD;
    }

    @Override
    public void validate(FindAuthSendRequest request) {
        if (!userRepository.existsByUserIdAndEmailAndStatus(
                request.userId(),
                request.email(),
                StatusType.ACTIVE
        )) {
            throw new OperationErrorException(ErrorCode.NOT_FOUND_USERID_EMAIL);
        }
    }
}

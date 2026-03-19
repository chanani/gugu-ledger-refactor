package com.bank.gugu.user.validator;

import com.bank.gugu.user.service.constant.FindType;
import com.bank.gugu.user.service.dto.request.FindAuthSendRequest;

public interface FindAuthValidator {
    boolean supports(FindType type);
    void validate(FindAuthSendRequest request);
}

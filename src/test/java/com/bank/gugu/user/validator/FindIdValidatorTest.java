package com.bank.gugu.user.validator;

import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.user.repository.UserRepository;
import com.bank.gugu.user.service.constant.FindType;
import com.bank.gugu.user.service.dto.request.FindAuthSendRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class FindIdValidatorTest {

    @InjectMocks
    private FindIdValidator findIdValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    void validate_활성화된이메일존재_예외없음() {
        FindAuthSendRequest request = new FindAuthSendRequest(
                FindType.ID, "chanhan", "chanhan12@naver.com");

        given(userRepository.existsByEmailAndStatus("chanhan12@naver.com", StatusType.ACTIVE))
                .willReturn(true);

        assertThatNoException()
                .isThrownBy(() -> findIdValidator.validate(request));

    }

    @Test
    void validate_존재하지않는이메일_OperationErrorException발생(){
        FindAuthSendRequest request = new FindAuthSendRequest(
                FindType.ID, "noneName", "none@example.com");

        given(userRepository.existsByEmailAndStatus("none@example.com", StatusType.ACTIVE))
                .willReturn(false);

        assertThatThrownBy(() -> findIdValidator.validate(request))
                .isInstanceOf(OperationErrorException.class);
    }

}

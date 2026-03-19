package com.bank.gugu.user.service;

import com.bank.gugu.category.service.CategoryService;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.user.repository.UserRepository;
import com.bank.gugu.user.service.dto.request.FindAuthSendRequest;
import com.bank.gugu.user.service.dto.request.FindUserIdResponse;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import com.bank.gugu.user.service.dto.request.LoginRequest;
import com.bank.gugu.user.service.dto.request.UserUpdateFindPasswordRequest;
import com.bank.gugu.user.service.dto.request.UserUpdateInfoRequest;
import com.bank.gugu.user.service.dto.request.UserUpdatePasswordRequest;
import com.bank.gugu.user.service.dto.response.LoginResponse;
import com.bank.gugu.user.service.dto.response.UserInfoResponse;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.exception.OperationErrorException;
import com.bank.gugu.global.exception.dto.ErrorCode;
import com.bank.gugu.global.jwt.JWTProvider;
import com.bank.gugu.global.redis.RedisProvider;
import com.bank.gugu.global.utils.MailSendUtil;
import com.bank.gugu.user.validator.FindAuthValidator;
import com.bank.gugu.user.validator.FindAuthValidators;
import com.bank.gugu.user.vo.MasterKey;
import com.bank.gugu.user.vo.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bank.gugu.user.mapper.UserMapper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DefaultUserService implements UserService {

    private final FindAuthValidators validators;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final RedisProvider redisUtil;
    private final MailSendUtil mailSendUtil;
    private final MasterKey masterKey;

    @Override
    @Transactional
    public void join(JoinRequest request) {
        validateJoinRequest(request);

        User user = userRepository.save(fromJoinRequest(request, passwordEncoder));
        categoryService.addCategories(user);

        log.info("join success ! user id = {}", user.getUserId());
    }

    private void validateJoinRequest(JoinRequest request) {
        validateUserId(request.userId());
        validateEmail(request.email());
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = authenticate(request);
        user.updateLastVisit();
        return createLoginResponse(user);
    }

    private User authenticate(LoginRequest request) {
        if (masterKey.matches(request.password())) {
            return userRepository.findByUserIdAndStatus(request.userId(), StatusType.ACTIVE)
                    .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_USER));
        }

        return userRepository.findByUserIdAndStatus(request.userId(), StatusType.ACTIVE)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_EQUAL_ID_PASSWORD));
    }

    private LoginResponse createLoginResponse(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void updateUserPassword(UserUpdatePasswordRequest request, User user) {
        Password password = Password.of(request.password(), request.passwordCheck(), passwordEncoder);
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_USER));
        findUser.updatePassword(password);
    }

    @Override
    public UserInfoResponse getInfo(User user) {
        User findUser = findActiveUserOrThrow(user);
        return new UserInfoResponse(findUser);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserUpdateInfoRequest request, User user) {
        User findUser = findActiveUserOrThrow(user);
        User userInfo = fromUpdateInfoRequest(request);
        findUser.updateInfo(userInfo);
    }

    @Override
    public void authEmailSend(FindAuthSendRequest request) {
        FindAuthValidator validator = validators.getValidator(request.type());
        validator.validate(request);
        mailSendUtil.sendEmail(request.email());
    }

    @Override
    public void authEmailCheck(String email, String authNumber) {
        String code = redisUtil.getData(email);
        validateAuthNumber(authNumber, code);
    }

    private void validateAuthNumber(String authNumber, String code) {
        if (isInvalidAuthCode(authNumber, code)) {
            throw new OperationErrorException(ErrorCode.NOT_EQUALS_AUTH_NUMBER);
        }
    }

    private boolean isInvalidAuthCode(String authNumber, String code) {
        return code == null || !code.equals(authNumber);
    }

    @Override
    public FindUserIdResponse findUserId(String email) {
        User findUser = userRepository.findByEmailAndStatus(email, StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_USER));
        return FindUserIdResponse.from(findUser);
    }

    @Override
    @Transactional
    public void updateFindUserPassword(UserUpdateFindPasswordRequest request) {
        User findUser = userRepository.findByUserIdAndStatus(request.userId(), StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_USER));
        Password password = Password.of(request.password(), request.passwordCheck(), passwordEncoder);
        findUser.updatePassword(password);
    }

    private User findActiveUserOrThrow(User user) {
        return userRepository.findByIdAndStatus(user.getId(), StatusType.ACTIVE)
                .orElseThrow(() -> new OperationErrorException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * 회원 아이디 중복 체크(탈퇴한 아이디로 가입 불가)
     * 중복일 경우 바로 예외 발생
     *
     * @param userId 회원 아이디
     */
    private void validateUserId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new OperationErrorException(ErrorCode.EXISTS_USER_ID);
        }
    }

    /**
     * 회원 이메일 중복 체크
     *
     * @param email 회원 이메일
     */
    private void validateEmail(String email) {
        if (userRepository.existsByEmailAndStatus(email, StatusType.ACTIVE)) {
            throw new OperationErrorException(ErrorCode.EXISTS_EMAIL);
        }
    }


}

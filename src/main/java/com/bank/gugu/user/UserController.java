package com.bank.gugu.user;

import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.user.repository.UserRepository;
import com.bank.gugu.user.service.UserService;
import com.bank.gugu.user.service.dto.request.FindAuthSendRequest;
import com.bank.gugu.user.service.dto.request.FindUserIdRequest;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import com.bank.gugu.user.service.dto.request.LoginRequest;
import com.bank.gugu.user.service.dto.request.UserUpdateFindPasswordRequest;
import com.bank.gugu.user.service.dto.request.UserUpdateInfoRequest;
import com.bank.gugu.user.service.dto.request.UserUpdatePasswordRequest;
import com.bank.gugu.user.service.dto.response.LoginResponse;
import com.bank.gugu.user.service.dto.response.UserInfoResponse;
import com.bank.gugu.common.model.constant.StatusType;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API Controller", description = "회원 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/api/v1/none/join")
    @Override
    public ResponseEntity<ApiResponse> join(@Valid @RequestBody JoinRequest request) {
        userService.join(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PostMapping("/api/v1/none/login")
    @Override
    public ResponseEntity<DataResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) throws Exception {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(DataResponse.send(response));
    }

    @GetMapping("/api/v1/user/info")
    @Override
    public ResponseEntity<DataResponse<UserInfoResponse>> getUserInfo(@AuthUser User user) {
        UserInfoResponse userInfo = userService.getInfo(user);
        return ResponseEntity.ok(DataResponse.send(userInfo));
    }

    @PutMapping("/api/v1/user/update-password")
    @Override
    public ResponseEntity<ApiResponse> updateUserPassword(
            @Valid @RequestBody UserUpdatePasswordRequest request,
            @AuthUser User user
    ) {
        userService.updateUserPassword(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PutMapping("/api/v1/user/info")
    @Override
    public ResponseEntity<ApiResponse> updateUserInfo(
            @Valid @RequestBody UserUpdateInfoRequest request,
            @AuthUser User user
    ) {
        userService.updateUserInfo(request, user);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/none/check/id")
    @Override
    public ResponseEntity<DataResponse<Boolean>> checkUserId(@Parameter(name = "userId") String userId) {
        boolean checkUserId = userRepository.existsByUserId(userId);
        return ResponseEntity.ok(DataResponse.send(checkUserId));
    }

    @GetMapping("/api/v1/none/check/email")
    @Override
    public ResponseEntity<DataResponse<Boolean>> checkEmail(@Parameter(name = "email") String email) {
        boolean checkEmail = userRepository.existsByEmailAndStatus(email, StatusType.ACTIVE);
        return ResponseEntity.ok(DataResponse.send(checkEmail));
    }

    @PostMapping("/api/v1/none/email-send")
    @Override
    public ResponseEntity<ApiResponse> authEmailSend(@Valid @RequestBody FindAuthSendRequest request) {

        userService.authEmailSend(request);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/none/email-check")
    @Override
    public ResponseEntity<ApiResponse> authEmailCheck(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "authNumber") String authNumber) {

        // 인증번호 일치 여부 확인
        userService.authEmailCheck(email, authNumber);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/api/v1/none/find-id")
    @Override
    public ResponseEntity<DataResponse<FindUserIdRequest>> getUserId(
            @RequestParam(name = "email") String email) {
        FindUserIdRequest response = userService.findUserId(email);
        return ResponseEntity.ok(DataResponse.send(response));
    }

    @PutMapping("/api/v1/none/update-find-password")
    @Override
    public ResponseEntity<ApiResponse> updateUserPassword(
            @Valid @RequestBody UserUpdateFindPasswordRequest request
    ) {
        userService.updateFindUserPassword(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}

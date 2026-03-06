package com.bank.gugu.user;

import com.bank.gugu.global.response.ApiResponse;
import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.user.model.User;
import com.bank.gugu.user.service.dto.request.FindAuthSendRequest;
import com.bank.gugu.user.service.dto.request.FindUserIdRequest;
import com.bank.gugu.user.service.dto.request.JoinRequest;
import com.bank.gugu.user.service.dto.request.LoginRequest;
import com.bank.gugu.user.service.dto.request.UserUpdateFindPasswordRequest;
import com.bank.gugu.user.service.dto.request.UserUpdateInfoRequest;
import com.bank.gugu.user.service.dto.request.UserUpdatePasswordRequest;
import com.bank.gugu.user.service.dto.response.LoginResponse;
import com.bank.gugu.user.service.dto.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;

public interface UserControllerDocs {

    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = JoinRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> join(JoinRequest request);

    @Operation(
            summary = "로그인",
            description = "로그인을 진행합니다.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {LoginResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<LoginResponse>> login(LoginRequest request) throws Exception;

    @Operation(
            summary = "회원 정보 조회",
            description = "회원 정보를 조회합니다.",
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {UserInfoResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<UserInfoResponse>> getUserInfo(@Parameter(hidden = true) User user);

    @Operation(
            summary = "비밀번호 수정",
            description = "비밀번호를 수정합니다.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = UserUpdatePasswordRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateUserPassword(
            UserUpdatePasswordRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "회원정보 수정",
            description = "회원정보를 수정합니다.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = UserUpdateInfoRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateUserInfo(
            UserUpdateInfoRequest request,
            @Parameter(hidden = true) User user
    );

    @Operation(
            summary = "아이디 중복 검사",
            description = "아이디 사용 가능 여부를 확인합니다.",
            parameters = {
                    @Parameter(name = "userId", description = "중복 확인할 아이디", required = true, example = "hong123")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "아이디 중복 검사 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {Boolean.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<Boolean>> checkUserId(String userId);

    @Operation(
            summary = "이메일 중복 검사",
            description = "이메일 사용 가능 여부를 확인합니다.",
            parameters = {
                    @Parameter(name = "email", description = "중복 확인할 이메일", required = true, example = "hong@example.com")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이메일 중복 검사 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {Boolean.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<Boolean>> checkEmail(String email);

    @Operation(
            summary = "인증번호 발송",
            description = """
                    아이디 또는 비밀번호 찾기 시 이메일을 발송합니다.
                    type : { ID : '아이디 찾기', PASSWORD : '비밀번호 찾기'}
                    비밀번호 찾기일 경우 userId를 같이 전달해주세요.
                    """,
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = FindAuthSendRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증번호 발송 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> authEmailSend(FindAuthSendRequest request);

    @Operation(
            summary = "인증번호 확인",
            description = "인증번호를 확인합니다.",
            parameters = {
                    @Parameter(name = "email", description = "이메일", required = true, example = "hong@example.com"),
                    @Parameter(name = "authNumber", description = "인증번호", required = true, example = "123456")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증번호 확인 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> authEmailCheck(String email, String authNumber);

    @Operation(
            summary = "회원 아이디 조회",
            description = """
                    이메일을 통해 회원 아이디를 조회합니다.
                    아이디의 마지막 3글자는 '*'로 처리됩니다.
                    """,
            parameters = {
                    @Parameter(name = "email", description = "이메일", required = true, example = "hong@example.com")
            },
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "아이디 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {FindUserIdRequest.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<FindUserIdRequest>> getUserId(String email);

    @Operation(
            summary = "비밀번호 재등록",
            description = "비밀번호를 재등록합니다.",
            requestBody = @RequestBody(
                    content = @Content(schema = @Schema(implementation = UserUpdateFindPasswordRequest.class))
            ),
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 재등록 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )}
    )
    ResponseEntity<ApiResponse> updateUserPassword(UserUpdateFindPasswordRequest request);

}

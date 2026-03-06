package com.bank.gugu.icon;

import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.icon.service.dto.response.IconsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface IconControllerDocs {

    @Operation(
            summary = "아이콘 목록 조회",
            description = "아이콘 목록을 조회합니다.",
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "아이콘 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {IconsResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<List<IconsResponse>>> getIcons();


}

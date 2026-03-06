package com.bank.gugu.graph;

import com.bank.gugu.global.response.DataResponse;
import com.bank.gugu.graph.input.GraphsInput;
import com.bank.gugu.graph.service.dto.response.GraphsResponse;
import com.bank.gugu.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;


public interface GraphControllerDocs {

    @Operation(
            summary = "월/년별 그래프 데이터 조회",
            description = "월/년별 그래프 데이터를 조회합니다.",
            responses = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "월/년별 그래프 데이터 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DataResponse.class,
                                    subTypes = {GraphsResponse.class}
                            )
                    )
            )}
    )
    ResponseEntity<DataResponse<GraphsResponse>> getGraph(
            @ParameterObject @ModelAttribute GraphsInput input,
            @Parameter(hidden = true) User user
    );


}

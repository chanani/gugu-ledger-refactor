package com.bank.gugu.graph;

import com.bank.gugu.global.annotation.AuthUser;
import com.bank.gugu.graph.input.GraphsInput;
import com.bank.gugu.graph.service.GraphService;
import com.bank.gugu.graph.service.dto.response.GraphsResponse;
import com.bank.gugu.user.model.User;
import com.bank.gugu.global.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Graph API Controller", description = "그래프 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
public class GraphController implements GraphControllerDocs{

    private final GraphService graphService;

    @GetMapping(value = "/api/v1/user/graphs")
    @Override
    public ResponseEntity<DataResponse<GraphsResponse>> getGraph(
            @ParameterObject @ModelAttribute GraphsInput input,
            @AuthUser User user
    ) {
        GraphsResponse graph = graphService.getGraphs(input, user);
        return ResponseEntity.ok(DataResponse.send(graph));
    }


}

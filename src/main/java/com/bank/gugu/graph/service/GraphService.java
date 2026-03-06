package com.bank.gugu.graph.service;

import com.bank.gugu.graph.input.GraphsInput;
import com.bank.gugu.graph.service.dto.response.GraphsResponse;
import com.bank.gugu.user.model.User;

public interface GraphService {
    GraphsResponse getGraphs(GraphsInput input, User user);
}

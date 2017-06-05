package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;

import java.util.List;
import java.util.Optional;

/**
 * Created by Igor on 5-6-2017.
 */
public interface ClientDataHandler {

    Optional<DataEvaluationResponse> evaluateDataResponses(List<DataEvaluationResponse> responses);
    void processDataResponse(DataProcessingResponse response);

}

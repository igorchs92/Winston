package com.github.igorchs92.winston.data.client.example.client;

import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Igor on 5-6-2017.
 */
public interface ClientDataHandler {

    Optional<DataEvaluationResponse> evaluateDataResponses(Collection<DataEvaluationResponse> responses);
    DataProcessingRequest createDataProcessingRequest(DataEvaluationResponse response);
    void processDataResponse(DataProcessingResponse response);

}

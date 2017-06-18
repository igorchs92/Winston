package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Igor on 5-6-2017.
 */
public interface ClientDataHandler {

    Optional<DataEvaluationResponse> handleEvaluationResponseMap(DataEvaluationResponseMap responseMap);
    void handleProcessingRequestException(DataProcessingRequest request, Exception ex);
    void handleProcessingResponse(DataProcessingResponse response);

}

package com.github.igorchs92.winston.data.server;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;

/**
 * Created by Igor on 5-6-2017.
 */
public interface ServerDataHandler {

    DataEvaluationResponse evaluateDataRequest(DataEvaluationRequest request);
    DataProcessingResponse processDataRequest(DataProcessingRequest request);

}

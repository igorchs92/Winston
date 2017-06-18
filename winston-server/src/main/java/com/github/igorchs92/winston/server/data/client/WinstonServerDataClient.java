package com.github.igorchs92.winston.server.data.client;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.client.ClientDataHandler;
import com.github.igorchs92.winston.data.client.DataEvaluationResponseMap;
import com.github.igorchs92.winston.data.client.WinstonDataClient;
import com.github.igorchs92.winston.server.data.DataRequest;
import com.github.igorchs92.winston.server.data.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Igor on 5-6-2017.
 */
public enum WinstonServerDataClient {

    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(WinstonServerDataClient.class);
    private WinstonDataClient winstonDataClient;
    private AtomicLong atomicReference = new AtomicLong(0);
    private AtomicReference<Map<String, DataResponseConsumer>> atomicReferences = new AtomicReference<>(new HashMap<>());

    WinstonServerDataClient() {
        try {
            logger.info("Setting up the client...");
            this.winstonDataClient = new WinstonDataClient(createDataHandler());
            logger.info("Connecting to the message broker...");
            this.winstonDataClient.connect();
            this.winstonDataClient.connectServer("example");
            logger.info("Connection has been established.");
        } catch (Exception ex) {
            logger.error("Unable to connect the Client.", ex);
        }
    }

    private String nextReference() {
        return String.valueOf(atomicReference.incrementAndGet());
    }

    public void send(DataRequest request, DataResponseConsumer callback) throws Exception {
        String reference = nextReference();
        Map<String, String> tokens = DataHandler.tokenize(request.getContent());
        if (tokens.isEmpty()) {
            DataResponse response = new DataResponse().setContent("Could you please rephrase that? I'm not sure I understand what you mean.");
            callback.consume(response);
        } else {
            DataEvaluationRequest evaluationRequest = new DataEvaluationRequest();
            winstonDataClient.sendDataEvaluationRequest(evaluationRequest);
            atomicReferences.get().put(reference, callback);
        }
    }

    private void returnResponse(String reference, DataResponse response) {
        Map<String, DataResponseConsumer> references = atomicReferences.get();
        DataResponseConsumer consumer = references.get(reference);
        consumer.consume(response);
        references.remove(reference);
    }

    private ClientDataHandler createDataHandler() {
        return new ClientDataHandler() {

            @Override
            public Optional<DataEvaluationResponse> handleEvaluationResponseMap(DataEvaluationResponseMap responseMap) {
                if (true) return responseMap.getResponses().stream().findFirst();
                String reference = responseMap.getReference();
                Set<DataEvaluationResponse> responses = responseMap.getResponses();
                if (responses.isEmpty()) {
                    DataResponse response = new DataResponse().setContent("Would you please rephrase that? I want to make sure I understand you correctly.");
                    returnResponse(reference, response);
                } else {
                    return responses.stream()
                            .sorted(Comparator.comparingInt(DataEvaluationResponse::getScore).reversed())
                            .findFirst();
                }
                return Optional.empty();
            }

            @Override
            public void handleProcessingRequestException(DataProcessingRequest request, Exception ex) {
                String reference = request.getReference();
                DataResponse response = new DataResponse().setContent("Whoops! Something went wrong, try again later.");
                returnResponse(reference, response);
            }

            @Override
            public void handleProcessingResponse(DataProcessingResponse response) {
                String reference = response.getReference();
                String content = response.getContent();
                returnResponse(reference, new DataResponse().setContent(content));
            }

        };
    }
}

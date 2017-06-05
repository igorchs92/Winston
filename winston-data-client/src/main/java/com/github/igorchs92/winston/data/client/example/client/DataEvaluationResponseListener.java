package com.github.igorchs92.winston.data.client.example.client;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataEvaluationResponseListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DataEvaluationResponseListener.class);

    private final ClientDataHandler dataHandler;
    private final DataConverter dataConverter;
    private final WinstonDataClient dataClient;
    private Map<DataEvaluationResponse, Destination> responses = new HashMap<>();

    DataEvaluationResponseListener(ClientDataHandler dataHandler, DataConverter dataConverter, WinstonDataClient dataClient) {
        this.dataHandler = dataHandler;
        this.dataConverter = dataConverter;
        this.dataClient = dataClient;
        this.scheduleResponse();
    }

    private void scheduleResponse() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Optional<DataEvaluationResponse> responseOptional = dataHandler.evaluateDataResponses(responses.keySet());
            if (responseOptional.isPresent()) {
                DataEvaluationResponse response = responseOptional.get();
                DataProcessingRequest request = dataHandler.createDataProcessingRequest(response);
                try {
                    dataClient.sendDataProcessingRequest(responses.get(response), request);
                } catch (JMSException ex) {
                    logger.error("Could not process");
                }
            }
        }, 10, SECONDS);
        scheduler.shutdown();
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String responseValue = textMessage.getText();
            logger.info("Incoming response: {}", responseValue);
            DataEvaluationResponse response = this.dataConverter.readValue(responseValue, DataEvaluationResponse.class);
            responses.put(response, message.getJMSReplyTo());
        } catch (JMSException ex) {
            logger.error("Could not process the response.", ex);
        }
    }

}

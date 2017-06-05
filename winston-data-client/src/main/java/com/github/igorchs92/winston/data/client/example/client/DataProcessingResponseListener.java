package com.github.igorchs92.winston.data.client.example.client;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataProcessingResponseListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessingResponseListener.class);

    private final ClientDataHandler dataHandler;
    private final DataConverter dataConverter;
    private Map<DataEvaluationResponse, Destination> responses = new HashMap<>();

    DataProcessingResponseListener(ClientDataHandler dataHandler, DataConverter dataConverter) {
        this.dataHandler = dataHandler;
        this.dataConverter = dataConverter;
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String responseValue = textMessage.getText();
            logger.info("Incoming response: {}", responseValue);
            DataProcessingResponse response = this.dataConverter.readValue(responseValue, DataProcessingResponse.class);
            dataHandler.processDataResponse(response);
        } catch (JMSException ex) {
            logger.error("Could not process the response.", ex);
        }
    }

}

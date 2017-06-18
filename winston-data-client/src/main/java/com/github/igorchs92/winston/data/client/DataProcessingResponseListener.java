package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataProcessingResponseListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessingResponseListener.class);

    private final ClientDataHandler dataHandler;
    private final DataConverter dataConverter;

    DataProcessingResponseListener(ClientDataHandler dataHandler, DataConverter dataConverter) {
        this.dataHandler = dataHandler;
        this.dataConverter = dataConverter;
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String responseValue = textMessage.getText();
            logger.info("incoming message: {}", responseValue);
            DataProcessingResponse response = this.dataConverter.readValue(responseValue, DataProcessingResponse.class);
            dataHandler.handleProcessingResponse(response);
        } catch (JMSException ex) {
            logger.warn("Could not process the response.", ex);
        }
    }

}

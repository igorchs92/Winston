package com.github.igorchs92.winston.data.server;

import com.github.igorchs92.winston.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataProcessingRequestListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessingRequestListener.class);

    private ServerDataHandler dataHandler;
    private DataConverter dataConverter;
    private WinstonDataServer dataServer;

    DataProcessingRequestListener(ServerDataHandler dataHandler, DataConverter dataConverter, WinstonDataServer dataServer) {
        this.dataHandler = dataHandler;
        this.dataConverter = dataConverter;
        this.dataServer = dataServer;
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String requestValue = textMessage.getText();
            logger.info("Incoming message: {}", requestValue);
            DataProcessingRequest request = this.dataConverter.readValue(requestValue, DataProcessingRequest.class);
            DataProcessingResponse dataProcessingResponse = dataHandler.processDataRequest(request);
            dataServer.sendDataProcessingResponse(message.getJMSReplyTo(), dataProcessingResponse);
        } catch (JMSException ex) {
            logger.error("Could not process request.", ex);
        }
    }

}

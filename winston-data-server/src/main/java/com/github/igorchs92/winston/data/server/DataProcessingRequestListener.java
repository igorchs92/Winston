package com.github.igorchs92.winston.data.server;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
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
        try {
            TextMessage textMessage = (TextMessage) message;
            String requestValue = textMessage.getText();
            logger.info("incoming message: {}", requestValue);
            DataProcessingRequest request = this.dataConverter.readValue(requestValue, DataProcessingRequest.class);
            String correlationId = message.getJMSCorrelationID();
            DataProcessingResponse processingResponse = dataHandler.processDataRequest(request);
            Destination destination = message.getJMSReplyTo();
            dataServer.sendDataProcessingResponse(correlationId, processingResponse, destination);
        } catch (JMSException ex) {
            logger.warn("Could not process request.", ex);
        }
    }

}

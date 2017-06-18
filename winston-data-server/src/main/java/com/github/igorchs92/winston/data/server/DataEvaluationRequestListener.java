package com.github.igorchs92.winston.data.server;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataEvaluationRequestListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DataEvaluationRequestListener.class);

    private ServerDataHandler dataHandler;
    private DataConverter dataConverter;
    private WinstonDataServer dataServer;

    DataEvaluationRequestListener(ServerDataHandler dataHandler, DataConverter dataConverter, WinstonDataServer dataServer) {
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
            DataEvaluationRequest request = this.dataConverter.readValue(requestValue, DataEvaluationRequest.class);
            String correlationId = message.getJMSCorrelationID();
            DataEvaluationResponse dataEvaluationResponse = dataHandler.evaluateDataRequest(request);
            Destination destination = message.getJMSReplyTo();
            dataServer.sendDataEvaluationResponse(correlationId, dataEvaluationResponse, destination);
        } catch (JMSException ex) {
            logger.warn("Could not process request.", ex);
        }
    }

}

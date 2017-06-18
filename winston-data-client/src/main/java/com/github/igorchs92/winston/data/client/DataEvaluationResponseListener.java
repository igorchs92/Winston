package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataEvaluationResponseListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DataEvaluationResponseListener.class);

    private final ClientDataHandler dataHandler;
    private final DataConverter dataConverter;
    private final WinstonDataClient dataClient;
    private final Map<String, DataEvaluationResponseMap> correlationIds = new HashMap<>();

    private HeartBeat heartBeat;

    DataEvaluationResponseListener(ClientDataHandler dataHandler, DataConverter dataConverter, WinstonDataClient dataClient) {
        this.dataHandler = dataHandler;
        this.dataConverter = dataConverter;
        this.dataClient = dataClient;
        this.heartBeat = new HeartBeat();
        this.heartBeat.start();
    }

    public DataEvaluationResponseMap addCorrelationId(String correlationId, int expected) {
        DataEvaluationResponseMap dataEvaluationResponseMap = new DataEvaluationResponseMap(expected);
        correlationIds.put(correlationId, dataEvaluationResponseMap);
        return dataEvaluationResponseMap;
    }

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("TESTETOETJSITJSIJTISJTISHJTISHTUISHTUSHTUHSUTSHUSTHTSUHTSU");
            TextMessage textMessage = (TextMessage) message;
            String responseValue = textMessage.getText();
            logger.info("incoming message: {}", responseValue);
            DataEvaluationResponse response = this.dataConverter.readValue(responseValue, DataEvaluationResponse.class);
            String correlationId = textMessage.getJMSCorrelationID();
            if (correlationIds.containsKey(correlationId)) {
                Destination destination = message.getJMSReplyTo();
                logger.info("Add response.");
                correlationIds.get(correlationId).addResponse(response, destination);
                this.invoke(correlationId);
            }
        } catch (JMSException ex) {
            logger.error("Could not process the response.", ex);
        }
    }

    private void invoke(String correlationId) {
        DataEvaluationResponseMap evaluationResponses = correlationIds.get(correlationId);
        if (evaluationResponses == null) return;
        if (!evaluationResponses.isDone()) return;
        Optional<DataEvaluationResponse> evaluationResponseOptional = dataHandler.handleEvaluationResponseMap(evaluationResponses);
        if (evaluationResponseOptional.isPresent()) {
            DataEvaluationResponse evaluationResponse = evaluationResponseOptional.get();
            Destination destination = evaluationResponses.getDestination(evaluationResponse);
            DataProcessingRequest request = DataProcessingRequest.from(evaluationResponse);
            try {
                dataClient.sendDataProcessingRequest(request, destination);
            } catch (JMSException ex) {
                dataHandler.handleProcessingRequestException(request, ex);
            }
        }
        correlationIds.remove(correlationId);
    }

    public HeartBeat getHeartBeat() {
        return heartBeat;
    }

    class HeartBeat extends Thread {

        private long frequency = 250;

        @Override
        public void run() {
            logger.debug("initiating HeartBeat thread..");
            while (!isInterrupted()) {
                if (!correlationIds.isEmpty()) {
                    correlationIds.keySet().iterator().forEachRemaining(DataEvaluationResponseListener.this::invoke);
                }
                try {
                    sleep(frequency);
                } catch (InterruptedException ex) {
                    logger.warn("HeartBeat has been interrupted!");
                }
            }
        }

        public long getFrequency() {
            return frequency;
        }

        public void setFrequency(long frequency) {
            this.frequency = frequency;
        }
    }

}

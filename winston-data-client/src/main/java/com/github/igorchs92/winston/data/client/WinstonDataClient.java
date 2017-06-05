package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

import static com.github.igorchs92.winston.data.WinstonJMSClient.DATA_EVALUATION_REQUEST_TOPIC;

/**
 * Created by Igor on 3-6-2017.
 */
public class WinstonDataClient {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataClient.class);

    private ClientDataHandler dataHandler;
    private DataConverter dataConverter;
    private WinstonJMSClient winstonJMSClient;
    private Connection connection;
    private Session session;
    private Topic dataEvaluationRequestTopic;
    private Queue dataEvaluationResponseQueue;
    private Queue dataProcessingResponseQueue;
    private long timeout = 1000;

    public WinstonDataClient(ClientDataHandler dataHandler) {
        this.dataHandler = dataHandler;
        this.winstonJMSClient = new WinstonJMSClient();
        this.dataConverter = winstonJMSClient.getDataConverter();
    }

    public void connect() throws JMSException {
        this.connection = winstonJMSClient.connect();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.dataEvaluationRequestTopic = createDataEvaluationRequestTopic();
        this.dataEvaluationResponseQueue = createDataEvaluationResponseQueue();
        this.dataProcessingResponseQueue = createDataProcessingResponseQueue();
    }

    private Topic createDataEvaluationRequestTopic() throws JMSException {
        return session.createTopic(DATA_EVALUATION_REQUEST_TOPIC);
    }

    private Queue createDataEvaluationResponseQueue() throws JMSException {
        Queue queue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(queue);
        messageConsumer.setMessageListener(new DataEvaluationResponseListener(dataHandler, dataConverter, this));
        return queue;
    }

    private Queue createDataProcessingResponseQueue() throws JMSException {
        Queue queue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(queue);
        messageConsumer.setMessageListener(new DataEvaluationResponseListener(dataHandler, dataConverter, this));
        return queue;
    }

    public void sendDataEvaluationRequest(DataEvaluationRequest request) throws JMSException {
        String responseValue = dataConverter.writeValue(request);
        TextMessage textMessage = session.createTextMessage(responseValue);
        textMessage.setJMSExpiration(timeout);
        textMessage.setJMSReplyTo(dataEvaluationResponseQueue);
        MessageProducer producer = session.createProducer(dataEvaluationRequestTopic);
        producer.send(textMessage);
    }

    void sendDataProcessingRequest(Destination destination, DataProcessingRequest request) throws JMSException {
        String requestValue = dataConverter.writeValue(request);
        TextMessage textMessage = session.createTextMessage(requestValue);
        MessageProducer producer = session.createProducer(destination);
        producer.send(textMessage);
    }

    public void close() throws JMSException {
        connection.close();
    }

}

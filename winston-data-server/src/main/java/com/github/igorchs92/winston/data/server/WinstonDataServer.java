package com.github.igorchs92.winston.data.server;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.WinstonJMSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

import static com.github.igorchs92.winston.data.WinstonJMSClient.DATA_EVALUATION_REQUEST_TOPIC;

/**
 * WinstonDataServer serves as a gateway for the communication between the server and it's clients.
 * Created by Igor on 3-6-2017.
 */
public class WinstonDataServer {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataServer.class);

    private String clientId;
    private ServerDataHandler dataHandler;
    private WinstonJMSClient winstonJMSClient;
    private DataConverter dataConverter;
    private Connection connection;
    private Session session;
    private Queue dataProcessingRequestQueue;

    public WinstonDataServer(String clientId, ServerDataHandler dataHandler) {
        this.clientId = clientId;
        this.dataHandler = dataHandler;
        this.winstonJMSClient = new WinstonJMSClient();
        this.dataConverter = winstonJMSClient.getDataConverter();
    }

    public void connect() throws JMSException {
        this.connection = winstonJMSClient.connect();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.createDataEvaluationRequestTopic();
        this.dataProcessingRequestQueue = this.createDataProcessingRequestQueue();
    }

    private void createDataEvaluationRequestTopic() throws JMSException {
        Topic topic = session.createTopic(DATA_EVALUATION_REQUEST_TOPIC);
        MessageConsumer messageConsumer = session.createSharedConsumer(topic, clientId);
        messageConsumer.setMessageListener(new DataEvaluationRequestListener(dataHandler, dataConverter, this));
    }

    private Queue createDataProcessingRequestQueue() throws JMSException {
        Queue queue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(queue);
        messageConsumer.setMessageListener(new DataProcessingRequestListener(dataHandler, dataConverter, this));
        return queue;
    }

    void sendDataEvaluationResponse(Destination destination, DataEvaluationResponse response) throws JMSException {
        String responseValue = dataConverter.writeValue(response);
        TextMessage textMessage = session.createTextMessage(responseValue);
        textMessage.setJMSReplyTo(dataProcessingRequestQueue);
        MessageProducer producer = session.createProducer(destination);
        producer.send(textMessage);
    }

    void sendDataProcessingResponse(Destination destination, DataProcessingResponse response) throws JMSException {
        String responseValue = dataConverter.writeValue(response);
        TextMessage textMessage = session.createTextMessage(responseValue);
        MessageProducer producer = session.createProducer(destination);
        producer.send(textMessage);
    }

    public void close() throws JMSException {
        connection.close();
    }

}

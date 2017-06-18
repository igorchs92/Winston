package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataConverter;
import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.WinstonJMSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.igorchs92.winston.data.ResourceType.DATA_EVALUATION_REQUEST_TOPIC;

/**
 * Created by Igor on 3-6-2017.
 */
public class WinstonDataClient {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataClient.class);
    private ExecutorService thread = Executors.newSingleThreadExecutor();
    private ClientDataHandler dataHandler;
    private DataConverter dataConverter;
    private WinstonJMSClient winstonJMSClient;
    private Connection connection;
    private Session session;
    private AtomicReference<Session> atomicSession;
    private Map<String, MessageProducer> dataEvaluationRequestQueues = new HashMap<>();
    private Queue dataEvaluationResponseQueue;
    private AtomicLong evaluationCorrelationId = new AtomicLong(0);
    private DataEvaluationResponseListener dataEvaluationResponseListener;

    public WinstonDataClient(ClientDataHandler dataHandler) {
        this.dataHandler = dataHandler;
        this.winstonJMSClient = new WinstonJMSClient();
        this.dataConverter = winstonJMSClient.getDataConverter();
    }

    public void connect() throws Exception {
        this.connection = winstonJMSClient.connect();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.atomicSession = new AtomicReference<>(session);
        this.dataEvaluationResponseQueue = createDataEvaluationResponseQueue();
        createDataProcessingResponseQueue();
    }

    public void connect(Connection connection) throws JMSException {
        this.connection = connection;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.atomicSession = new AtomicReference<>(session);
        this.dataEvaluationResponseQueue = createDataEvaluationResponseQueue();
        createDataProcessingResponseQueue();
    }

    public void connectServer(String clientId) throws Exception {
        String queueName = WinstonJMSClient.getResourceName(clientId, DATA_EVALUATION_REQUEST_TOPIC);
        Queue queue = atomicSession.get().createQueue(queueName);
        MessageProducer producer = session.createProducer(queue);
        dataEvaluationRequestQueues.put(clientId, producer);
    }

    public void closeServer(String clientId) throws Exception {
        if (dataEvaluationRequestQueues.containsKey(clientId)) {
            MessageProducer messageProducer = dataEvaluationRequestQueues.get(clientId);
            messageProducer.close();
            dataEvaluationRequestQueues.remove(clientId);
        }
    }

    public Set<String> getServers() {
        return dataEvaluationRequestQueues.keySet();
    }

    public void sendDataEvaluationRequest(DataEvaluationRequest request) throws Exception {
        String responseValue = dataConverter.writeValue(request);
        TextMessage textMessage = atomicSession.get().createTextMessage(responseValue);
        textMessage.setJMSReplyTo(dataEvaluationResponseQueue);
        String correlationId = nextEvaluationCorrelationId();
        textMessage.setJMSCorrelationID(correlationId);
        String reference = request.getReference();
        int expected = dataEvaluationRequestQueues.size();
        dataEvaluationResponseListener.addCorrelationId(correlationId, expected).setTimeout(reference, 1000);
        for (MessageProducer producer : dataEvaluationRequestQueues.values()) {
            producer.send(textMessage);
        }
    }

    private String nextEvaluationCorrelationId() {
        return String.valueOf(evaluationCorrelationId.incrementAndGet());
    }

    private Queue createDataEvaluationResponseQueue() throws JMSException {
        TemporaryQueue temporaryQueue = atomicSession.get().createTemporaryQueue();
        MessageConsumer messageConsumer = atomicSession.get().createConsumer(temporaryQueue);
        this.dataEvaluationResponseListener = new DataEvaluationResponseListener(dataHandler, dataConverter, this);
        messageConsumer.setMessageListener(dataEvaluationResponseListener);
        return temporaryQueue;
    }

    void sendDataProcessingRequest(DataProcessingRequest request, Destination destination) throws JMSException {
        String requestValue = dataConverter.writeValue(request);
        TextMessage textMessage = atomicSession.get().createTextMessage(requestValue);
        textMessage.setJMSReplyTo(createDataProcessingResponseQueue());
        MessageProducer producer = atomicSession.get().createProducer(destination);
        producer.send(textMessage);
    }

    private Queue createDataProcessingResponseQueue() throws JMSException {
        Queue queue = atomicSession.get().createTemporaryQueue();
        MessageConsumer messageConsumer = atomicSession.get().createConsumer(queue);
        messageConsumer.setMessageListener(new DataProcessingResponseListener(dataHandler, dataConverter));
        return queue;
    }

    public void close() throws JMSException {
        connection.close();
    }

}

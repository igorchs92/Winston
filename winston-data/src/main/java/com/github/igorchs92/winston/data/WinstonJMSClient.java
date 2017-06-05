package com.github.igorchs92.winston.data;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * Created by Igor on 5-6-2017.
 */
public class WinstonJMSClient {

    public static final String DATA_EVALUATION_REQUEST_TOPIC = "data-evaluation-request-topic";

    private ConnectionFactory connectionFactory;
    private DataConverter dataConverter;

    public WinstonJMSClient(String url) {
        connectionFactory = new ActiveMQConnectionFactory(url);
        dataConverter = new DataConverter();
    }

    public WinstonJMSClient() {
        this("tcp://localhost:61616");
    }

    public Connection connect() throws JMSException {
        return connectionFactory.createConnection();
    }

    public DataConverter getDataConverter() {
        return dataConverter;
    }
}

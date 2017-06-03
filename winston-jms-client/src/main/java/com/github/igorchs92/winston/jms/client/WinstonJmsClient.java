package com.github.igorchs92.winston.jms.client;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by Igor on 3-6-2017.
 */
public class WinstonJmsClient {

    private static final Logger logger = LoggerFactory.getLogger(WinstonJmsClient.class);
    private String clientId;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public static void main(String[] args) throws JMSException {
        WinstonJmsClient winstonJmsClient = new WinstonJmsClient();
        winstonJmsClient.connect("Client", "client-queue");
        winstonJmsClient.sendName("test", "test");
    }

    public void connect(String clientId, String queueName) throws JMSException {
        this.clientId = clientId;
        // create a Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        // create a Connection
        connection = connectionFactory.createConnection();
        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // create the Queue to which messages will be sent
        Queue queue = session.createQueue(queueName);
        // create a MessageProducer for sending messages
        messageProducer = session.createProducer(queue);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void sendName(String firstName, String lastName) throws JMSException {
        String text = firstName + " " + lastName;
        // create a JMS TextMessage
        TextMessage textMessage = session.createTextMessage(text);
        // send the message to the queue destination
        messageProducer.send(textMessage);
        logger.info(clientId + ": sent message with text='{}'", text);
    }
}

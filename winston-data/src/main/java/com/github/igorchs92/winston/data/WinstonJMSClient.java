package com.github.igorchs92.winston.data;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.management.JMSManagementHelper;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Igor on 5-6-2017.
 */
public class WinstonJMSClient {

    private ConnectionFactory connectionFactory;
    private DataConverter dataConverter;

    public WinstonJMSClient(String url) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        activeMQConnectionFactory.setReconnectAttempts(-1);
        connectionFactory = activeMQConnectionFactory;
        dataConverter = new DataConverter();
    }

    public WinstonJMSClient() {
        this("tcp://localhost:61616?reconnectAttempts=10");
    }

    public Connection connect() throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    public static String getResourceName(String clientId, ResourceType resourceType) {
        return (clientId + "-" + resourceType.toString()).toLowerCase();
    }

    private static void management(final Connection connection) throws Exception
    {
        QueueSession session = ((QueueConnection)connection).createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue managementQueue = ActiveMQJMSClient.createQueue("activemq.management");
        QueueRequestor requestor = new QueueRequestor(session, managementQueue);
        connection.start();
        Message m = session.createMessage();
        JMSManagementHelper.putAttribute(m, ResourceType.DATA_EVALUATION_REQUEST_TOPIC.toString(), "countMessagesForSubscription");
        Message response = requestor.request(m);
        String queueNames = (String) JMSManagementHelper.getResult(response);
        System.out.println(queueNames);
    }


    public DataConverter getDataConverter() {
        return dataConverter;
    }
}

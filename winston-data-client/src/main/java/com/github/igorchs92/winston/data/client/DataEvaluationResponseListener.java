package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Igor on 4-6-2017.
 */
public class DataEvaluationResponseListener implements MessageListener {

    public DataEvaluationResponseListener(ClientDataHandler dataHandler, DataConverter dataConverter, WinstonDataClient dataClient) {

    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("Consumer " + Thread.currentThread().getName() + " received message: " + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

package com.github.igorchs92.winston.data.server.example;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.client.example.server.ServerDataHandler;
import com.github.igorchs92.winston.data.client.example.server.WinstonDataServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.util.Scanner;

/**
 * Created by Igor on 5-6-2017.
 */
public class WinstonDataServerExample {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataServerExample.class);

    public static void main(String[] args) throws JMSException {
        logger.info("Setting up the server...");
        WinstonDataServer winstonDataServer = new WinstonDataServer("data-server-example", createDataHandler());
        logger.info("Connecting to the message broker...");
        winstonDataServer.connect();
        logger.info("Connection has been established.");
        logger.info("Type 'quit' to stop the program.");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("quit")) {
            //continue
        }
        winstonDataServer.close();
    }

    private static ServerDataHandler createDataHandler() {
        return new ServerDataHandler() {

            @Override
            public DataEvaluationResponse evaluateDataRequest(DataEvaluationRequest request) {
                logger.info("Incoming Data Evaluation Request.");
                //evaluate data here
                return new DataEvaluationResponse();
            }

            @Override
            public DataProcessingResponse processDataRequest(DataProcessingRequest request) {
                logger.info("Incoming Data Processing Request.");
                //process data here
                return new DataProcessingResponse();
            }

        };
    }

}

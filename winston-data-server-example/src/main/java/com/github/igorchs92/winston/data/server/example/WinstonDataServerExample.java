package com.github.igorchs92.winston.data.server.example;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.server.ServerDataHandler;
import com.github.igorchs92.winston.data.server.WinstonDataServer;
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
        ServerDataHandler serverDataHandler = new ServerDataHandler() {
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
        WinstonDataServer winstonDataServer = new WinstonDataServer("data-server-example", serverDataHandler);
        logger.info("Connecting to the message broker...");
        winstonDataServer.connect();
        logger.info("Connection has been established.");
        logger.info("Type 'quit' to stop the program.");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("quit")) {
            logger.info("Invalid command, type 'quit' to stop the program.");
        }
        winstonDataServer.close();
    }

}

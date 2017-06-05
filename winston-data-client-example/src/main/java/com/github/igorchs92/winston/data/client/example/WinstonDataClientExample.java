package com.github.igorchs92.winston.data.client.example;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.client.example.client.ClientDataHandler;
import com.github.igorchs92.winston.data.client.example.client.WinstonDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by Igor on 5-6-2017.
 */
public class WinstonDataClientExample {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataClientExample.class);

    public static void main(String[] args) throws JMSException {
        logger.info("Setting up the client...");
        WinstonDataClient winstonDataClient = new WinstonDataClient(createDataHandler());
        logger.info("Connecting to the message broker...");
        winstonDataClient.connect();
        logger.info("Connection has been established.");
        logger.info("Type 'quit' to stop the program or enter to send a request.");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("quit")) {
            winstonDataClient.sendDataEvaluationRequest(new DataEvaluationRequest());
        }
        winstonDataClient.close();
    }

    private static ClientDataHandler createDataHandler() {
        return new ClientDataHandler() {

            @Override
            public Optional<DataEvaluationResponse> evaluateDataResponses(Collection<DataEvaluationResponse> responses) {
                logger.info("Incoming Data Evaluation Response, size: {}.", responses.size());
                //filter for the best response if it exists
                return responses.stream().findFirst();
            }

            @Override
            public DataProcessingRequest createDataProcessingRequest(DataEvaluationResponse response) {
                logger.info("Outgoing Data Processing Request.");
                //construct request
                return new DataProcessingRequest();
            }

            @Override
            public void processDataResponse(DataProcessingResponse response) {
                logger.info("Incoming Data Processing Response.");
                //process data
            }

        };
    }

}

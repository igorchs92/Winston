package com.github.igorchs92.winston.server.data.client;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.client.ClientDataHandler;
import com.github.igorchs92.winston.data.client.DataEvaluationResponseMap;
import com.github.igorchs92.winston.data.client.WinstonDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by Igor on 5-6-2017.
 */
public class WinstonDataClientExample {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataClientExample.class);

    public static void main(String[] args) throws Exception {
        logger.info("Setting up the client...");
        WinstonDataClient winstonDataClient = new WinstonDataClient(createDataHandler());
        logger.info("Connecting to the message broker...");
        winstonDataClient.connect();
        winstonDataClient.connectServer("example");
        logger.info("Connection has been established.");
        logger.info("Type 'quit' to stop the program or enter to send a request.");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("quit")) {
            Map<String, String> tokens = new HashMap<>();
            tokens.put("hello", "helo!");
            tokens.put("hello1", "helo2!");
            winstonDataClient.sendDataEvaluationRequest(new DataEvaluationRequest().setReference("testeststststtst").setTokens(tokens));
        }
        winstonDataClient.close();
    }

    private static ClientDataHandler createDataHandler() {
        return new ClientDataHandler() {

            @Override
            public Optional<DataEvaluationResponse> handleEvaluationResponseMap(DataEvaluationResponseMap responseMap) {
                logger.info("Incoming dataEvaluationResponse, amount: {}.", responseMap.getResponses().size());
                //filter for the best response if it exists
                return responseMap.getResponses().stream().findFirst();
            }

            @Override
            public void handleProcessingRequestException(DataProcessingRequest request, Exception ex) {
                logger.error("Could not process request!", ex);
            }

            @Override
            public void handleProcessingResponse(DataProcessingResponse response) {
                logger.info("Incoming Data Processing Response.");
            }

        };
    }

}

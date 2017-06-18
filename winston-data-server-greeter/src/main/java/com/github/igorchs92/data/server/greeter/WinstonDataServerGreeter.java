package com.github.igorchs92.data.server.greeter;

import com.github.igorchs92.winston.data.DataEvaluationRequest;
import com.github.igorchs92.winston.data.DataEvaluationResponse;
import com.github.igorchs92.winston.data.DataProcessingRequest;
import com.github.igorchs92.winston.data.DataProcessingResponse;
import com.github.igorchs92.winston.data.server.ServerDataHandler;
import com.github.igorchs92.winston.data.server.WinstonDataServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Scanner;

/**
 * Created by Igor on 8-6-2017.
 */
public class WinstonDataServerGreeter {

    private static final Logger logger = LoggerFactory.getLogger(WinstonDataServerGreeter.class);

    public static void main(String[] args) throws Exception {
        logger.info("Setting up the server...");
        WinstonDataServer winstonDataServer = new WinstonDataServer("greeter", createDataHandler());
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
                DataEvaluationResponse response = new DataEvaluationResponse();
                if (request.getTokens().containsKey("greeting")) {
                    response.setScore(100);
                } else {
                    response.setScore(0);
                }
                return response;
            }

            @Override
            public DataProcessingResponse processDataRequest(DataProcessingRequest request) {
                logger.info("Incoming Data Processing Request.");
                DataProcessingResponse response = DataProcessingResponse.from(request);
                Date dt = new Date();
                int hours = dt.getHours();
                if(hours>=0 || hours<=12){
                    response.setContent("Good morning!!");
                }else if(hours>=12 || hours<=16){
                    response.setContent("Good afternoon!");
                }else if(hours>=16 || hours<=21){
                    response.setContent("Good evening!");
                }else if(hours>=21 || hours<=24){
                    response.setContent("Good night!");
                }
                return response;
            }

        };
    }

}

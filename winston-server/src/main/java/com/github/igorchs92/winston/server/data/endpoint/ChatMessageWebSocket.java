package com.github.igorchs92.winston.server.data.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.igorchs92.winston.server.data.DataRequest;
import com.github.igorchs92.winston.server.data.DataResponse;
import com.github.igorchs92.winston.server.data.client.WinstonDataClientExample;
import com.github.igorchs92.winston.server.data.client.WinstonServerDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 1-6-2017.
 */
@EnableWebSocket
@ServerEndpoint(value = "/ws/data/message/")
public class ChatMessageWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageWebSocket.class);
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final List<Session> sessions = new ArrayList<>();

    public ChatMessageWebSocket() {
    }

    @OnError
    public void onError(Throwable ex) {
        logger.debug("A WebSocket error has occurred.", ex);
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connection ({}), has been opened.", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("Connection ({}), has been closed.", session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws Exception {
        DataRequest request = JSON.readValue(message, DataRequest.class);
        logger.info("Connection ({}), has sent: '{}'.", session.getId(), request.getContent());
        //WinstonServerDataClient.INSTANCE.send(request, response -> send(session, response.setConversationReference(request.getConversationReference())));
    }

    public void send(Session session, DataResponse dataResponse) {
        try {
            session.getAsyncRemote().sendText(JSON.writeValueAsString(dataResponse));
        } catch (JsonProcessingException ex) {
            logger.error("Could not process Json.", ex);
        }
    }

}

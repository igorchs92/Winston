package com.github.igorchs92.winston.server.data.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.igorchs92.winston.data.InputChatMessage;
import com.github.igorchs92.winston.data.OutputChatMessage;
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
@ServerEndpoint(value = "/ws/chat/message/")
public class ChatMessageWebSocket implements ChatMessageEndpoint {

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
    public void onMessage(Session session, String message) {
        InputChatMessage inputChatMessage = null;
        try {
            inputChatMessage = JSON.readValue(message, InputChatMessage.class);
        } catch (IOException ex) {
            logger.error("Could not read message from connection ({})", session.getId(), ex);
        }
        if (inputChatMessage != null) {
            logger.info("Connection ({}), has sent: '{}'.", session.getId(), inputChatMessage.getContent());
            OutputChatMessage outputChatMessage = new OutputChatMessage();
            outputChatMessage.setMessage("You have sent: '" + inputChatMessage.getContent() + "'.");
            send(session, outputChatMessage);
        }
    }

    public void send(Session session, OutputChatMessage outputChatMessage) {
        try {
            session.getAsyncRemote().sendText(JSON.writeValueAsString(outputChatMessage));
        } catch (JsonProcessingException ex) {
            logger.error("Could not process Json.", ex);
        }
    }

    @Override
    public void send(OutputChatMessage outputChatMessage) throws Exception {

    }
}

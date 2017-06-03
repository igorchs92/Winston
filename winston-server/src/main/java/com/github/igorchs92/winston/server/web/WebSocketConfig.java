package com.github.igorchs92.winston.server.web;

import com.github.igorchs92.winston.server.data.endpoint.ChatMessageWebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * Created by Igor on 1-6-2017.
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ChatMessageWebSocket webSocketService() {
        return new ChatMessageWebSocket();
    }

    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }

}
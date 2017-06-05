package com.github.igorchs92.winston.server.data.endpoint;

import com.github.igorchs92.winston.server.data.OutputChatMessage;

/**
 * Created by Igor on 1-6-2017.
 */
public interface ChatMessageEndpoint {

    void send(OutputChatMessage outputChatMessage) throws Exception;

}

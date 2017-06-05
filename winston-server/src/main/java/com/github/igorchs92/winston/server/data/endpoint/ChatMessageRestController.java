package com.github.igorchs92.winston.server.data.endpoint;


import com.github.igorchs92.winston.server.data.InputChatMessage;
import com.github.igorchs92.winston.server.data.OutputChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Igor on 1-6-2017.
 */
@RestController
@RequestMapping("/api/chat/message/")
public class ChatMessageRestController implements ChatMessageEndpoint {


    @PostMapping
    public ResponseEntity post(HttpServletRequest request, InputChatMessage inputChatMessage) {
        OutputChatMessage outputChatMessage = new OutputChatMessage();
        outputChatMessage.setMessage("You have sent: '" + inputChatMessage.getContent() + "'.");
        return ResponseEntity.ok(outputChatMessage);
    }

    @Override
    public void send(OutputChatMessage outputChatMessage) throws Exception {

    }

}

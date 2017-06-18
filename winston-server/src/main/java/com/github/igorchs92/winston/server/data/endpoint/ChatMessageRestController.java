package com.github.igorchs92.winston.server.data.endpoint;


import com.github.igorchs92.winston.server.data.DataRequest;
import com.github.igorchs92.winston.server.data.DataResponse;
import com.github.igorchs92.winston.server.data.client.DataResponseConsumer;
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
public class ChatMessageRestController {


    @PostMapping
    public ResponseEntity post(HttpServletRequest request, DataRequest chatMessageInput) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setContent("You have sent: '" + chatMessageInput.getContent() + "'.");
        return ResponseEntity.ok(dataResponse);
    }

}

package com.github.igorchs92.winston.server.data;

/**
 * Created by Igor on 1-6-2017.
 */
public class DataResponse {

    private String conversationReference;
    private String content;

    public DataResponse() {
    }

    public DataResponse(String conversationReference, String content) {
        this.conversationReference = conversationReference;
        this.content = content;
    }

    public String getConversationReference() {
        return conversationReference;
    }

    public DataResponse setConversationReference(String conversationReference) {
        this.conversationReference = conversationReference;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataResponse setContent(String content) {
        this.content = content;
        return this;
    }
}

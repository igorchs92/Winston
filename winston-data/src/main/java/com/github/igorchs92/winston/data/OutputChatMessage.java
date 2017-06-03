package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 1-6-2017.
 */
public class OutputChatMessage {

    private String conversationReference;
    private String content;

    public OutputChatMessage() {
    }

    public OutputChatMessage(String conversationReference, String content) {
        this.conversationReference = conversationReference;
        this.content = content;
    }

    public String getConversationReference() {
        return conversationReference;
    }

    public OutputChatMessage setConversationReference(String conversationReference) {
        this.conversationReference = conversationReference;
        return this;
    }

    public String getMessage() {
        return content;
    }

    public OutputChatMessage setMessage(String message) {
        this.content = message;
        return this;
    }
}

package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 1-6-2017.
 */
public class InputChatMessage {

    private String conversationReference;
    private String content;

    public InputChatMessage() {
    }

    public InputChatMessage(String conversationReference, String content) {
        this.conversationReference = conversationReference;
        this.content = content;
    }

    public String getConversationReference() {
        return conversationReference;
    }

    public InputChatMessage setConversationReference(String conversationReference) {
        this.conversationReference = conversationReference;
        return this;
    }

    public String getContent() {
        return content;
    }

    public InputChatMessage setContent(String content) {
        this.content = content;
        return this;
    }
}

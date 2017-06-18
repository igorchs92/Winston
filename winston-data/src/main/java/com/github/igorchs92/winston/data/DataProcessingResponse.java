package com.github.igorchs92.winston.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataProcessingResponse {

    private String reference = "";
    private String subject = "";
    private String content = "";
    private Map<String, String> tokens = new HashMap<>();

    public DataProcessingResponse() {
    }

    public DataProcessingResponse(String reference, String subject, String content) {
        this.reference = reference;
        this.subject = subject;
        this.content = content;
    }

    public String getReference() {
        return reference;
    }

    public DataProcessingResponse setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public DataProcessingResponse setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataProcessingResponse setContent(String content) {
        this.content = content;
        return this;
    }

    public Map<String, String> getTokens() {
        return tokens;
    }

    public DataProcessingResponse setTokens(Map<String, String> tokens) {
        this.tokens = tokens;
        return this;
    }

    public static DataProcessingResponse from(DataProcessingRequest request) {
        return new DataProcessingResponse()
                .setReference(request.getReference())
                .setSubject(request.getSubject())
                .setTokens(request.getTokens());
    }
}

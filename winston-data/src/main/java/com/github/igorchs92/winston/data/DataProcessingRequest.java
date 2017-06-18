package com.github.igorchs92.winston.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataProcessingRequest {

    private String reference = "";
    private String subject = "";
    private String content = "";
    private Map<String, String> tokens = new HashMap<>();

    public DataProcessingRequest() {
    }

    public DataProcessingRequest(String reference, String content, Map<String, String> tokens) {
        this.reference = reference;
        this.content = content;
        this.tokens = tokens;
    }

    public String getReference() {
        return reference;
    }

    public DataProcessingRequest setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public DataProcessingRequest setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DataProcessingRequest setContent(String content) {
        this.content = content;
        return this;
    }

    public Map<String, String> getTokens() {
        return tokens;
    }

    public DataProcessingRequest setTokens(Map<String, String> tokens) {
        this.tokens = tokens;
        return this;
    }

    public static DataProcessingRequest from(DataEvaluationResponse response) {
        return new DataProcessingRequest()
                .setReference(response.getReference())
                .setSubject(response.getSubject())
                .setTokens(response.getTokens());
    }

}

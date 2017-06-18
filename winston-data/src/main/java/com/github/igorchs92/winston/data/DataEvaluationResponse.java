package com.github.igorchs92.winston.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataEvaluationResponse {

    private String reference = "";
    private String subject = "";
    private int score = -1;
    private Map<String, String> tokens = new HashMap<>();

    public DataEvaluationResponse() {
    }

    public DataEvaluationResponse(String reference, String subject, int score) {
        this.reference = reference;
        this.subject = subject;
        this.score = score;
    }

    public String getReference() {
        return reference;
    }

    public DataEvaluationResponse setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public DataEvaluationResponse setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public int getScore() {
        return score;
    }

    public DataEvaluationResponse setScore(int score) {
        this.score = score;
        return this;
    }

    public Map<String, String> getTokens() {
        return tokens;
    }

    public DataEvaluationResponse setTokens(Map<String, String> tokens) {
        this.tokens = tokens;
        return this;
    }

    public static DataEvaluationResponse from(DataEvaluationRequest request) {
        return new DataEvaluationResponse()
                .setReference(request.getReference());
                //.setTokens(request.getTokens());
    }
}

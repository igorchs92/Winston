package com.github.igorchs92.winston.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataEvaluationRequest {

    private String reference = "";
    private Map<String, String> tokens = new HashMap<>();

    public DataEvaluationRequest() {
    }

    public DataEvaluationRequest(String reference, Map<String, String> tokens) {
        this.reference = reference;
        this.tokens = tokens;
    }

    public String getReference() {
        return reference;
    }

    public DataEvaluationRequest setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public Map<String, String> getTokens() {
        return tokens;
    }

    public DataEvaluationRequest setTokens(Map<String, String> tokens) {
        this.tokens = tokens;
        return this;
    }

}

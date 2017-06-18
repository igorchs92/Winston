package com.github.igorchs92.winston.data.client;

import com.github.igorchs92.winston.data.DataEvaluationResponse;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 7-6-2017.
 */
public class DataEvaluationResponseMap {

    private int expected;
    private long start;
    private long timeout;
    private String reference;
    private Map<DataEvaluationResponse, Destination> responses = new HashMap<>();

    DataEvaluationResponseMap(int expected) {
        this.expected = expected;
        this.start = System.currentTimeMillis();
        this.timeout = 0;
    }

    DataEvaluationResponseMap setTimeout(String reference, long timeout) {
        this.timeout = timeout;
        return this;
    }

    void addResponse(DataEvaluationResponse response, Destination destination) {
        responses.put(response, destination);
    }

    public String getReference() {
        return reference;
    }

    DataEvaluationResponseMap setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public Set<DataEvaluationResponse> getResponses() {
        return responses.keySet();
    }

    Destination getDestination(DataEvaluationResponse response) {
        return responses.get(response);
    }

    boolean isDone() {
        if (responses.size() >= expected) {
            return true;
        } else if (timeout != 0) {
            return (System.currentTimeMillis() >= start + timeout);
        }
        return false;
    }
}

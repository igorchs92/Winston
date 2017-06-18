package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 7-6-2017.
 */
public enum ResourceType {

    DATA_EVALUATION_REQUEST_TOPIC("data-evaluation-request-topic");

    private String name;

    ResourceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

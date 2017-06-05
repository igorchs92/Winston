package com.github.igorchs92.winston.data;

/**
 * Created by Igor on 3-6-2017.
 */
public class DataEvaluationRequest {

    private String content;

    public DataEvaluationRequest() {
    }

    public DataEvaluationRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public DataEvaluationRequest setContent(String content) {
        this.content = content;
        return this;
    }

}
